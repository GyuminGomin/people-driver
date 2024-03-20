package com.gls.ppldv.developer.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.AbstractDocument.BranchElement;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.common.util.Criteria;
import com.gls.ppldv.common.util.FileUtil;
import com.gls.ppldv.common.util.PageMaker;
import com.gls.ppldv.common.util.Paging.Cri;
import com.gls.ppldv.common.util.Paging.PMaker;
import com.gls.ppldv.developer.dto.DeveloperDTO;
import com.gls.ppldv.developer.entity.DCareer;
import com.gls.ppldv.developer.entity.DLicense;
import com.gls.ppldv.developer.entity.Developer;
import com.gls.ppldv.developer.repository.DCareerRepository;
import com.gls.ppldv.developer.repository.DLicenseRepository;
import com.gls.ppldv.developer.repository.DeveloperRepository;
import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

	private final DeveloperRepository dr;
	private final DCareerRepository dcr;
	private final DLicenseRepository dlr;
	private final MemberRepository mr;

	private final FileUtil fu;

	@Transactional
	public String register(DeveloperDTO developerDTO, MultipartFile file) throws Exception {

		String message = "Register Failed";

		// DeveloperDTO에서 Developer 엔티티로 변환
		Developer developer = new Developer();
		developer.setTitle(developerDTO.getTitle());
		developer.setContent(developerDTO.getContent());
		developer.setSkill(developerDTO.getSkill());
		developer.setTendency(developerDTO.getTendency());
		developer.setSchool(developerDTO.getSchool());
		Optional<Member> memberOptional = mr.findById(developerDTO.getUno());
		developer.setMember(memberOptional.get());
		developer.setUpdateDate(new Date());
		developer.setViewCount(0);

		String imgUrl = null;
		String fileName = null;

		if (file != null && !file.isEmpty()) {
			// 이미지 파일 처리
			imgUrl = fu.uploadFile(file);
			if (imgUrl == null) {
				message = "Network Problem Wait A Moment Please";
				return message;
			}
			fileName = fu.savedFileName(imgUrl);
			developer.setImgUrl(imgUrl);
			developer.setFileName(fileName);
		}
		
		// Developer Entity 저장
		Developer savedDeveloper = dr.save(developer);

		// DCareer 엔티티 저장
		if (developerDTO.getDCareer() != null && !developerDTO.getDCareer().isEmpty()) {
			for (DCareer career : developerDTO.getDCareer()) {
				if (career.getJobName() != null && !career.getJobName().isEmpty()
						|| career.getJobPeriod() != null && !career.getJobPeriod().isEmpty()
						|| career.getJobResponsibilities() != null && !career.getJobResponsibilities().isEmpty()) {
					career.setDeveloper(savedDeveloper);
					dcr.save(career);
				}
				
			}
		}

		// DLicense 엔티티 저장 (여러개의 데이터를 검사할 때, 꼭 리스트 안에 들어있는 데이터도 검사 필수)
		if (developerDTO.getDLicense() != null && !developerDTO.getDLicense().isEmpty()) {
			for (DLicense license : developerDTO.getDLicense()) {
				if (license.getLicenseName() != null && !license.getLicenseName().isEmpty()
						|| license.getAcquisitionDate() != null && !license.getAcquisitionDate().isEmpty()) {
					license.setDeveloper(savedDeveloper);
					dlr.save(license);
				}
			}
		}

		if (savedDeveloper != null) {
			message = "Register Success";
		} else {
			fu.deleteFile(fileName);
			return message;
		}

		return message;
	}

	@Override
	@Transactional
	public Page<Developer> searchDev(Long id, Cri cri) {
		// 페이징 처리 된 게시물 개수 받아오기 (jpa)
		Page<Developer> dlist = null;
		Page<Developer> dlist2 = null;
		// 아직 JPA의 Query 어노테이션에 대해 배우지 않아서
		/* Pageable pageable = PageRequest.of(pageNumber, 5); */
		Sort sort = Sort.by(Sort.Direction.DESC, "updateDate");

		// 회원 id와 cri를 받아서 페이징 처리에 사용
		Pageable pageable = PageRequest.of(cri.getPage1() - 1, cri.getPerPageNum1(), sort);
		dlist = dr.findByMemberId(mr.findById(id).get().getId(), pageable);
		return dlist;
	}
	
	@Override
	@Transactional
	public Page<Developer> searchDev2(Criteria cri) {
		// 페이징 처리 된 게시물 개수 받아오기 (jpa)
		Page<Developer> dlist = null;
		// 아직 JPA의 Query 어노테이션에 대해 배우지 않아서
		/* Pageable pageable = PageRequest.of(pageNumber, 5); */
		Sort sort = Sort.by(Sort.Direction.DESC, "updateDate");

		// 회원 id와 cri를 받아서 페이징 처리에 사용
		Pageable pageable = PageRequest.of(cri.getPage() - 1, cri.getPerPageNum(), sort);
		dlist = dr.findAll(pageable);
		return dlist;
	}
	
	@Override
	@Transactional
	public Page<Developer> searchDev3(String name, Criteria cri) {
		// 페이징 처리 된 게시물 개수 받아오기 (jpa)
		Page<Developer> dlist = null;
		// 아직 JPA의 Query 어노테이션에 대해 배우지 않아서
		/* Pageable pageable = PageRequest.of(pageNumber, 5); */
		Sort sort = Sort.by(Sort.Direction.DESC, "updateDate");

		// 회원 id와 cri를 받아서 페이징 처리에 사용
		Pageable pageable = PageRequest.of(cri.getPage() - 1, cri.getPerPageNum(), sort);
		dlist = dr.findByMemberNameContaining(name, pageable);
		return dlist;
	}

	@Override
	public PMaker getPageMaker(Long id, Cri cri) throws Exception {
		// 전체 게시물 개수
		int totalCount = dr.countByMemberId(mr.findById(id).get().getId());

		PMaker pm = new PMaker(cri, totalCount);
		return pm;
	}
	
	@Override
	public PageMaker getPageMaker(Criteria cri) throws Exception {
		// 전체 게시물 개수
		int totalCount = (int)dr.count();

		PageMaker pm = new PageMaker(cri, totalCount);
		return pm;
	}
	
	@Override
	public PageMaker getPageMaker(String name, Criteria cri) throws Exception {
		// 전체 게시물 개수
		int totalCount = (int)dr.countByMemberNameContaining(name);

		PageMaker pm = new PageMaker(cri, totalCount);
		return pm;
	}
	
	@Override
	public DeveloperDTO searchDev(Long dno) {
		Developer developer = dr.findById(dno).get();
		List<DCareer> dcar = dcr.findByDeveloperDno(dno);
		List<DLicense> dlic = dlr.findByDeveloperDno(dno);
		
		DeveloperDTO developerDTO = new DeveloperDTO();
		developerDTO.setTitle(developer.getTitle());
		developerDTO.setContent(developer.getContent());
		developerDTO.setSkill(developer.getSkill());
		developerDTO.setSchool(developer.getSchool());
		developerDTO.setTendency(developer.getTendency());
		developerDTO.setImgUrl(developer.getImgUrl());
		developerDTO.setFileName(developer.getFileName());
		developerDTO.setUno(developer.getMember().getId());
		
		developerDTO.setDCareer(dcar);
		developerDTO.setDLicense(dlic);
		
		return developerDTO;
	}

	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public String edit(Long dno, DeveloperDTO developerDTO, MultipartFile file) throws Exception {
		String message = "Edit Failed";
		
		Developer dev = dr.findById(dno).get();
		
		// DeveloperDTO에서 Developer 엔티티로 변환
		dev.setTitle(developerDTO.getTitle());
		dev.setContent(developerDTO.getContent());
		dev.setSkill(developerDTO.getSkill());
		dev.setTendency(developerDTO.getTendency());
		dev.setSchool(developerDTO.getSchool());
		dev.setUpdateDate(new Date());

		String imgUrl = null;
		String fileName = null;

		// 이미지 파일 분류를 3 단계
		// 원상태로 되돌리기
		// 이미지 수정
		// 이미지 수정하지 않고 그대로 둔거
		
		// 만약 이미지 파일이 수정된게 있다면,
		if (file != null && !file.isEmpty()) {
			// 기존에 이미지가 없었다면,
			if (dev.getImgUrl() == null) {
				// 이미지 파일 처리
				imgUrl = fu.uploadFile(file);
				if (imgUrl == null) {
					message = "Network Problem Wait A Moment Please";
					return message;
				}
				fileName = fu.savedFileName(imgUrl);
				dev.setImgUrl(imgUrl);
				dev.setFileName(fileName);
			} else {
				// 기존에 이미지가 있었다면,
				fu.deleteFile(dev.getFileName());
				imgUrl = fu.uploadFile(file);
				if (imgUrl == null) {
					message = "Network Problem Wait A Moment Please";
					return message;
				}
				fileName = fu.savedFileName(imgUrl);
				dev.setImgUrl(imgUrl);
				dev.setFileName(fileName);
			}
		} else {
			// 기존에 파일이 있었지만 바꾸지 않았을 경우
			// -> 아무 수정도 하지 않아야 함
			
			// 기존에 파일이 있었지만 원상태로 되돌렸을 경우
			// -> 기존 파일 삭제를 해야함
			
			// 기존에 파일이 없었지만 바꾸지 않았을 경우
			// -> 아무 수정도 하지 않아야 함
			
			// 기존에 파일이 없었지만 원상태로 되돌렸을 경우
			// -> 아무 수정도 하지 않아야 함
			if (dev.getImgUrl() != null && developerDTO.isFlag()) {
				fu.deleteFile(dev.getFileName());
				dev.setImgUrl(null);
				dev.setFileName(null);
			}
		}	
		
		
		// UPDATE (DEVELOPER)
		Developer updateDev = dr.save(dev);
		
		// 기존에 있던 정보 다 지우고(DELETE)
		dcr.deleteAllByDeveloperDno(dno);
		// 기존에 있던 정보 다 지우고(DELETE)
		dlr.deleteAllByDeveloperDno(dno);
		
		// DCareer 엔티티 저장
		if (developerDTO.getDCareer() != null && !developerDTO.getDCareer().isEmpty()) {
			for (DCareer career : developerDTO.getDCareer()) {
				if (career.getJobName() != null && !career.getJobName().isEmpty()
						|| career.getJobPeriod() != null && !career.getJobPeriod().isEmpty()
						|| career.getJobResponsibilities() != null && !career.getJobResponsibilities().isEmpty()) {
					
					// 다시 삽입(INSERT)
					career.setDeveloper(dev);
					dcr.save(career);
				}
				
			}
		}

		// DLicense 엔티티 저장 (여러개의 데이터를 검사할 때, 꼭 리스트 안에 들어있는 데이터도 검사 필수)
		if (developerDTO.getDLicense() != null && !developerDTO.getDLicense().isEmpty()) {
			for (DLicense license : developerDTO.getDLicense()) {
				if (license.getLicenseName() != null && !license.getLicenseName().isEmpty()
						|| license.getAcquisitionDate() != null && !license.getAcquisitionDate().isEmpty()) {
					
					// 다시 삽입(INSERT)
					license.setDeveloper(dev);
					dlr.save(license);
				}
			}
		}

		if (updateDev != null) {
			message = "Edit Success";
		} else {
			return message;
		}

		return message;
	}

	@Override
	@Transactional
	public String remove(Long dno) {
		Developer d = dr.findById(dno).get();
		if (d.getImgUrl() != null) {
			try {
				fu.deleteFile(d.getFileName());
			} catch (IOException e) {}
		}
		
		dr.deleteById(dno);
		
		return "Remove Success";
	}

	@Override
	@Transactional
	public String removeAll(Long uno) {
		List<Developer> deList = dr.findAllByMemberId(uno);
		
		for (Developer d : deList) {
			if (d.getImgUrl() != null) {
				try {
					fu.deleteFile(d.getFileName());
				} catch (IOException e) {}
			}
		}
		
		dr.deleteAllByMemberId(uno);
		
		return "Remove Success";
	}

	@Override
	public Member findName(Long uno) {
		
		Member m = mr.findById(uno).get();
		
		return m;
	}

	@Override
	public void updateViewCnt(Long dno) {
		dr.updateViewCount(dno);
	}
}
