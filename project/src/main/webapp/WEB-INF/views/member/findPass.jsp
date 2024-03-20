<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/findPass.css"/>
	
<c:set var="content">
	
	<section>
		<div class="mail-ready">
			<div class="mail-ready-content">
				<p>메일을 전송하는 중입니다. 잠시 기다려주세요...</p>
			</div>
		</div>
		<div class="form">
			<h1>비밀번호 찾기</h1>
			<p>회원가입 시 등록 한 아이디(email)와 이름을 입력해주세요.</p>
			<div class="findPassForm">
				<form action="/user/passAuth" method="post">
					<input type="email" name="email" id="email" placeholder="이메일 주소" required />
					<input type="hidden" name="csrf_token" value="${csrf_token}" />
					<input type="submit" id="formSubmit" style="display:none;"/>
				</form>
				<input type="text" name="name" id="name" placeholder="이름" required />
			</div>
			<div class="findPassBtn">
				<button onclick="submit()">확인</button>
			</div>
		</div>
	</section>
</c:set>

<script>
	function submit() {
		let email = $("#email");
		let name = $("#name");
		
		// 메일 전송 대기 창
		let ready = $(".mail-ready");
		
		// 정규식
		var regexEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 정규표현식 이메일
		
		ready.css("display", "block");
		
		if (email.val() === '') {
			alert("이메일을 입력해주세요.");
			email.focus();
		} else if (!regexEmail.test(email.val())) {
			alert("이메일 형식이 맞지 않습니다.");
			email.val('');
			email.focus();
		} else if (name.val() === '') {
			alert("이름을 입력해주세요.");
			name.focus();
		} else {
			$.ajax({
				type : "post",
				url : "/user/find",
				data : {
					email : email.val(),
					name : name.val()
				},
				dataType : "text",
				success : function(result) {
					if (result === 'NOT EXISTS') {
						alert(result);
						email.val('');
						name.val('');
						email.focus();
						ready.css("display", "none");
					} else {
						// 메일 발송 성공
						alert("메일 발송 성공 메일함을 확인해주세요.");
						$("#formSubmit").click();
					}
				},
				error : function(res) {
					alert("메일 발송 실패 운영자한테 문의해주세요.");
				}
			});
		}
	}	
</script>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>