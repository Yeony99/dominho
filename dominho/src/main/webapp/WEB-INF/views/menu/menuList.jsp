<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<style type="text/css">
.imgLayout {
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #dad9ff;
	cursor: pointer;
}

.subject {
	display: inline-block;
	width: 180px;
	height: 25px;
	line-height: 25px;
	margin: 5px auto;
	border-top: 1px solid #dad9ff;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
</style>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
function searchList() {
	var f=document.searchForm;
	f.submit();
}
</script>
</head>

<body>

<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
<div class="body-container" style="width: 700px;">
    <div class="body-title">
        <h3> 메뉴 </h3>
    </div>
    
    <div>
    	<table style="width: 630px; margin: 0 auto; border-spacing: 0">
    	<c:forEach var="dto" items="${list}" varStatus="status">
    		<c:if test="${status.index == 0}">
    			<tr>
    		</c:if>
    		<c:if test="${status.index != 0 && status.index%3 == 0}">
    			<c:out value="</tr><tr>" escapeXml="false"/>
    		</c:if>
    		<td width="210" align="center">
    			<div class="imgLayout" onclick="location.href='${articleUrl}&num=${dto.menuNum}';">
    				<img src="${pageContext.request.contextPath}/uploads/menu/${dto.imageFilename}" width="180" height="180">
    				<span class="subject">${dto.menuName}</span>
    				<c:choose>
    					<c:when test="${sessionScope.member.userId=='admin'}">
    						<span>${mddto.count}</span>
    					</c:when>
    					<c:otherwise>
    					
    					</c:otherwise>
    				</c:choose>
    			</div>
    		</td>
    	</c:forEach>
    	
    	<c:set var="n" value="${list.size()}"/>
    	<c:if test="${n > 0 && n%3 != 0}">
    		<c:forEach var="i" begin="${n%3+1}" end="3">
    			<td width="210">
    				<div class="imgLayout">&nbsp;</div>
    			</td>
    		</c:forEach>
    	</c:if>
    	<c:if test="${n!=0}">
    		<c:out value="</tr>" escapeXml="false"/>
    	</c:if>
    	</table>
		 
		<table style="width: 100%; border-spacing: 0;">
		   <tr height="35">
			<td align="center">
		        ${dataCount==0?"등록된 메뉴가 없습니다.":paging}
			</td>
		   </tr>
		</table>
		
		<table style="width: 100%; margin-top: 10px; border-spacing: 0;">
		   <tr height="40">
		      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/menu/menuList.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/menu/menuList.do" method="post">
			              <select name="condition" class="selectField">
			                  <option value="all" ${condition=="all"?"selected='selected'":""}>메뉴+분류</option>
			                  <option value="menuType" ${condition=="menuType"?"selected='selected'":""}>분류</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF" value="${keyword}">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
		      <td align="right" width="50%">
		          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/menu/menuCreated.do';">메뉴 올리기</button>
		      </td>
		   </tr>
		</table>

    </div>
</div>
</div>

<div class="footer">
<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>