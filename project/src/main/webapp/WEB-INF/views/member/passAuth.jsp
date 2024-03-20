<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/passAuth.css"/>

<c:set var="content">
	<div class="form">
		<form action="/user/changePass" method="post">
			<input type="hidden" name="email" id="email" value="${email}" />
			<input type="submit" id="formSubmit" style="display:none;"/>
		</form>
		<h1>코드 인증</h1>
		<p>이메일로 전송받은 5자리 코드를 입력해주세요.</p>
		<div class="checkCodeForm">
			<input type="text" id="code" placeholder="코드 입력" required>
		</div>
		<div class="checkCodeBtn">
			<button onclick="submit()">확인</button>
		</div>
	</div>
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>

<script>
	function submit() {
		let code = $("#code");
		let email = $("#email");
		
		// 정규식
		var regexCode = /^\d{5}$/;
		
		if (code.val() === '') {
			alert('코드를 입력해주세요.');
			code.focus();
		} else if (!regexCode.test(code.val())) {
			alert('코드는 5자리의 숫자입니다.');
			code.val('');
			code.focus();
		} else {
			$.ajax({
				type : "post",
				async : false,
				url : "/user/passAccept",
				data : {
					email : email.val(),
					code : code.val()
				},
				dataType : "text", 
				success : function(result) {
					if (result === 'Code Equal') {
						alert("코드가 일치합니다. 비밀번호 변경 페이지로 이동합니다.");
						$("#formSubmit").click();
					} else {
						alert("코드가 일치하지 않습니다. 다시 비밀번호 찾기 요청을 보내주세요.");
						window.location.href="/user/findPass";
					}
				},
				error : function(res) {
					alert("인증 실패 운영자한테 문의해주세요.");					
				}
			});
		}
	}
</script>