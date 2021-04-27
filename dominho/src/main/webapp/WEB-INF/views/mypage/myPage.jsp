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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/myPageMenu.css"
	type="text/css">
<title>마이페이지 - 도민호 피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.memberForm {
	margin: 50px auto;
	width: 400px;
}

.box {
	border-bottom: 2px solid #adadad;
}

.msgBox, .box {
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

.btn {
	position: relative;
	left: 50%;
	transform: translateX(-50%);
	margin-bottom: 40px;
	width: 80%;
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
}

.delbtn {
	position: relative;
	left: 50%;
	transform: translateX(-50%);
	margin-bottom: 40px;
	width: 320px;
	height: 40px;
	background: linear-gradient(125deg, #FCD6B8, #084B27, #0E191A);
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
}

.btn:hover, .delbtn:hover {
	background-position: right;
}

.postbtn {
	position: relative;
	left: 220px;
	bottom: 28px;
	border: none;
	border-radius: 10px;
	height: 30px;
	width: 100px;
}
</style>
<script type="text/javascript">
	function isValidDateFormat(data) {
		var regexp = /[12][0-9]{3}[\.|\-|\/]?[0-9]{2}[\.|\-|\/]?[0-9]{2}/;
		if (!regexp.test(data))
			return false;

		regexp = /(\.)|(\-)|(\/)/g;
		data = data.replace(regexp, "");

		var y = parseInt(data.substr(0, 4));
		var m = parseInt(data.substr(4, 2));
		if (m<1||m>12)
			return false;
		var d = parseInt(data.substr(6));
		var lastDay = (new Date(y, m, 0)).getDate();
		if (d<1||d>lastDay)
			return false;

		return true;
	}

	function sendUpdate() {
		var f = document.memberForm;
		var str;

		str = f.userId.value;
		str = str.trim();

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

		str = f.email.value;
		str = str.trim();
		if (!str) {
			alert("이메일을 입력하세요. ");
			f.email.focus();
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

		f.action = "${pageContext.request.contextPath}/member/update_ok.do";
		f.submit();
		alert("수정되었습니다.");
	}

	function changeEmail() {
		var f = document.memberForm;

		var str = f.selectEmail.value;
		if (str != "direct") {
			f.email.value = str;
			f.email.focus();
		}
	}

	function sendQuit() {
		var f = document.delForm;
		var conf = confirm("탈퇴하시겠습니까?");

		if (conf) {
			f.action = "${pageContext.request.contextPath}/member/delete_ok.do"
			f.submit();
			alert("탈퇴되었습니다.");
		} else {
			alert("취소되었습니다.");
		}
	}
</script>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<div style="margin: 50px auto; width: 1000px">
		<h2>내 정보</h2>
		<div class="myPageMenu">
			<ul class="myPageNav">
				<li><span class="un"><a
						href="${pageContext.request.contextPath}/mypage/myOrderList.do">주문내역</a></span></li>
				<li><span class="un"><a
						href="${pageContext.request.contextPath}/member/pwd.do?mode=update">정보수정</a></span></li>
			</ul>
		</div>
		<form name="memberForm" method="post" class="memberForm">

			<div class="box">
				<h4>아이디 *</h4>
				<br> <input type="text" name="userId" id="userId"
					class="inputStyle" value="${sessionScope.member.userId}"
					readonly="readonly" disabled="disabled">
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
				<h4>이름 *</h4>
				<br> <input type="text" name="userName" class="inputStyle"
					value="${dto.userName}" maxlength="30">
			</div>
			<div class="box">
				<h4>이메일 *</h4>
				<br> <input type="email" name="email" class="inputStyle"
					value="${dto.email}" placeholder="이메일을 입력해주세요.">
			</div>
			<div class="box">
				<h4>생일 *</h4>
				<br> <input type="text" name="birth" class="inputStyle"
					value="${dto.birth}" readonly="readonly" disabled="disabled">
			</div>
			<div class="box">
				<h4>휴대폰 번호 *</h4>
				<br> <input type="text" name="tel" class="inputStyle"
					value="${dto.tel}" placeholder="휴대폰 번호를 입력해주세요.">
			</div>
			<div class="box">
				<h4>주소</h4>
				<br> <input type="text" name="zip" id="zip" value="${dto.zip}"
					class="inputStyle" readonly="readonly" placeholder="우편번호">
				<button type="button" class="postbtn" onclick="daumPostcode();">우편번호</button>
			</div>
			<div class="box">
				<input type="text" name="address1" id="addr1" value="${dto.addr1}"
					maxlength="50" class="inputStyle" placeholder="기본 주소"
					readonly="readonly">
			</div>
			<div class="box">
				<input type="text" name="address2" id="addr2" value="${dto.addr2}"
					maxlength="50" class="inputStyle" placeholder="상세 주소를 입력해주세요.">
			</div>
			<button type="button" class="btn" onclick="sendUpdate();">정보수정</button>
			<div class="box" style="border: 0px solid gray; text-align: center;">
				<h5>더이상 서비스를 이용하지 않으시려면</h5>
			</div>
		</form>
		<form name="delForm" method="post">
			<input type="hidden" name="userId" id="userId" class="inputStyle"
				value="${sessionScope.member.userId}" readonly="readonly"
				disabled="disabled">
			<button type="button" class="delbtn" onclick="sendQuit()">탈퇴</button>
		</form>
	</div>
	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
	<script type="text/javascript">
		function daumPostcode() {
			new daum.Postcode({
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
						fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')'
								: '');
					}
					// 우편번호와 주소 정보를 해당 필드에 넣는다.
					document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
					document.getElementById('addr1').value = fullAddr;
					// 커서를 상세주소 필드로 이동한다.
					document.getElementById('addr2').focus();
				}
			}).open();
		}
	</script>
	<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
</body>
</html>