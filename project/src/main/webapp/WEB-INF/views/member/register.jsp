<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${path}/resources/css/member/register.css"/>

<c:set var="content">
	<input type="hidden" class="postcodify_postcode5"/>
	<input type="hidden" class="postcodify_address"/>
		<div id="table">
			<div>
				<h2>회원가입</h2>
			</div>
			
			<div>
				<img src="${path}/resources/img/profile.jpg" id="sampleImg"  style="width:100px; height:100px; border-radius: 50%; object-fit: cover;"/>
				<input type="file" name="file" id="profileImage" accept=".gif, .jpg, .jpeg, .png" />
			</div>
			
			<div>
				<input type="text" id="name" autofocus required placeholder="이름 작성" />
			</div>
			
			<div>
				<input type="email" id="email" required placeholder="(아이디)이메일 작성" />
			</div>
			
			<div>
				<input type="password" id="pass" required placeholder="비밀번호 작성"/>
			</div>
			
			<div>
				<input type="password" id="re_pass" required placeholder="비밀번호 확인"/>
			</div>
			
			<div>
				<p>생년월일</p>
				<input type="date" id="birth" required/>
			</div>
			
			<div>
				<p>전화번호</p>
				<input type="text" id="phone" value="010" placeholder="휴대전화 입력" required />
			</div>
			
			<div>
				<p>우편번호</p>
				<div class="bbbb">
					<input type="text" id="postcode1" readOnly />
	            	<button type="button" id="postcodify_search_button">검색</button>
	            </div>
            </div>
            
            <div>
	            <p>도로명주소</p>
	            <input type="text" id="postcode2" readOnly />
            </div>
            
            <div>
	            <input type="text" id="postcode3" placeholder="상세주소 입력" required/>
            </div>
            
            <div>
	            <p>성별</p>
	            <div>
	                <input type="radio" id="gender" name="gender" class="gender" value="MALE" checked/>
	                <label for="gender">남성</label>
	                <input type="radio" id="gender2" name="gender" class="gender" value="FEMALE"/>
		           	<label for="gender2">여성</label>
	           	</div>
            </div>
            
            <div>
	            <p>원하는 비즈니스 형태 선택</p>
	            <div>
	                <input type="radio" id="role1" name="role" class="role" value="DEVELOPER" checked/>
	                <label for="role1">개발자</label>
	                <input type="radio" id="role2" name="role" class="role" value="BUSINESS"/>
	                <label for="role2">사업가</label>
	            </div>
            </div>
            
            <div class="aaaa">
            	<p>회사명</p>
            	<input type="text" id="bname" required placeholder="회사 명 작성" />
            </div>
            
            <div class="aaaa">
            	<p>회사 우편 번호</p>
            	<div class="bbbb">
	            	<input type="text" id="postcode4" readonly />
	            	<button type="button" id="postcodify_search_button2">검색</button>
            	</div>
            </div>
            
            <div class="aaaa">
	            <p>회사 도로명주소</p>
	            <input type="text" id="postcode5" readOnly />
            </div>
            
            <div class="aaaa">
            	<p>회사 상세 주소</p>
            	<input type="text" id="postcode6" required placeholder="상세주소 입력" />
            </div>
            
            
            
            <div class="aaaa">
            	<p>회사 연락 번호</p>
            	<input type="text" id="phone2" placeholder="회사 일반 전화 작성(또는 담당자 전화)" required />
            </div>
            
            <div>
            	<input style="cursor:pointer; color: white; box-shadow: 0 3px 0 #CCCCCC; background: linear-gradient(-90deg, rgba(3, 215, 79, 0.8), rgba(3, 215, 79, 0.8)); " type="button" onclick="javascript:join();" value="회원가입" />
            </div>
			
			<div id="map" style="width:500px;height:400px;"></div>
		</div>
		
</c:set>

<%@ include file="/WEB-INF/views/common/frame.jsp" %>

<script>

// kakaoMapAPI
var container = $("#map")[0];
var options= {
		center : new kakao.maps.LatLng(35.159056, 129.116904), // 지도 중심 좌표 (부산 광안리)
		level : 3 // 지도 레벨 (확대, 축소 정도)
};
var map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

function slowScrollTo(element, to, duration) {
	if (duration <= 0) return;
	let difference = to - (element === window ? window.scrollY : element.scrollTop);
	let perTick = difference / duration * 10;
	
	setTimeout(function() {
		if (element === window) {
			window.scrollTo(0,window.scrollY + perTick);
		} else {
			element.scrollTop += perTick;	
		}
		
		if ((element === window ? window.scrollY : element.scrollTop) === to) return;
		slowScrollTo(element, to, duration - 10);
	}, 10);
}

// 사업가, 개발자 라디오 선택
$(".role").click(function(){
	if($(".role:checked").val() === 'BUSINESS'){
		$(".aaaa").slideDown();
		// 윈도우 창도 함께 아래로 내력기
		let scrollHeight = document.body.scrollHeight;
		slowScrollTo(window, scrollHeight, 100);
	} else {
		// 입력된 값 초기화
		$(".aaaa input").val('');
		// 윈도우 창도 함꼐 위로 올라가기
		slowScrollTo(window, 100, 500);
		// 다시 숨기기
		$(".aaaa").hide();
	}
});

// Postcodify 주소 검색 설정
$("#postcodify_search_button").postcodifyPopUp({
	afterSelect: function(data) {
		$("#postcode1").val($(".postcodify_postcode5").val());
		$("#postcode2").val($(".postcodify_address").val());
		$("#postcode3").focus();
		
		// 주소 선택 후 팝업 닫음
		alert("입력이 완료되었습니다. esc를 눌러주세요");
	}
});

$("#postcodify_search_button2").postcodifyPopUp({
	afterSelect: function(data) {
		$("#postcode4").val($(".postcodify_postcode5").val());
		$("#postcode5").val($(".postcodify_address").val());
		$("#postcode6").focus();
		
		// 주소 선택 후 팝업 닫음
		alert("입력이 완료되었습니다. esc를 눌러주세요");
	}
});

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

/* 회원가입 요청 처리 */
function join(){
	let u_name = $("#name"); // user 이름
	let u_id = $("#email"); // user 아이디
	let u_pass = $("#pass"); // user 비번
	let u_re_pass = $("#re_pass");
	let u_birth = $("#birth"); // user 생년월일
	let u_phone_f = $("#phone"); // 휴대전화(일반회원용)
	let postcode5 = $("#postcode1"); // 일반회원 우편번호 (도로명주소도 자동으로 생성됨)
	let address = $("#postcode2"); // 도로명 주소
	let detail = $("#postcode3"); // 일반회원 상세주소
	let role = $(".role:checked"); // 개발자,비즈니스
	let gender = $(".gender:checked"); // 성별
	let bname = $("#bname"); // 회사 명 // 처음 값을 초기화 할 때, 없을 수 도 있다. (참고) -- 여기서부터
	let b_postcode5 = $("#postcode4"); // 기업 우편번호
	let baddress = $("#postcode5"); // 도로명 주소
	let bdetail = $("#postcode6"); // 기업 상세주소
	let bphone = $("#phone2"); // 일반전화(기업용)
	
	// 정규식
	var regexEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 정규표현식 이메일
	var regexPass = /(?=.*\d)(?=.*[a-z]).{8,}/; // 흔한 8자리 이상의 영어소문자, 숫자 포함
	var regexPhone = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/; // 휴대전화
	var regexLocalPhone = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))(\d{3,4})(\d{4})$/; // 일반 전화
	
	
	// 이름 필드
	if(u_name.val() === '') {
		alert('이름을 입력해주세요.');
		u_name.focus();
	} else if (u_name.val().length <2 || u_name.val().length > 20) {
		alert('이름은 2자 이상 20자 이내입니다.');
		u_name.val('');
		u_name.focus();
	}
	
	// 이메일 필드
	else if(!regexEmail.test(u_id.val())){
		alert('이메일 형식이 맞지 않습니다.');
		u_id.val('');
		u_id.focus();
	}
	
	// 패스워드 필드
	else if(!regexPass.test(u_pass.val()) || u_pass.val().length > 20) {
		alert('비밀번호는 영문자,숫자를 포함한 8자리 이상 20자 이내로 작성해주세요.');
		u_pass.val('');
		u_pass.focus();
	}
	
	// 패스워드 일치 필드
	else if(u_pass.val() != u_re_pass.val()){
		alert('비밀번호가 일치하지 않습니다.');
		u_re_pass.val('');
		u_re_pass.focus();
	}
	
	// 생년월일 필드
	else if(u_birth.val() === ''){
		alert('생년월일을 입력해주세요.');
		u_birth.focus();
		// 생년월일 조건 추가
	}
	
	// 전화번호 필드
	else if(!regexPhone.test(u_phone_f.val())) {
		alert('휴대전화 형태가 맞지 않습니다. (-는 빼주세요)');
		u_phone_f.val('');
		u_phone_f.focus();
	}
	
	else if(postcode5.val() === '') {
		alert('거주하고 있는 주소지를 입력해주세요.');
		$("#postcodify_search_button").click();
	}
	
	else if(detail.val() === '') {
		alert('거주하고 있는 주소지의 상세주소를 입력해주세요. 실 거주지여야 우편배송이 가능합니다.');
		address.focus();
	}
	
	// 이제 비즈니스 형태 타입 확인 후 조건 2개로 나누기
	else if(role.val() === 'DEVELOPER') {
		// ajax로 post 요청 보내기;
		let formData = new FormData(); // 가상의 폼 태그를 의미
		let files = $("#profileImage")[0].files; // 한개만 올린다고 해도 파일은 여러개의 리스트로 가져옴 
		//if (files.length !== 0) { // 이미지 파일이 있다면
		let file = files[0];
		formData.append("file", file); // 이미지 파일 등록	
		//}
		let Tdata = { // 이미지 파일을 뺀 나머지 txt 데이터
				name : u_name.val(),
				email : u_id.val(),
				password : u_pass.val(),
				birth : u_birth.val(),
				phoneNo : u_phone_f.val(),
				pc5 : postcode5.val(),
				address : address.val(),
				detail : detail.val(),
				role : role.val(),
				gender : gender.val(),
				${_csrf.parameterName} : '${_csrf.token}'
		};
		// 나머지 데이터도 FormData에 추가
		Object.keys(Tdata).forEach(key => {
			formData.append(key, Tdata[key]);
		});
		$.ajax ({
			type : "POST",
			url : "/user/register",
			data : formData,
			contentType : false,
			processData : false,
			dataType : "text",
			success : function(result) {
				if (result === "ID Duplicate Retry!") {
					alert("아이디 중복입니다.");
					u_id.val('');
					u_id.focus();
				} else {
					// 로그인 화면으로 이동
					alert("회원가입 성공");
					window.location.href= "/user/login";	
				}
			},
			error : function(res) {
				alert(res.responseText);
			}
		});
		return false;
	}
	
	else if(role.val() === 'BUSINESS') { // click 되었을 때 이벤트 다시 생각해야함
		if (bname.val() === '') {
			alert('회사 명을 입력해주세요.');
			bname.focus();
		} else if(b_postcode5.val() === '') {
			alert('기업의 주소를 입력해주세요.');
			$("#postcodify_search_button2").click();
		} else if(bdetail.val() === '') {
			alert('기업 주소지의 상세주소를 입력해주세요. 실 거주지여야 우편배송이 가능합니다.');
			baddress.focus();
		} else if(!regexPhone.test(bphone.val()) && !regexLocalPhone.test(bphone.val()) ) {
			alert('휴대전화 또는 일반전화 형태로 작성해주세요. (-는 빼주세요)');
			bphone.val('');
			bphone.focus();
		} else {
			// ajax로 post 요청 보내기;
			let formData = new FormData(); // 가상의 폼 태그를 의미
			let files = $("#profileImage")[0].files; // 한개만 올린다고 해도 파일은 여러개의 리스트로 가져옴 
			let file = files[0];
			formData.append("file", file); // 이미지 파일 등록	
			let Tdata = { // 이미지 파일을 뺀 나머지 txt 데이터
				name : u_name.val(),
				email : u_id.val(),
				password : u_pass.val(),
				birth : u_birth.val(),
				phoneNo : u_phone_f.val(),
				pc5 : postcode5.val(),
				address : address.val(),
				detail : detail.val(),
				role : role.val(),
				gender : gender.val(),
				bname : bname.val(),
				bpc5 : b_postcode5.val(),
				baddress : baddress.val(),
				bdetail : bdetail.val(),
				bphone : bphone.val(),
				${_csrf.parameterName} : '${_csrf.token}'
			};
			// 나머지 데이터도 FormData에 추가
			Object.keys(Tdata).forEach(key => {
				formData.append(key, Tdata[key]);
			});
			
			$.ajax ({
				type : "POST",
				url : "/user/register",
				data : formData,
				contentType : false,
				processData : false,
				dataType : "text",
				success : function(result) {
					if (result === "ID Duplicate Retry!") {
						alert("아이디 중복입니다.");
						u_id.val('');
						u_id.focus();
					} else {
						// 로그인 화면으로 이동 redirect
						alert("회원가입 성공");
						window.location.href= "/user/login";
					}
				},
				error : function(res) {}
			});
		}
	}
}
</script>