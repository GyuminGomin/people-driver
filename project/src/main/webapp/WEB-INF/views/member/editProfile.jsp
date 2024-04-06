<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/register.css"/>

<c:set var="content">
	<input type="hidden" class="postcodify_postcode5"/>
	<input type="hidden" class="postcodify_address"/>
	<img src="${path}/resources/img/logo/logo2.png" id="logo" />
	<form id="editProfile" action="/user/edit" method="POST" enctype="multipart/form-data">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<div id="table">
			<div>
				<h2>회원정보 수정</h2>
			</div>
			
			<div>
				<c:choose>
					<c:when test="${loginMember.imgUrl ne null}">
						<img src="${loginMember.imgUrl}" id="sampleImg"  style="width:100px; height:100px; border-radius: 50%; object-fit: cover;"/>
						<input type="file" name="file" id="profileImage" accept=".gif, .jpg, .jpeg, .png" />		
					</c:when>
					<c:otherwise>
						<img src="${path}/resources/img/profile.jpg" id="sampleImg"  style="width:100px; height:100px; border-radius: 50%; object-fit: cover;"/>
						<input type="file" name="file" id="profileImage" accept=".gif, .jpg, .jpeg, .png" />
					</c:otherwise>
				</c:choose>
				
				
			</div>
			
			<div>
				<input type="text" id="name" value="${loginMember.name}" readonly />
			</div>
			
			<div>
				<input type="email" id="email" name="email" value="${loginMember.email}" readonly />
			</div>
			
			<div>
				<input type="password" id="pass" name="password" required placeholder="변경할 비밀번호 작성"/>
			</div>
			
			<div>
				<input type="password" id="re_pass" required placeholder="비밀번호 확인"/>
			</div>
			
			<div>
				<p>생년월일</p>
				<input type="text" id="birth" value="${loginMember.birth}" readonly/>
			</div>
			
			<div>
				<p>전화번호</p>
				<input type="text" id="phone" name="phoneNo" value="${loginMember.phoneNo}" placeholder="휴대전화 입력" required />
			</div>
			
			<div>
				<p>우편번호</p>
				<div class="bbbb">
					<input type="text" id="postcode1" name="pc5" value="${loginMember.pc5}" readOnly />
	            	<button type="button" id="postcodify_search_button">검색</button>
	            </div>
            </div>
            
            <div>
	            <p>도로명주소</p>
	            <input type="text" id="postcode2" name="address" value="${loginMember.address}" readOnly />
            </div>
            
            <div>
	            <input type="text" id="postcode3" name="detail" value="${loginMember.detail}" placeholder="상세주소 입력" required/>
            </div>
            
            <div>
	            <p>성별</p>
	            <div>
	            	<c:choose>
	            		<c:when test="${loginMember.gender eq 'MALE'}">
	            			<input type="text" id="gender" value="남성" readonly/>
	            		</c:when>
	            		<c:otherwise>
	            			<input type="text" id="gender" value="여성" readonly/>
	            		</c:otherwise>
	            	</c:choose>
	           	</div>
            </div>
            
            <div>
	            <p>비즈니스 형태</p>
	            <c:choose>
	            	<c:when test="${loginMember.role eq 'DEVELOPER'}">
	            		<input type="text" id="role" value="DEVELOPER(개발자)" readonly/>
	            	</c:when>
	            	<c:otherwise>
	            		<input type="text" id="role" value="BUSINESS(사업가)" readonly/>
	            	</c:otherwise>
	            </c:choose>
            </div>
            
            <c:if test="${loginMember.role eq 'BUSINESS'}">
            	<hr />
            	<div>
	            	<p>회사명</p>
	            	<input type="text" id="bname" name="bname" value="${loginMember.bname}" />
	            </div>
	            
	            <div>
	            	<p>회사 우편 번호</p>
	            	<div class="bbbb">
		            	<input type="text" id="postcode4" name="bpc5" value="${loginMember.bpc5}" readonly />
		            	<button type="button" id="postcodify_search_button2">검색</button>
	            	</div>
	            </div>
	            
	            <div>
		            <p>회사 도로명주소</p>
		            <input type="text" id="postcode5" name="baddress" value="${loginMember.baddress}" readOnly />
            	</div>
	            
	            <div>
	            	<p>회사 상세 주소</p>
	            	<input type="text" id="postcode6" name="bdetail" value="${loginMember.bdetail}" required placeholder="상세주소 입력" />
	            </div>
	            
	            <div>
	            	<p>회사 연락 번호</p>
	            	<input type="text" id="phone2" name="bphone" value="${loginMember.bphone}" placeholder="회사 일반 전화 작성(또는 담당자 전화)" required />
	            </div>
            </c:if>
            
            <div>
            	<input style="color: white; box-shadow: 0 3px 0 #CCCCCC; background-color: rgba(3,199,90);" type="button" onclick="edit();" value="회원정보 수정" />
            	<input style="color: white; box-shadow: 0 3px 0 #CCCCCC; background-color: rgba(3,199,90);" type="button" onclick="remove();" value="회원정보 삭제" />
            </div>	
      	</div>
     </form>
     
     <form id="deleteProfile" action="/user/removeUser" method="post">
		<input type="hidden" id="email" name="email" value="${loginMember.email}"/>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
     </form>
     
     
     
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>


<script>
	//Postcodify 주소 검색 설정
	$("#postcodify_search_button").postcodifyPopUp({
		afterSelect: function(data) {
			$("#postcode1").val($(".postcodify_postcode5").val());
			$("#postcode2").val($(".postcodify_address").val());
			$("#postcode3").focus();
			
			// 주소 선택 후 팝업 닫음
			alert("입력이 완료되었습니다. esc를 눌러주세요");
		}
	});
	
	if ($("#postcode4") !== null) {
		$("#postcodify_search_button2").postcodifyPopUp({
			afterSelect: function(data) {
				$("#postcode4").val($(".postcodify_postcode5").val());
				$("#postcode5").val($(".postcodify_address").val());
				$("#postcode6").focus();
				
				// 주소 선택 후 팝업 닫음
				alert("입력이 완료되었습니다. esc를 눌러주세요");
			}
		});	
	}

	// 이미지 업로드
	$("#profileImage").on("change", function() {
		let files = this.files; // 한개만 올린다고 해도 파일은 여러개의 리스트로 가져옴
		
		// 사용자 컴퓨터에서 사용자가 선택한 파일이 저장된 실제 위치 정보를 HTML 문서가 해석할 수 있는 URL 경로로 반환
		if (files.length === 0) { // 파일이 없다면
			$("#sampleImg").attr("src", "resources/img/profile.jpg");
		} else {
			let file = files[0];
			let path = window.URL.createObjectURL(file);
			$("#sampleImg").attr("src", path);	
		}
	});
	
	// 회원정보 수정
	function edit() {
		let pass = $("#pass");
		let repass = $("#re_pass");
		let phone_f = $("#phone");
		let detail = $("#postcode3");
		let role = $("#role");
		if (role.val() === 'BUSINESS(사업가)') {
			var bname = $("#bname");
			var bdetail = $("#postcode6");
			var bphone = $("#phone2");	
		}
		
		let form = $("#editProfile");
		
		var regexPass = /(?=.*\d)(?=.*[a-z]).{8,}/; // 흔한 8자리 이상의 영어소문자, 숫자 포함
		var regexPhone = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/; // 휴대전화
		var regexLocalPhone = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))(\d{3,4})(\d{4})$/; // 일반 전화
		
		if (!regexPass.test(pass.val()) || pass.val().length > 20) {
			alert('비밀번호는 영문자,숫자를 포함한 8자리 이상 20자 이내로 작성해주세요.');
			pass.val('');
			pass.focus();
		}
		else if (pass.val() != repass.val()) {
			alert('비밀번호가 일치하지 않습니다.');
			repass.val('');
			repass.focus();
		}
		
		else if (!regexPhone.test(phone_f.val())) {
			alert('휴대전화 형태가 맞지 않습니다. (-는 빼주세요)');
			phone_f.val('');
			phone_f.focus();
		}
		
		else if (detail.val() === '') {
			alert('거주하고 있는 주소지의 상세주소를 입력해주세요. 실 거주지여야 우편배송이 가능합니다.');
			address.focus();
		}
		
		else if (role.val() === 'DEVELOPER(개발자)') {
			form.submit();
		}
		
		else if (role.val() === 'BUSINESS(사업가)') {
			if (bname.val() === '') {
				alert('회사 명을 입력해주세요.');
				bname.focus();
			} else if (bdetail.val() === '') {
				alert('기업 주소지의 상세주소를 입력해주세요. 실 거주지여야 우편배송이 가능합니다.');
				baddress.focus();
			} else if (!regexPhone.test(bphone.val()) && !regexLocalPhone.test(bphone.val())) {
				alert('휴대전화 또는 일반전화 형태로 작성해주세요. (-는 빼주세요)');
				bphone.val('');
				bphone.focus();
			} else {
				form.submit();
			}
		}
	}
	
	// 회원정보 삭제
	function remove() {
		const flag = confirm("정말 삭제하시겠습니까? 복구 불가능합니다.");
		if (flag) {
			alert("삭제 성공 (메인화면으로 돌아갑니다.)");
			let form = $("#deleteProfile");
			form.submit();
		}
			
	}
</script>