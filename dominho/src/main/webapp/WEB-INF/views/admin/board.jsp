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
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
<c:if test=${sessionScope.member.userId=='admin'}">
function deleteBoard(postNum) {
	if(confirm("게시물을 삭제하시겠습니까 ?")){
		var url = "${pagaContext.request.contextPath}/admin/board_delete?num="num+"&${query}";
		location.href=url;
	}
}
</c:if>
</script>

<style type="text/css">
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}
</style>
</head>
<body>

<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
	<div class="body-con" style="width: 1200px;">
		<div class="body_title">
			<span>공지사항</span>
		</div>
		
		<div>
			<table style="width: 100%;margin: 30px auto; border-spacing: 0px; border-collapse: collapse; border-top: 1.5px solid #111;">
				<tr align="left" style=" height=70px; border-bottom: 1px solid #ddd;">
					<td colspan="2" style="text-align: center;">
						${dto.subject}
					</td>			
				</tr>	
				<tr style="height: 70px; border-bottom: 1px solid #ddd;">
					<td width="50%" align="right" style="padding-left: 5px;">
						${dto.created} | 조회 ${dto.hitCount}
					</td>
				</tr>
				<tr style="border-bottom: 1px solid #ddd;">
				  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="400">
				  	${dto.content}
				  </td>
				 </tr>
					
				<c:forEach var="vo" items="${listFile}">
					<tr height="55" style="border-bottom: 1px solid #ddd;">
						<td colspan="2" align="left" style="padding-left: 5px;">
							첨&nbsp;부&nbsp; <a href="${pageContext.request.contextPath}/admin/donload?fileNum=${vo.fileNum}">${vo.originalFilename}</a>
						</td>
					</tr>
				</c:forEach>
				<tr height="55" style="border-bottom: 1px solid #ddd;">
					<td colspan="2" align="left" style="padding-left: 5px;">
					이전
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/admin/board?${query}&postNum=${preReadDto.postNum}">${preReadDto.subject} </a>
					</c:if>
					</td>
				</tr>
				
				<tr height="55" style="border-bottom: 1px solid #ddd;">
					<td colspan="2" align="left" style="padding-left: 5px;">
					다음
					<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/admin/board?${query}&postNum=${nextReadDto.postNum}">${nextReadDto.subject} </a>
					</c:if>
					</td>
				</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="62">
				<td width="600" align="left">
					<c:if test="${sessionScope.member.userId=='admin'}">
						<button type="button" class="btn" onclick="javascript:location.href=${pageContext.request.contextPath}/admin/board_update?postNum=${dto.postNum}&page=${page}';">수정</button>
						<button type="button" class="btn" onclick="deleteBoard('${dto.postNum}');">삭제</button>
					</c:if>
				</td>
				<td align="right">
					<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/admin/boardList?${query}';">목록</button>
				</td>					
			</table>
		</div>
	
	</div>
</div>
<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html>