<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.login {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	text-align: center;

	border: 1px solid #ccc;
	border-radius: 1%;
	
	width : 80%;
	height: 90%;
}

.login h1 {
	text-align: center;
}

.login .loginForm input {
	width : 80%;
	height : 30px;
	margin-bottom: 10px;
}

</style>

<c:set var="content">
	<section>
	<div class="login" id="login">
		<h1 class="login_title">로그인</h1>
		<div class="loginForm">
			<input type="text" id="email" placeholder="이메일 주소"/>
			<input type="password" id="pass" placeholder="비밀번호"/>
		</div>
		<div>
			<input type="checkbox" name="loginSession" id="loginSession"/>
			<label for="loginSession">로그인 상태 유지</label>
			
			<button onclick="location.href='/user/findPass'">비밀번호 찾기</button>
		</div>
		<div>
			<button onclick="login()">로그인</button>
			<p>계정이 없으신가요? <a href="/user/register">회원가입하기</a> </p>
		</div>
		
		<div>
			<button onclick="check()">콘솔 확인</button>
		</div>
	</div>
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
			$.ajax({
				type : "POST",
				url : "/user/login",
				data :{
					email : $("#email").val(),
					password :  $("#pass").val(),
					checked : $("#loginSession").is(":checked")
				},
				dataType : "text",
				success : function(result) {
					if (result === '이메일 또는 비밀번호가 일치하지 않습니다.') {
						alert(result);
						$("#email").val('');
						$("#pass").val('');
						email.focus();
					} else {
						var data = JSON.parse(result); // result를 객체로 파싱
						alert(data.name + " 회원 로그인 성공");
						// 로그인 완료 시 홈으로 이동
						window.location.href="/";
					}
				},
				error : function(res) {
					alert(res.responseText);
				}
			});
		}
	}

</script>