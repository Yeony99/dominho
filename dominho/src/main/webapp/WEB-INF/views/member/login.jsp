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

<title>로그인 - 도민호 피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.loginForm {
	margin: 50px auto; width : 300px;
	height: 400px;
	padding: 30px, 20px;
	background-color: #FFFFFF;
	text-align: center;
	top: 50%;
	border-radius: 15px;
	width: 300px;
}

.loginForm h2 {
	text-align: center;
	margin: 30px;
}

.idbox {
	border-bottom: 2px solid #adadad;
	margin: 30px;
	padding: 10px 10px;
}

.pwdbox {
	border-bottom: 2px solid #adadad;
	margin: 30px;
	padding: 10px 10px;
}

.id {
	width: 100%;
	border: none;
	outline: none;
	color: #636e72;
	font-size: 16px;
	height: 25px;
	background: none;
}

.pw {
	width: 100%;
	border: none;
	outline: none;
	color: #636e72;
	font-size: 16px;
	height: 25px;
	background: none;
}

.btn {
	position: relative;
	left: 40%;
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
	border-radius: 20px; cursor : pointer;
	transition: 0.4s;
	display: inline;
	cursor: pointer;
}

.btn:hover {
	background-position: right;
}

.bottomText {
	text-align: center;
	font-size: 14px;
}

.bottomText>a:visited {
	color: black;
}
</style>
<script type="text/javascript">
function sendLogin() {
    var f = document.loginForm;

	var str = f.userId.value;
    if(!str) {
        alert("아이디를 입력하세요. ");
        f.userId.focus();
        return;
    }

    str = f.userPwd.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.userPwd.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/member/login_ok.do"; //여기로 넘김.
    f.submit();
}

</script>
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>


	<div>
		<form name="loginForm" method="post" class="loginForm" action="">
			<h2>로그인</h2>
			<div class="idbox">
				<input type="text" name="userId" class="id" placeholder="아이디">
			</div>
			<div class="pwdbox">
				<input type="password" name="userPwd" class="pw" placeholder="패스워드">
			</div>
			<button type="button" class="btn" onclick="sendLogin();">로그인</button>
			<div class="bottomText">
				회원이 아니신가요? <a href="${pageContext.request.contextPath}/">회원가입</a><br> 
				<a href="${pageContext.request.contextPath}/">비밀번호 찾기</a>&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/">아이디 찾기</a>
			</div>

		</form>
	</div>


	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>

</body>
</html>