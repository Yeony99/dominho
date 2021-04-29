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
<title>공지사항- 도민호피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script type="text/javascript">
   function listSearch() {
      var f = document.listSearchForm;
      f.submit();
   }
   
</script>

<style type="text/css">
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}

.container {
	padding: 30px 0px 0px 80px;
}

.allDataCount {
	width: 100%;
	margin: 20px auto 0px;
	border-spacing: 0px;
	border-bottom: 1.5px solid #111;
	font-size: small;
	padding-bottom: 5px;
}

.selectField {
	height: 45px;
	padding-left: 20px;
	padding-right: 50px;
	border: 1px solid #ddd;
	color: #888888;
	font-size: 15px;
	float: left;
	margin-right: 5px;
	margin-left: 250px;
	margin-top: 53px;
}

.boxTFdiv {
	float: left;
	margin-top: 53px;
	height: 47px;
}
.boxTF {
	width: 425px;
	height: 43px;
	padding-left: 15px;
	border: 1px solid #ddd;
	color: #888888;
	float: left;
	margin-right: 5px;
	
}

.btnSearch {
	width: 43px;
	height: 43px;
	background-color: white;
	border: 1px solid #ddd;
	cursor: pointer;

}
.btnCreate{
	width: 60px;
	height: 40px;
	background-color: #424242;
	color: white;
	border: 1px solid #ddd;
	cursor: pointer;
	margin-left: 1040px;
	margin-bottom: 30px;
}
.btnDelete{
	width: 50px;
	height: 30px;
	background-color:white;
	border : 1px solid #ddd;
	border-radius: 10px;
	cursor: pointer;
}
a {
 text-decoration: none
}

a:link {
	color: #212121;
}
a:hover {
	color: #f06292;
}
</style>
</head>
<body>

<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
	<div class="body_con" style="width: 1100px;">
		<div class="body_title">
			<span>공지사항</span>
		</div>
		
			<table style="width: 100%; height:120px; margin: 30px auto; border-spacing: 0px;">
				<tr>
					<td align="center" style=" width:100%; border-top: 2px solid #111;">
					<form name="listSearchForm" action="${pageContext.request.contextPath}/admin/boardList" method="post">
						<select name="condition" class="selectField">
							<option value="subject" ${condition=="subject"?"selected='selected'":""}>제목</option>
				            <option value="content" ${condition=="content"?"selected='selected'":"" }>내용</option>			       						
			                  <option value="all" ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
						</select>
						<div class="boxTFdiv">
						<input type="text" name="keyword" class="boxTF" value="${keyword}">
						<button type="button" class="btnSearch" onclick="listSearch()"><img alt="" src="${pageContext.request.contextPath}/resource/images/notice_search.png" style="padding-top: 5px;"></button>
						</div>
					</form>
				</td>
			</tr>
		</table>
		
		<div>
			<form>		
			<table class="allDataCount">
				<tr height="20">
					<td align="left" width="50%">
						총 ${dataCount}건
					</td>
				</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
				<tr align="center" height="55">
					<th width="105">번호</th>
					<th>제목</th>
					<th width="330">등록일</th>
					<th width="107">조회수</th>
				</tr>
				
				<c:forEach var="dto" items="${listNotice}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">			
						<td align="left" style="padding: 20px 0px 20px 150px;">
							<a href="${articleBoardUrl}&postNum=${dto.postNum}">${dto.subject}</a>
						</td>
						<td width="330">${dto.created}</td>
						<td width="107">${dto.hitCount}</td>
				</c:forEach>
				
				<c:forEach var="dto" items="${list}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
						<td>${dto.listNum}</td>
						<td align="left" style="text-align: center;">
							<a href="${articleBoardUrl}&postNum=${dto.postNum}">${dto.subject}</a>
						</td>
						<td width="330">${dto.created}</td>
						<td width="107">${dto.hitCount}</td>
					</tr>
				</c:forEach>			
			</table>
	
			</form>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
				<tr height="55">
					<td align="center">
						${dataCount!=0?paging:"등록된 게시물이 없습니다."}
					</td>		
				</tr>
			</table>
			
			<span>
				<c:if test="${sessionScope.member.userId=='admin'}">
					<button type="button" class="btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/admin/board_create';">등록</button>
				</c:if>
			</span>			
			

		
	</div>
	
	
	</div>
</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html>