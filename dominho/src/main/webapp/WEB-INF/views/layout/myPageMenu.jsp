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
	bottom:60px;
	border: none;
	border-radius: 10px;
	height: 30px;
	width: 100px;
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
				style="border-bottom: 2px solid #CA3D2A; display: inline-block; position: relative;">
				<c:if test="${mode eq 'myOrderList'}">
					<h3>${sessionScope.member.userName}님의&nbsp;주문내역입니다.</h3>
				</c:if>
				<c:if test="${mode eq 'update'}">
					<h3>본인여부&nbsp;확인</h3>
				</c:if>
			</div>
		</div>
	</div>
		<c:if test="${mode eq 'update'}">
			<form name="confirm" method="post" style="margin:30px 10px;">
				<div>
					<div class="box">
					<h4>아이디 : ${sessionScope.member.userId}</h4>
					</div>
					<div class="box">
					<h4>비밀번호 확인</h4><input type="password" name="userPwd" class="inputStyle"
							placeholder="패스워드 입력">
					</div>
					<button type="button" class="btn" onclick="sendConfirm();">확인</button>
					${msg}
				</div>
			</form>
		</c:if>
</div>