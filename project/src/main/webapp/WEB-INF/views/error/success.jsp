<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>error</title>
</head>
<body>

	
<script>
const msg = '${message}';
if (msg == 'loginSuccess') {
	// 서버에서 response로 헤더에 담아서 jwtToken을 보낸 값을 받아오기
	let jwtToken = '${header.jwtToken}';
	// localStorage에 jwtToken 저장
	localStorage.setItem('jwtToken', jwtToken);
	
	alert('로그인 성공');
	window.location.href="/";
} else {
	alert('잘못된 접근 방식 입니다.');
	window.location.href="/";
}
</script>
</body>
</html>