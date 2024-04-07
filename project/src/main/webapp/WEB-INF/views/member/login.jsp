<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/login.css"/>

<c:set var="content">
	<section>
	<form method="post" action="/user/login" id="form">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	<div class="login" id="login">
		<h1 class="login_title">로그인</h1>
		<div class="loginForm">
			<input type="text" name="email" id="email" placeholder="이메일 주소" autofocus="autofocus"/>
			<input type="password" name="password" id="pass" placeholder="비밀번호"/>
		</div>
		<div class="loginCheck">
			<input type="checkbox" name="checked" id="loginSession"/>
			<label for="checked">로그인 상태 유지</label>
			
			<a href="/user/findPass" id="test">비밀번호 찾기</a>
		</div>
		<div class="loginButton">
			<button onclick="login()">로그인</button>
			<p>계정이 없으신가요? <a href="/user/register">회원가입하기</a> </p>
		</div>
	</div>
	</form>
	</section>
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>

<script>
	function check() {
		let chkLogin = $("#loginSession");
		console.log(chkLogin.is(":checked"));
	}

	// 로그인
	function login() {
		let email = $("#email");
		let pass = $("#pass");
		let chkLogin = $("#loginSession");
		
		// 정규식
		var regexEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 정규표현식 이메일
		
		if (email.val() === '') {
			alert('이메일을 입력해주세요.');
			email.focus();
		} else if (!regexEmail.test(email.val())) {
			alert('이메일 형식이 맞지 않습니다.');
			email.val('');
			email.focus();
		} else if (pass.val() === '') {
			alert('비밀번호가 입력되지 않았습니다.');
			pass.focus();
		} else {
			$("#form").submit();
		}
	}

</script>