<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="path" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE html>
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<!-- Postcodify (주소) -->
<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
<!-- 카카오맵 API -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=8bfd085ed59957246b59c9466c5d09d7"></script>
<!-- TinyMCE API -->
<script src="https://cdn.tiny.cloud/1/s2737ar5ojhnqhnsqg91v5run6wasakbr0so33pf18023yxi/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
<meta charset="UTF-8">
<title>Web Site</title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/common/header.css"/>

</head>
<body>
	<header>
		<!-- 헤더 내용 추가 -->
		<div class="first">
			<div class="logo1">
				<!-- 왼쪽 상단에 로고 추가 -->
				<img src="${path}/resources/img/logo/logo.png" style="width:80px; height:80px; border-radius: 50%; object-fit: cover;" onclick="location.href='/'" />
				<span class="home-tooltip">홈으로</span>
			</div>
			<div class="nav1">
			<nav>
				<ul>
					<li>&nbsp;|&nbsp;<a href="/search">프로젝트 찾기</a></li>
					<li><a href="/searchD">개발자 찾기</a>&nbsp;|</li>
				</ul>
			</nav>
			</div>
		</div>
		<div class="logo2">
			<img src="${path}/resources/img/logo/logo2.png" style="width:300px; height:50px;" onclick="location.href='/'" />
			<span class="home-tooltip">홈으로</span>
		</div>
		<div class="nav2">
		<nav>
			<ul>
				<c:choose>
					<c:when test="${empty sessionScope.loginMember}">
					<!-- 로그인 되지 않은 사용자 -->
						<li><a href="/user/login">로그인</a></li>
						<li><a href="/user/register">회원가입</a></li>
					</c:when>
					<c:otherwise>
					<!-- 로그인 된 사용자 -->
						<li><span><b>${loginMember.name}</b></span> <img src="${path}/resources/img/home/alerm.png" style="width:40px; height:40px; margin-left:10px;" /></li>
						<img onclick="modalShow($(this))" class="profileImage" src="${sessionScope.loginMember.imgUrl}" style="width:50px; height:50px; border-radius: 50%; object-fit: cover;" />
						<div class="modal">
					        <h2>프로필</h2>
					        <div class="modal_body">
					            <div>
					                <img class="profileImage profileImage2" style="margin-left:1px; width: 100px; height: 100px; border-radius: 50%; object-fit: cover;" src="${sessionScope.loginMember.imgUrl}" />
					            </div>
					            <hr/>
					            <div class="modal_info">
					                <input style="width:190px;" value="이름 : ${sessionScope.loginMember.name}" readonly >
					                <input style="width:190px;" value="아이디 : ${sessionScope.loginMember.email}" readonly >
					                <input style="width:190px;" value="역할 : ${sessionScope.loginMember.role}" readonly >
					                <input style="width:190px;" value="성별 : ${sessionScope.loginMember.gender}" readonly >
					                <input style="width:190px;" value="전화번호 : ${sessionScope.loginMember.phoneNo}" readonly >
					            </div>
					        </div>
					        <div class="modal_footer">
					            <a href="/user/editProfile">회원정보 수정</a>
					            <c:if test="${loginMember.role == 'DEVELOPER'}">
					            	<a href="/developer/profile?id=${loginMember.id}">프로필 등록</a>
					            </c:if>
					            <c:if test="${loginMember.role == 'BUSINESS'}">
					            	<a href="/business/register">프로젝트 등록</a>
					            </c:if>
					            <span id="logout" onclick="logout()">로그아웃</span>
					        </div>
					    </div>
					</c:otherwise>
				</c:choose>
			</ul>
		</nav>
		</div>
	</header>
<script>
const msg = '${message}';
if (msg != '') {
	alert(msg);
}

	var image = $(".profileImage");
	var modal = $(".modal");
	var flag = false; // 닫혀있는 상태
	
	if (!image.attr("src")) {
		image.attr("src", "${path}/resources/img/profile.jpg");
	}
	
	$(window).resize(function() {
		if (flag === true) {
			let position = image.offset();
			modal.css({
				"top": position.top + 14 + image.outerHeight(),
				"left": position.left - 301 + image.outerWidth()
			});
		}
	});
	
	function modalShow(e) {
		// 현재 이벤트가 발생한 객체 위치
		let position = e.offset();
		if (flag === false) {
			modal.css({
				"display": "flex",
				"top": position.top + 14 + e.outerHeight(),
				"left": position.left - 301 + e.outerWidth()
			});	
			flag = true;
		} else {
			modal.css({
				"display": "none"
			});
			flag = false;
		}
	}

	function logout() {
		alert("로그아웃 완료");
		window.location.href="/user/logout";
	}
</script>