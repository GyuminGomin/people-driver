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
if (msg == 'loginPlease') {
	alert('로그인 부터 해주세요.');
	window.location.href="/user/login";
} else if (msg == 'csrf') {
	alert('잘못된 접근 방식 입니다.');
	window.history.go(-1);
} else if (msg == 'NotExist') {
	alert('아이디 또는 비밀번호가 일치하지 않습니다.');
	window.location.href="/user/login";
} else if (msg == 'business') {
	alert('비즈니스 회원만 접속 가능합니다.');
	window.history.go(-1);
} else if (msg == 'developer') {
	alert('개발자 회원만 접속 가능합니다.');
	window.history.go(-1);
} else if (msg == 'wrong') {
	alert('에러가 어디에서 발생하는가? 정리하기');
	window.history.go(-1);
} else if (msg == 'wrongMember') {
	alert('접근할 수 없는 회원 정보 입니다.');
	window.history.go(-1);
}

else {
	alert('알 수 없는 오류가 발생하였습니다. 관리자에게 문의해 주세요.');
	window.history.go(-1);
}
</script>	
</body>
</html>