<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/changePass.css"/>

<c:set var="content">
	<section>
		<div class="form">
			<input type="hidden" id="email" value="${email}">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<h1>비밀번호 변경</h1>
			<p>새롭게 사용하실 비밀번호를 입력해 주세요.</p>
			<div class="changePassForm">
				<input type="password" name="password" id="password" placeholder="새 비밀번호" required />
			</div>
			<div class="changePassBtn">
				<button onclick="submit()">변경하러 가기</button>
			</div>
		</div>
	</section>
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>

<script>
function submit() {
	let email = $("#email");
	let pass = $("#password");
	let csrf_token = $("#csrf_token");
	
	// 정규식
	var regexPass = /(?=.*\d)(?=.*[a-z]).{8,}/; // 흔한 8자리 이상의 영어소문자, 숫자 포함
	
	if (pass.val() === '') {
		alert("비밀번호를 입력해주세요.");
		pass.focus();
	} else if (!regexPass.test(pass.val())) {
		alert("비밀번호는 영문자,숫자를 포함한 8자리 이상 20자 이내로 작성해주세요.");
		pass.val('');
		pass.focus();
	} else {
		$.ajax({
			type : "post",
			url : "/user/change",
			data : {
				email : email.val(),
				password : pass.val(),
				${_csrf.parameterName} : '${_csrf.token}'
			},
			dataType : "text",
			success : function(result) {
				alert("비밀번호 변경 성공");
				window.location.href="/user/login";
			},
			error : function(res) {
				alert("비밀번호 변경 실패 운영자한테 문의해 주세요.");
			}
		});
	}
}
</script>