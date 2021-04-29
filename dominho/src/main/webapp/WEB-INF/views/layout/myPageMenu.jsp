<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/myPageMenu.css"
	type="text/css">
<style>
.box {
	border-bottom: 2px solid #adadad;
	margin: 30px 0;
	width: 150px;
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
	display: block;
	float: left;
	position: relative;
	left: 170px;
	bottom: 60px;
	border: none;
	border-radius: 10px;
	height: 30px;
	width: 100px;
	border: none;
}

.loginForm {
	margin: 50px auto;
	width: 300px;
	height: 500px;
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
	color: black;
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

.msgBox, .box {
	margin: 30px;
	padding: 10px 10px;
}

.btn {
	position: relative;
	left: 50%;
	top: 5%;
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

.btn:hover {
	background-position: right;
}
</style>
<script type="text/javascript">
	function sendConfirm() {
		var f = document.confirm;

		str = f.userPwd.value;
		if (!str) {
			alert("패스워드를 입력하세요. ");
			f.userPwd.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/mypage/pwd_ok.do"; //여기로 넘김.
		f.submit();
	}
</script>
<div style="margin: 50px auto; width: 1000px;">
	<h2>내 정보</h2>
	<div class="myPageMenu">
		<ul class="myPageNav">
			<li><span class="un"><a
					href="${pageContext.request.contextPath}/mypage/myOrderList.do">주문내역</a></span></li>
			<li><span class="un"><a
					href="${pageContext.request.contextPath}/member/pwd.do?mode=update">정보수정</a></span></li>
			<!-- 			<li><span class="un"><a href="#">1:1 문의</a></span></li>
			<li><span class="un"><a href="#">쿠폰함</a></span></li>-->
		</ul>
	</div>

	<div class="myPageBox">
		<div style="padding: 30px 30px;">
			<div
				style="border-bottom: 2px solid #CA3D2A; display: inline-block; position: relative; font-size: 20px;">
				<span style="font-weight: 600;">${sessionScope.member.userName}님</span>의&nbsp;주문내역입니다.
			</div>
		</div>
	</div>
	<table
		style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
		<tr align="center" height="55">
			<th width="60">주문번호</th>
			<th width="150">주문매장</th>
			<th width="107">배달/포장</th>
			<th>결제금액</th>
			<th width="200">주문일</th>
		</tr>
		<c:forEach var="dto" items="${AllOrders}">
			<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
				<td width="60">${dto.orderNum}</td>
				<td width="150">${dto.storeName}</td>
				<td width="107">${dto.isDelivery}</td>
				<td>${dto.totalPrice}원</td>
				<td width="200">${dto.orderDate}</td>
		</c:forEach>

	</table>
	<c:if test="${mode eq 'update'}">
		<form name="confirm" method="post" class="loginForm" action="">
			<h2>본인여부 확인</h2>
			<div class="idbox">
				<input type="text" name="userId" class="id"
					value="${sessionScope.member.userId}">
			</div>
			<div class="pwdbox">
				<input type="password" name="userPwd" class="pw"
					placeholder="비밀번호를 입력해주세요.">
			</div>
			<button type="button" class="btn" onclick="sendConfirm();">비밀번호
				확인</button>
			<div class="msgBox" style="color: #CA3D2A">${msg}</div>
		</form>
	</c:if>
</div>