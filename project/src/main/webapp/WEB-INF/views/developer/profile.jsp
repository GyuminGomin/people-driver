<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/developer/profile.css"/>

<c:set var="content">
	<div class="container">
	
		<div class="scrollbar">
		<div class="sidebar">
			<ul class="tab-list">
				<li><span onclick="showFirstRow()">체결된 프로젝트 보기</span></li>
				<li><span onclick="showSecondRow()">프로필 보기</span></li>
				<li><span onclick="showThirdRow()">참고할만한 다른 사람 프로필 보기</span></li>
			</ul>
		</div>
		</div>
		
		<div class="content">
			<div class="firstRow">
				체결된 프로젝트 목록
			</div>
			<div class="secondRow">
				<div>
					<p>등록된 프로필 목록</p>
					<button onclick="location.href='/developer/register'">등록</button>
				</div>
				<table border="1">
					<tr>
						<th>증명사진</th>
						<th>제목</th>
						<th>성향</th>
						<th>이름</th>
						<th>업데이트 날짜</th>
						<th>조회수</th>
					</tr>
					<c:choose>
						<c:when test="${!empty developerList}">
							<c:forEach var="developer" items="${developerList}">
							<tr>
								<c:choose>
								<c:when test="${!empty developer.imgUrl}">
									<td><img src="${developer.imgUrl}" style="width:100px; height:100px; object-fit:cover;" /></td>
								</c:when>
								<c:otherwise>
									<td><img src="${path}/resources/img/profile.jpg" style="width:100px; height:100px; object-fit:cover;" /></td>
								</c:otherwise>
								</c:choose>
								<td><a href="/developer/readPage?dno=${developer.dno}">${developer.title}</a></td>
								<td>${developer.tendency}</td>
								<td>${loginMember.name}</td>
								<td>${developer.updateDate}</td>
								<td>${developer.viewCount}</td>
							</tr>
							</c:forEach>
							<c:if test="${!empty pm1 and pm1.maxPage > 0}">
								<tr>
									<th colspan="6">
										<c:if test="${pm1.first}"> <!-- 제일 처음으로 갈 수 있는가? -->
											<a href="/developer/profile?id=${loginMember.id}&page1=1">[&laquo;]</a>
										</c:if>
										<c:if test="${pm1.prev}"> <!-- 이전으로 갈 수 있는가? -->
											<a href="/developer/profile?id=${loginMember.id}&page1=${pm1.startPage-1}">[&lt;]</a>
										</c:if>
										<c:forEach var="i" begin="${pm1.startPage}" end ="${pm1.endPage}">
											<a href="/developer/profile?id=${loginMember.id}&page1=${i}">[${i}]</a>
										</c:forEach>
										<c:if test="${pm1.next}">
											<a href="/developer/profile?id=${loginMember.id}&page1=${pm1.endPage+1}">[&gt;]</a>
										</c:if>
										<c:if test="${pm1.last}">
											<a href="/developer/profile?id=${loginMember.id}&page1=${pm1.maxPage}">[&raquo;]</a>
										</c:if>
									</th>
								</tr>
							</c:if>
						</c:when>
						<c:otherwise>
						<tr>
							<th colspan="6">등록된 프로필이 없습니다.</th>
						</tr>
						</c:otherwise>
					</c:choose>
				</table>
				
			</div>
			
			<div class="thirdRow">
				<div>
					<p>참고할만한 다른 사람 프로필 보기</p>
				</div>
				<table border="1">
					<tr>
						<th>증명사진</th>
						<th>제목</th>
						<th>성향</th>
						<th>이름</th>
						<th>업데이트 날짜</th>
						<th>조회수</th>
					</tr>
					<c:choose>
						<c:when test="${!empty developerAllList}">
							<c:forEach var="developer" items="${developerAllList}">
							<tr>
								<c:choose>
								<c:when test="${!empty developer.imgUrl}">
									<td><img src="${developer.imgUrl}" style="width:100px; height:100px; object-fit:cover;" /></td>
								</c:when>
								<c:otherwise>
									<td><img src="${path}/resources/img/profile.jpg" style="width:100px; height:100px; object-fit:cover;" /></td>
								</c:otherwise>
								</c:choose>
								<td><a href="/developer/readOtherPage?dno=${developer.dno}">${developer.title}</a></td>
								<td>${developer.tendency}</td>
								<td>${developer.member.name}</td>
								<td>${developer.updateDate}</td>
								<td>${developer.viewCount}</td>
							</tr>
							</c:forEach>
							<c:if test="${!empty pm2 and pm2.maxPage > 0}">
								<tr>
									<th colspan="6">
										<c:if test="${pm2.first}"> <!-- 제일 처음으로 갈 수 있는가? -->
											<a href="/developer/profile?id=${loginMember.id}&page=1">[&laquo;]</a>
										</c:if>
										<c:if test="${pm2.prev}"> <!-- 이전으로 갈 수 있는가? -->
											<a href="/developer/profile?id=${loginMember.id}&page=${pm2.startPage-1}">[&lt;]</a>
										</c:if>
										<c:forEach var="i" begin="${pm2.startPage}" end ="${pm2.endPage}">
											<a href="/developer/profile?id=${loginMember.id}&page=${i}">[${i}]</a>
										</c:forEach>
										<c:if test="${pm2.next}">
											<a href="/developer/profile?id=${loginMember.id}&page=${pm2.endPage+1}">[&gt;]</a>
										</c:if>
										<c:if test="${pm2.last}">
											<a href="/developer/profile?id=${loginMember.id}&page=${pm2.maxPage}">[&raquo;]</a>
										</c:if>
									</th>
								</tr>
							</c:if>
						</c:when>
						<c:otherwise>
						<tr>
							<th colspan="6">등록된 프로필이 없습니다.</th>
						</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
		</div>
		
		
	</div>
	
	
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>

<script>

	$(document).ready(function() {
		if (${!empty param.page}) {
			$('.firstRow').css({
	        	display: 'none'
	        });
	        $('.secondRow').css({
	        	display: 'none'
	        });
	        $('.thirdRow').css({
	        	display: 'block'
	        });
		} else {
			$('.firstRow').css({
	        	display: 'none'
	        });
	        $('.secondRow').css({
	        	display: 'block'
	        });
	        $('.thirdRow').css({
	        	display: 'none'
	        });
		}
	});

	/* $(document).ready(function() {
		$('.firstRow').css({
        	display: 'none'
        });
        $('.secondRow').css({
        	display: 'block'
        });
        $('.thirdRow').css({
        	display: 'none'
        });
	});
 */
	// 탭 버튼 구현
    function showFirstRow() {
        $('.firstRow').css({
        	display: 'block'
        });
        $('.secondRow').css({
        	display: 'none'
        });
        $('.thirdRow').css({
        	display: 'none'
        });
    }

    function showSecondRow() {
    	$('.firstRow').css({
        	display: 'none'
        });
        $('.secondRow').css({
        	display: 'block'
        });
        $('.thirdRow').css({
        	display: 'none'
        });
    }

    function showThirdRow() {
    	$('.firstRow').css({
        	display: 'none'
        });
        $('.secondRow').css({
        	display: 'none'
        });
        $('.thirdRow').css({
        	display: 'block'
        });
    }
    
    $(document).ready(function() {
        var sidebar = $('.sidebar');

        $(window).scroll(function() {
            var scrollTop = $(window).scrollTop();
            if (scrollTop > 0) {
                sidebar.css({
                    marginTop: scrollTop
                });
            } else {}
        });
    });
</script>