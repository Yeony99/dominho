<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/layout2.css"
	type="text/css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/style2.css"
	type="text/css">

<title>회원가입 - 도민호 피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.memberForm {
	margin: 50px auto;
	width: 500px;
}

.box {
	border-bottom: 2px solid #adadad;
	margin: 30px;
	padding: 10px 10px;
}

.inputStyle {
	width: 100%;
	border: none;
	outline: none;
	font-size: 16px;
	height: 25px;
	background: none;
}

.joinbtn {
	width: 100%;
	position: relative;
	left: 50%;
	transform: translateX(-50%);
	margin-bottom: 40px;
	height: 40px;
	background: linear-gradient(125deg, #FCD6B8, #FF8E21, #CA3D2A);
	background-position: left;
	background-size: 200%;
	color: white;
	font-weight: bold;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	transition: 0.4s;
	display: inline;
	cursor: pointer;
	margin: 40px 0;
}

.joinbtn:hover {
	background-position: right;
}

.btn {
	display: block;
	float: left;
	position: relative;
	left: 320px;
	bottom: 28px;
	border: none;
	border-radius: 10px;
	height: 30px;
	width: 100px;
}

.popbtn {
	display: inline;
	position: relative;
	left: 210px;
	border: none;
	border-radius: 10px;
	height: 30px;
	width: 100px;
}

#popup {
	display: flex;
	justify-content: center;
	align-items: center;
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, .7);
	z-index: 1;
}

#popup.hide {
	display: none;
}

#popup.has-filter {
	backdrop-filter: blur(4px);
	-webkit-backdrop-filter: blur(4px);
}

#popup .content {
	padding: 20px;
	background: #fff;
	border-radius: 5px;
	box-shadow: 1px 1px 3px rgba(0, 0, 0, .3);
}
</style>


<script type="text/javascript">
function isValidDateFormat(data){
    var regexp = /[12][0-9]{3}[\.|\-|\/]?[0-9]{2}[\.|\-|\/]?[0-9]{2}/;
    if(! regexp.test(data))
        return false;

    regexp=/(\.)|(\-)|(\/)/g;
    data=data.replace(regexp, "");
    
	var y=parseInt(data.substr(0, 4));
    var m=parseInt(data.substr(4, 2));
    if(m<1||m>12) 
    	return false;
    var d=parseInt(data.substr(6));
    var lastDay = (new Date(y, m, 0)).getDate();
    if(d<1||d>lastDay)
    	return false;
		
	return true;
}
	function memberOk() {
		var f = document.memberForm;
		var str;

		str = f.userId.value;
		str = str.trim();
		if (!str) {
			alert("아이디를 입력하세요. ");
			f.userId.focus();
			return;
		}
		if (!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) {
			alert("아이디는 5~10자이며 첫글자는 영문자이어야 합니다.");
			f.userId.focus();
			return;
		}
		f.userId.value = str;

		str = f.userPwd.value;
		str = str.trim();
		if (!str) {
			alert("패스워드를 입력하세요. ");
			f.userPwd.focus();
			return;
		}
		if (!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,30}$/i.test(str)) {
			alert("패스워드는 5~30자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
			f.userPwd.focus();
			return;
		}
		f.userPwd.value = str;

		if (str != f.userPwdCheck.value) {
			alert("패스워드가 일치하지 않습니다. ");
			f.userPwdCheck.focus();
			return;
		}

		str = f.userName.value;
		str = str.trim();
		if (!str) {
			alert("이름을 입력하세요. ");
			f.userName.focus();
			return;
		}
		f.userName.value = str;
		
		str = f.userEmail.value;
		str = str.trim();
		if (!str) {
			alert("이메일을 입력하세요. ");
			f.userEmail.focus();
			return;
		}

		str = f.birth.value;
		str = str.trim();
		if (!str || !isValidDateFormat(str)) {
			alert("생년월일를 입력하세요. ");
			f.birth.focus();
			return;
		}

		str = f.tel.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel.focus();
			return;
		}

		if (!/^(\d+)$/.test(str)) {
			alert("숫자만 가능합니다. ");
			f.tel.focus();
			return;
		}
		
		if (document.getElementById('tos').checked == false) {
			  alert('이용 약관에 동의해주세요.');
			return;
		}
		
		f.action = "${pageContext.request.contextPath}/member/${mode}_ok.do";
		f.submit();
	}

	function changeEmail() {
		var f = document.memberForm;

		var str = f.selectEmail.value;
		if (str != "direct") {
			f.email2.value = str;
			f.email2.readOnly = true;
			f.email1.focus();
		} else {
			f.email2.value = "";
			f.email2.readOnly = false;
			f.email1.focus();
		}
	}

	function daumPostcode() {
		new daum.Postcode(
				{
					oncomplete : function(data) {
						var fullAddr = '';
						var extraAddr = '';

						if (data.userSelectedType === 'R') { //도로명 주소
							fullAddr = data.roadAddress;

						} else { //지번 주소
							fullAddr = data.jibunAddress;
						}

						if (data.userSelectedType === 'R') {
							//법정동명이 있을 경우 추가한다.
							if (data.bname !== '') {
								extraAddr += data.bname;
							}
							// 건물명이 있을 경우 추가한다.
							if (data.buildingName !== '') {
								extraAddr += (extraAddr !== '' ? ', '
										+ data.buildingName : data.buildingName);
							}
							// 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
							fullAddr += (extraAddr !== '' ? ' (' + extraAddr
									+ ')' : '');
						}
						// 우편번호와 주소 정보를 해당 필드에 넣는다.
						document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
						document.getElementById('addr1').value = fullAddr;
						// 커서를 상세주소 필드로 이동한다.
						document.getElementById('addr2').focus();
					}
				}).open();
	}
	function showPopup(hasFilter) {
		const popup = document.querySelector('#popup');
		popup.classList.add('has-filter');
		popup.classList.remove('hide');
	}

	function closePopup() {
		const popup = document.querySelector('#popup');
		popup.classList.add('hide');
	}
</script>
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<form name="memberForm" method="post" class="memberForm">
		<h2 style="text-align: center;">회원가입</h2>

		<div class="box">
			<h4>아이디 *</h4>
			<br> <input type="text" name="userId" id="userId" class="inputStyle"
				value="${dto.userId}" placeholder="아이디는 5~10자 사이로 입력해주세요.">
		</div>
		<div class="box">
			<h4>패스워드 *</h4>
			<br> <input type="password" name="userPwd" class="inputStyle"
				placeholder="패스워드 (5자 이상 + 영문 혹은 숫자)">
		</div>
		<div class="box">
			<input type="password" name="userPwdCheck" class="inputStyle"
				placeholder="패스워드를 한 번 더 입력해주세요.">
		</div>
		<div class="box">
			<h4>이름 * </h4>
			<br> <input type="text" name="userName" class="inputStyle"
				value="${dto.userName}" maxlength="30" placeholder="이름을 입력해주세요.">
		</div>
		<div class="box">
			<h4>이메일 * </h4>
			<br> <input type="email" name="userEmail" class="inputStyle"
				value="${dto.email}" placeholder="이메일을 입력해주세요.">
		</div>
		<div class="box">
			<h4>생일 * </h4>
			<br> <input type="text" name="birth" class="inputStyle"
				value="${dto.birth}" placeholder="생일을 입력하세요. (2021-01-01)">
		</div>
		<div class="box">
			<h4>휴대폰 번호 * </h4>
			<br> <input type="text" name="tel" class="inputStyle"
				value="${dto.tel}" placeholder="휴대폰 번호를 입력해주세요.">
		</div>
		<div class="box">
			<h4>주소</h4>
			<br> <input type="text" name="zip" id="zip" value="${dto.zip}"
				class="inputStyle" readonly="readonly" placeholder="우편번호">
			<button type="button" class="btn" onclick="daumPostcode();">우편번호</button>
		</div>
		<div class="box">
			<input type="text" name="addr1" id="addr1" value="${dto.address1}"
				maxlength="50" class="inputStyle" placeholder="기본 주소"
				readonly="readonly">
		</div>
		<div class="box">
			<input type="text" name="addr2" id="addr2" value="${dto.address2}"
				maxlength="50" class="inputStyle" placeholder="상세 주소를 입력해주세요.">
		</div>
		<div class="box">
			<h4>이용약관 * </h4>

			<br> <input type="checkbox" name="tos" id="tos" value="agree">
			이용약관 동의
			<button type="button" class="popbtn" onclick="showPopup();">자세히</button>
		</div>
		<div id="popup" class="hide">
				<div class="content">
					<p>제 1 장 총 칙</p>
					<p>제 1 조 (목적)</p>
					<p>이 약관은 도민호피자(이하 "사이트")에서 제공하는 인터넷서비스(이하 "서비스")의 이용 조건 및 절차에 관한 기본적인 사항을 규정함을 목적으로 합니다.</p><br>
					<p>제 2 조 (약관의 효력 및 변경) </p>
					<p>① 이 약관은 서비스 화면이나 기타의 방법으로 이용고객에게 공지함으로써 효력을 발생합니다.<br>
					   ② 사이트는 이 약관의 내용을 변경할 수 있으며, 변경된 약관은 제1항과 같은 방법으로 공지 또는 통지함으로써 효력을 발생합니다.</p>
					<button type="button" class="popbtn" onclick="closePopup();" style="left:400px">닫기</button>
				</div>
			</div>
		
		<button type="button" class="joinbtn" onclick="memberOk();">회원가입</button>
	</form>
	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>
	<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>