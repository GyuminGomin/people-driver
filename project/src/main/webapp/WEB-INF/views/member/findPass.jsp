<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.form {
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
	.form h1 {
	text-align: center;
}

	.form .findPassForm input {
		width : 80%;
		height : 30px;
		margin-bottom: 10px;
	}
	
</style>
<c:set var="content">
	<section>
		<div class="form">
			<h1>비밀번호 찾기</h1>
			<p>회원가입 시 등록 한 아이디(email)와 이름을 입력해주세요.</p>
			<div class="findPassForm">
				<form action="/passAuth" method="post">
					<input type="email" name="email" id="email" placeholder="이메일 주소" required />
					<input type="submit" id="formSubmit" style="display:none;"/>
				</form>
				<input type="text" name="name" id="name" placeholder="이름" required />
			</div>
			<div>
				<button onclick="submit()">확인</button>
			</div>
		</div>
	</section>
</c:set>

<script>
	function submit() {
		let email = $("#email");
		let name = $("#name");
		
		// 정규식
		var regexEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 정규표현식 이메일
		
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
				async : false,
				url : "/user/findPass",
				data : {
					email : email.val(),
					name : name.val()
				},
				dataType : "text",
				success : function(result) {
					if (result === '일치하는 회원 정보가 없습니다.') {
						alert(result);
						email.val('');
						name.val('');
						email.focus();
					} else {
						// 메일 발송 성공
						console.log(result);
						alert(result);
						$("#formSubmit").click();
					}
				},
				error : function(res) {
					alert(res.responseText);
				}
			});
		}
		
		
	}	
</script>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>