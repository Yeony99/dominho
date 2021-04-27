<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
function deletePhoto(num) {
	if(confirm("메뉴를 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}";
		location.href=url;
	}
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
            <h3>메뉴</h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
				   ${dto.menuNum}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : ${dto.menuName}
			    </td>
			</tr>
			
			<tr>
				<td colspan="2" align="left" style="padding: 10px 5px; width">
					<div>
			      		<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" style="max-width: 100%; height: auto; resize: both;">
			      	</div>
				</td>
			</tr>
			
			<tr style="border-bottom: 1px solid #cccccc;">
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="45">
			    <td>
			    	<c:choose>
			    		<c:when test="${dto.userId==sessionScope.member.userId}">
			    			<p>재고 : ${mddto.count}</p>
			          		<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/menu/update.do?num=${dto.num}&page=${page}';">수정</button>
			          	</c:when>
			          	<c:otherwise>
			          		
			          	</c:otherwise>
			        </c:choose>	
			        <c:choose>
			        	<c:when test="${dto.userId==sessionScope.member.userId}">
			          		<button type="button" class="btn" onclick="deletePhoto('${dto.num}');">삭제</button>
			          	</c:when>
			          	<c:otherwise>
			          		
			          	</c:otherwise>
			        </c:choose>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/menu/menuList.do?${query}';">리스트</button>
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