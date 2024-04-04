package com.gls.ppldv.user.service;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.common.util.CookieUtils;
import com.gls.ppldv.common.util.FileUtil;
import com.gls.ppldv.common.util.GmailAuthentication;
import com.gls.ppldv.configuration.userException.LoginFailedException;
import com.gls.ppldv.developer.service.DeveloperService;
import com.gls.ppldv.user.dto.EditDTO;
import com.gls.ppldv.user.dto.LoginDTO;
import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.PassCode;
import com.gls.ppldv.user.mapper.MemberMapper;
import com.gls.ppldv.user.repository.CodeRepository;
import com.gls.ppldv.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository mr;
	private final CodeRepository cr;
	private final MemberMapper mm;

	private final FileUtil fu;
	
	@Autowired
	private DeveloperService ds;

	// 만들어놓은 util 패키지의 GmailAuthentication
	@Autowired
	GmailAuthentication ga;

	@Override
	@Transactional // 2개 이상의 db 처리 관리
	public String register(Member member, MultipartFile file) throws Exception {
		String message = "Register Failed";
		Member m = mm.idCheck(member.getEmail());
		
		// 아이디 중복 체크
		if (m != null) {
			return "ID Duplicate Retry!";
		}
		
		if (file != null && !file.isEmpty()) {
			// 비밀번호 encoding
			String encPass = CookieUtils.encrypt(member.getPassword());
			member.setPassword(encPass);
			
			// 이미지 업로드
			String imgUrl = fu.uploadFile(file);
			String savedFileName = fu.savedFileName(imgUrl);
			if (imgUrl != null) { // 이미지 업로드 성공 (S3)
				// 이미지 URL 설정
				member.setFileName(savedFileName);
				member.setImgUrl(imgUrl);
				// 회원 정보 저장
				Member mem = mr.save(member);
				if (mem != null) {
					message = "Register Success";
				}
			}
		} else {
			// 비밀번호 encoding
			String encPass = CookieUtils.encrypt(member.getPassword());
			member.setPassword(encPass);
			
			Member mem = mr.save(member);
			if (mem != null) {
				message = "Register Success";
			}
		}
		return message;
	}

	@Override
	@Transactional
	public Member login(LoginDTO member) throws Exception {
		
		Member mem = mr.findByEmail(member.getEmail()).get();
		
		String encryptedPassword = mem.getPassword();
		
		String decryptedPassword = CookieUtils.decrypt(encryptedPassword);
		
		Member m = null;
		
		if (decryptedPassword.equals(member.getPassword())) {
			m = mr.findByEmail(member.getEmail()).get();
		}
		
		if (m != null) {
			// 로그인 성공
			return m;
		} else {
			// 로그인 실패
			throw new LoginFailedException("NOT EQUAL");
		}
	}

	@Override
	@Transactional
	public String findPassSubmit(Member member, HttpServletRequest request) throws Exception {

		Member m = mr.findByEmailAndName(member.getEmail(), member.getName());

		if (m == null) {
			// 일치하는 회원이 존재하지 않는다면,
			return "NOT EXISTS";
		}

		// 일치하는 회원 존재
		// 5자리의 숫자 코드 생성
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int random = (int) (Math.random() * 10);
			sb.append(random);
		}
		String code = sb.toString();

		PassCode pc = null;
		
		PassCode existingPassCode = cr.findByEmail(m.getEmail());
		
		if (existingPassCode != null) {
			// 기존에 이메일 정보가 존재하는 경우
			pc = existingPassCode;
			existingPassCode.setCode(code);
			cr.save(existingPassCode);
		} else {
			// 메일로 발송될 코드 DB에 저장
			pc = new PassCode();
			pc.setEmail(m.getEmail());
			pc.setCode(code);
			cr.save(pc);
		}

		// 메일 발송
		Session session = Session.getDefaultInstance(ga.getProp(), ga);
		MimeMessage msg = new MimeMessage(session);
		InternetAddress fromAddress = new InternetAddress("trailblazer6351@gmail.com", "MASTER");
		InternetAddress toAddress = new InternetAddress(m.getEmail());

		msg.setSentDate(new Date()); // 보내는 날짜
		msg.setHeader("Content-Type", "text/html;charset=utf-8"); // 마임 타입
		msg.setRecipient(Message.RecipientType.TO, toAddress); // 송신자
		msg.setFrom(fromAddress); // 발신자
		msg.setSubject("Find Pass Request", "utf-8"); // 제목
		StringBuilder mail = new StringBuilder();
		mail.append("<!DOCType html>");
		mail.append("<html>");
		mail.append("<head>");
		mail.append("<meta charset='utf-8'>");
		mail.append("</head>");
		mail.append("<body>");
		mail.append("<h1 style='text-align:center;'> @@@ PEOPLE.DRIVER Site Find Pass @@@ </h1>");
		mail.append("<div style='text-align:center;'>");
		mail.append("<p style='font-size:30px;'>Auth Code : <b>" + pc.getCode() + "</b> </p>");
		mail.append("</div>");
		mail.append("</body>");
		mail.append("</html>");

		String content = mail.toString();
		msg.setContent(content, "text/html;charset=utf-8");
		// blocking (메일이 발송될 때 까지)
		Transport.send(msg);
		return "Mail Translation Success";
	}

	@Override
	public String changePassCode(PassCode passCode) {
		PassCode pc = cr.findByEmailAndCode(passCode.getEmail(), passCode.getCode());
		if (pc != null) {
			// 일치하면
			return "Code Equal";
		} else {
			// 일치하지 않으면
			return "Code Not Equal";
		}
	}
	
	@Override
	@Transactional
	public void removeCode(String email) {
		cr.deleteByEmail(email);
	}

	@Override
	public String changePass(Member member) throws Exception {
		
		// 암호화 해서 다시 저장
		String encryptedPassword = CookieUtils.encrypt(member.getPassword());
		member.setPassword(encryptedPassword);
		
		mm.changePass(member);

		return "Pass Change Success";
	}

	@Override
	@Transactional
	public String editProfile(EditDTO member, MultipartFile file) throws Exception {

		String message = "Edit Failed";
		Member mem = mr.findByEmail(member.getEmail()).get();
		
		if (file != null && !file.isEmpty()) {
			// 만약 회원 이미지가 변경되었다면, 삭제 후 다시 업로드
			if (mem.getImgUrl() != null) {
				// 기존 이미지가 있었다면,
				fu.deleteFile(mem.getFileName());
			}

			String imgUrl = fu.uploadFile(file);
			String savedFileName = fu.savedFileName(imgUrl);
			if (imgUrl != null) {
				// 회원정보가 잘 수정되었다면,
				member.setFileName(savedFileName);
				member.setImgUrl(imgUrl);
				mm.editProfile(member);
				message = "Edit Success";
			}
		} else {
			if (mem.getImgUrl() != null) {
				// 파일을 바꾸진 않았지만, 기존에 올려둔 이미지가 존재했었다면,
				member.setImgUrl(mem.getImgUrl());
				member.setFileName(mem.getFileName());
				// 기존에 있던 정보를 추가해서
			}
			mm.editProfile(member);

			message = "Edit Success";
		}
		return message;
	}

	@Override
	@Transactional
	public void removeUser(String email) throws Exception {
		
		Member member = mr.findByEmail(email).get();
		
		// 가장 큰 문제점 종속성이 강해진다. (이걸 해결할 수 있는 방안이 있을까?)
		String message = ds.removeAll(member.getId());
		
		if (message.equals("Remove Success")) {
			if (member.getImgUrl() != null) {
				fu.deleteFile(member.getFileName());
			}
			mr.deleteByEmail(email);
		}
	}

	@Override
	public Member findMember(String email) throws Exception {
		Member m = mr.findByEmail(email).get();
		return m;
	}

	

}
