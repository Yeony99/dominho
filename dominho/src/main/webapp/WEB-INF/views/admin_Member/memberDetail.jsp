<%@ page contentType="text/html; charset=UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<title>고객정보- 도민호피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function deleteMember(userId) {
	if(confirm("해당 회원을 탈퇴처리합니다.")){
		var url="${pageContext.request.contextPath}/admin_Member/member_delete?userId="+userId+"&${query}";
		location.href=url;
	}
}


</script>
<style type="text/css"> 
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
	width: 100%;
}

.container {
	padding: 30px 0px 0px 450px;
}
.tdtitle{
	width: 150px;
	height: 50px;
	padding-left: 15px;
	font-weight: 500;
}
.tdcontent, .tdcontent{
	width: 200px;
	height: 50px;
	padding-left: 40px;
}
.tdcontent1, .tdcontent2{
	height: 50px;
	width: 150px;
	padding-left: 40px;
	
}
.btn{
	width: 60px;
	height: 40px;
	background-color: #424242;
	color: white;
	border: 1px solid #ddd;
	cursor: pointer;
	margin-bottom: 10px;
	margin-top: 10px;
}


</style>
</head>

<body>
<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

	<div class="container">
		<div class="body_con" style="width:350px;">
			<div class="body_title">
				<span>고객정보</span>
			</div>

			<div>
				<table style="border-top: 2px solid #111; border-bottom: 2px solid #111; margin-top: 30px; background: #fafafa;">
					<tr class="trcontent">
						<th class="tdtitle">이름</th>
						<td class="tdcontent">${dto.userName}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">아이디</th>
						<td class="tdcontent">${dto.userId}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">비밀번호</th>
						<td class="tdcontent">${dto.userPwd}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">E-mail</th>
						<td class="tdcontent">${dto.email}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">생일</th>
						<td class="tdcontent">${dto.birth}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">연락처</th>
						<td class="tdcontent">${dto.tel}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">우편번호</th>
						<td class="tdcontent">${dto.zip}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle" style="height: 160px;">주소</th>
						<td class="tdcontent1" style="float: left; padding-top: 30px;">${dto.address1}</td>
						<td class="tdcontent2"rowspan="2" style="float: left;">${dto.address2}</td>
					</tr>

					<tr class="trcontent">
						<th class="tdtitle">가입일</th>
						<td class="tdcontent">${dto.joinedDate}</td>
					</tr>
				</table>
				
		<div>
			<button type="button" class="btn" style="margin-left: 120px;" onclick="deleteMember('${dto.userId}');">탈퇴</button>
			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/admin_Member/memberList?${query}';">목록</button>
			
		</div>
			</div>
		</div>
	</div>

	<div class="footer" style="padding-top: 50px;">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>