<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css"
	href="${path}/resources/css/developer/register.css" />

<c:set var="content">
	<input id="uno" type="hidden" value="${loginMember.id}" />
	<div class="container">
		<div class="input">
			<span>개발자 프로필</span> <span> 제목 <input id="title" type="text"
				placeholder="제목입력" autofocus="autofocus" />
			</span> <span id="register" onclick="register()">프로필 등록</span>
		</div>

		<div class="profile">
			<div class="firstRow">
				<div class="img">
					<img src="${path}/resources/img/profile.jpg" id="sampleImg" />
					<button type="button" onclick="rollback()">원상태로 되돌리기</button>
					<span>Drag & DROP</span>
				</div>
				<div class="introduce">
					<span>개발자 소개</span>
					<textarea class="introduce2" id="introduce"
						placeholder="1000자 이내로 작성해주세요."></textarea>
				</div>
			</div>
			<div class="secondRow">
				<div class="tendency">
					<h3>성향</h3>
					<div>
						<input type="radio" id="tend" name="tend" class="tend"
							value="FRONT" checked /> <label for="tend">프론트엔드</label> <input
							type="radio" id="tend2" name="tend" class="tend" value="BACK" />
						<label for="tend2">백엔드</label> <input type="radio" id="tend3"
							name="tend" class="tend" value="APP" /> <label for="tend3">앱</label>
					</div>
				</div>
				<div class="introduce">
					<span>개발자 기술스택</span>
					<textarea class="introduce2" id="skill"
						placeholder="1000자 이내로 작성해주세요."></textarea>
				</div>
			</div>

			<div class="thirdRow">
				<table border="1">
					<tr>
						<th colspan="2"><span>학력사항</span></th>
					</tr>
					<tr>
						<th>학교명</th>
						<td><textarea id="school"
								placeholder="학력사항에 대해 간단하게 설명해주세요. ex. 어느 고등학교 : 언제 입학, 졸업"></textarea>
						</td>
					</tr>
				</table>
			</div>

			<div class="fourthRow">
				<table border="1">
					<tr>
						<th colspan="3"><span>경력사항</span></th>
					</tr>
					<tr>
						<th>직장명</th>
						<th>근무기간</th>
						<th>담당업무</th>
					</tr>
					<tr>
						<td><input class="nameList" type="text"
							placeholder="IT 관련 직장" /></td>
						<td><input class="periodList" type="text"
							placeholder="ex. 18.03.18 ~ 24.03.18 (6년 근무)" /></td>
						<td><input class="resList" type="text"
							placeholder="ex. 개발자 (웹 엔지니어 전문)" /></td>
					</tr>
					<tr>
						<td><input class="nameList" type="text" /></td>
						<td><input class="periodList" type="text" /></td>
						<td><input class="resList" type="text" /></td>
					</tr>
					<tr>
						<td><input class="nameList" type="text" /></td>
						<td><input class="periodList" type="text" /></td>
						<td><input class="resList" type="text" /></td>
					</tr>
				</table>
			</div>

			<div class="fifthRow">
				<table border="1">
					<tr>
						<th colspan="2"><span>보유자격증</span></th>
					</tr>
					<tr>
						<th>자격증명</th>
						<th>취득기간</th>
					</tr>
					<tr>
						<td><input class="licList" type="text"
							placeholder="ex. 리눅스마스터 1급 (KAIT 자격검정)" /></td>
						<td><input class="acqList" type="text"
							placeholder="ex. 18.06.03 취득" /></td>
					</tr>
					<tr>
						<td><input class="licList" type="text" /></td>
						<td><input class="acqList" type="text" /></td>
					</tr>
					<tr>
						<td><input class="licList" type="text" /></td>
						<td><input class="acqList" type="text" /></td>
					</tr>
				</table>
			</div>

		</div>
	</div>
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp"%>

<script>

	$(document).ready(function() {
		// 컨텐츠 가로 길이 조절
		function setImgSize() {
			let firstRowHeight = $(".firstRow").height();
			
			let profileImg = $("#sampleImg");
			let introduce = $(".introduce");
			let tendency = $(".tendency");
			let imgdiv = $(".img");
			
			// 가로 크기를 세로와 동일하게 바꿈 (이미지)
			imgdiv.css({
				width: firstRowHeight + 'px'
			});
			
			// 변경됭 이미지 크기에 맞춰 가로 변경 (개발자 소개)
			let imgWidth = profileImg.width();
			introduce.css({
				width: 'calc(98% - ' + imgWidth + 'px)'
			});
			
			// 이미지 크기에 맞춰 가로 변경 (성향)
			tendency.css({
				width: firstRowHeight + 'px'
			});
		}
		
		// 페이지 로드 시 이미지 크기 설정
		setImgSize();
		
		// 창 크기 변경 시 이미지 크기 다시 설정
		$(window).resize(function() {
			setImgSize();
		});
	});
	
	// sampleImg drag & drop 시 브라우저가 파일을 해석 하려는 기본이벤트를 제거
	$("#sampleImg").on("dragenter dragover", function(e) {
		e.preventDefault();
	});
	
	var formData = new FormData();
	
	$("#sampleImg").on("drop", function(e) {
		e.preventDefault();
		
		let allowedExt = ["jpg", "jpeg", "png", "gif"];
		let maxSize = 10485760;
		
		let files = e.originalEvent.dataTransfer.files;
		let file = files[0];
		let fileName = file.name;
		let fileExt = fileName.split(".").pop().toLowerCase();
		
		if (files.length !== 1 || !allowedExt.includes(fileExt)) {
			alert("파일은 이미지 파일로 한개만 올려주세요.(jpg, jpeg, png, gif)");
			return false;
		}
		
		if (file.size > maxSize) {
			alert("파일 크기는 10MB 이하로 올려주세요.");
			return false;
		}
		
		let path = window.URL.createObjectURL(file);
		$("#sampleImg").attr("src", path);
		$("#sampleImg").css("opacity", 1);
		$(".img span").hide();
		formData.append("file", file);
	});
	
	function rollback() {
		$("#sampleImg").attr("src", "/resources/img/profile.jpg");
		$("#sampleImg").css("opacity", 0.6);
		$(".img span").show();
		formData.delete("file");
	}
	
	// 만약 등록 버튼을 클릭한다면, img는 ajax로 나머지는 폼태그로 보내자
	function register() {
		let title = $("#title");
		let introduce = $("#introduce");
		let tendency = $(".tend:checked").val();
		let skill = $("#skill");
		let school = $("#school");
		let nameList = $(".nameList");
		let periodList = $(".periodList");
		let resList = $(".resList");
		let licList = $(".licList");
		let acqList = $(".acqList");
		let uno = $("#uno");
		
		if (title.val() === '') {
			alert('제목은 필수로 입력해야 합니다.');
			return false;
		}
		
		formData.append('title', title.val());
		formData.append('content', introduce.val());
		formData.append('skill', skill.val());
		formData.append('tendency', tendency);
		formData.append('school', school.val());
		formData.append('uno', uno.val());
		formData.append('${_csrf.parameterName}', '${_csrf.token}');
		
		nameList.each(function(index) {
			let jobName = $(this).val();
	        let period = periodList.eq(index).val();
	        let responsibility = resList.eq(index).val();

	        formData.append('dCareer[' + index + '].jobName', jobName);
	        formData.append('dCareer[' + index + '].jobPeriod', period);
	        formData.append('dCareer[' + index + '].jobResponsibilities', responsibility);
		});
		
		licList.each(function(index) {
			let licenseName = $(this).val();
	        let acquisitionDate = acqList.eq(index).val();

	        formData.append('dLicense[' + index + '].licenseName', licenseName);
	        formData.append('dLicense[' + index + '].acquisitionDate', acquisitionDate);
		});
		
		$.ajax({
			type: "POST",
			url: "/developer/register",
			data: formData,
			processData: false,
			contentType: false,
			dataType: "text",
			success: function(result) {
				alert("등록 성공");
				window.location.href="/developer/profile?id=${loginMember.id}";
			},
			error: function(res) {
				alert("처리가 실패되었습니다. 잠시 후 다시 시도해주세요.");
			}
		})
	}
</script>