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
<title>질문과 답변 - 도민호 피자</title>
<script type="text/javascript">
	<c:if test="${sessionScope.member.userId=='admin'}">
	function deleteBoard(postNum) {
		if (confirm("게시물을 삭제하시겠습니까 ?")) {
			var url = "${pageContext.request.contextPath}/admin/board_delete?postNum="
					+ postNum + "&${query}";
			location.href = url;
		}
	}
	</c:if>
</script>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}

.container {
	padding: 30px 0px 0px 80px;
}

.btn {
	width: 60px;
	height: 40px;
	background-color: #424242;
	color: white;
	border: 1px solid #ddd;
	cursor: pointer;
	margin-bottom: 10px;
}

a {
	text-decoration: none;
}

a:hover {
	color: #f06292;
}
button[disabled] {
	background: #eee;
}
</style>
<c:if
	test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
	<script type="text/javascript">
		function deleteBoard(qBoardNum) {
			if (confirm("게시물을 삭제 하시겠습니까 ?")) {
				var url = "${pageContext.request.contextPath}/qna/delete.do?qBoardNum="
						+ qBoardNum + "&${query}";
				location.href = url;
			}
		}
	</script>
</c:if>
</head>
<body>
	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="container">
		<div class="body-con" style="width: 1100px;">
			<div class="body_title">
				<span>Q&A 게시판</span>
			</div>

			<div>
				<table
					style="width: 100%; margin: 30px auto; border-spacing: 0px; border-collapse: collapse; border-top: 1.5px solid #111;">

					<tr style="height: 70px; border-bottom: 1px solid #ddd;">
						<td style="text-align: center; padding-left: 200px;">
						<c:if test="${dto.depth!=0 }">[Re] </c:if> 
							${dto.subject}
						</td>
						<td width="20%" align="right">${dto.created} | 조회
							${dto.hitCount}</td>
					</tr>
					<tr style="width: 80%; float: right;">
						<td
							style="padding: 20px 20px; margin-left: 100px; text-align: center; line-height: 60px;"
							valign="top" height="400">${dto.content}</td>
					</tr>
					<tr height="55"
						style="border-bottom: 1px solid #ddd; border-top: 1px solid #ddd;">
						<td colspan="2" align="left" style="padding-left: 5px;">이전 
						<c:if test="${not empty preReadDto}">
								<a style="padding-left: 20px;"
									href="${pageContext.request.contextPath}/qna/article.do?qBoardNum=${preReadDto.qBoardNum}&${query}">${preReadDto.subject}
								</a>
							</c:if>
						</td>
					</tr>

					<tr height="55" style="border-bottom: 1px solid #ddd;">
						<td colspan="2" align="left" style="padding-left: 5px;">다음 <c:if
								test="${not empty nextReadDto}">
								<a style="padding-left: 20px;"
									href="${pageContext.request.contextPath}/qna/article.do?qBoardNum=${nextReadDto.qBoardNum}&${query}">${nextReadDto.subject}
								</a>
							</c:if>
						</td>
					</tr>
				</table>

				<table
					style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
					<tr height="62">
						<td width="600" align="left" style="padding-left: 20px;">
						<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId}">
			    			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/qna/update.do?qBoardNum=${dto.qBoardNum}&${query}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">수정</button>
			    		</c:otherwise>
			    	</c:choose>
						
						<c:if test="${sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.userId}">
								<button type="button" class="btn"
									onclick="deleteBoard('${dto.qBoardNum}');">삭제</button>
							</c:if></td>
						
						<td align="right" style="padding-right:0;">
						<c:if test="${sessionScope.member.userId=='admin'}">
							<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/qna/reply.do?qBoardNum=${dto.qBoardNum}&page=${page}';">답변</button>
						</c:if>
							<button type="button" class="btn"
								onclick="javascript:location.href='${pageContext.request.contextPath}/qna/list.do?${query}';">목록</button>
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