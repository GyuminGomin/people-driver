<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/error/error.css"/>

<c:set var="content">
	<section>
	<div class="error" style="background-image: url('${path}/resources/img/error/error_405.jpg');">
		<h1>정상적인 요청이 아닙니다.</h1>
		<a href="/">메인으로</a>
	</div>
	</section>
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>