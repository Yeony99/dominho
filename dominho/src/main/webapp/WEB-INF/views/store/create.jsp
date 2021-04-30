<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/jquery.bxslider.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
<title>Dominho Pizza!</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script type="text/javascript">
function sendStore(){
	var f = document.storeForm;
	
	var str = f.storeName.value;
	if(!str){
		alert("매장명을 입력하세요.");
		f.storeName.focus();
		return;
	}
	
	str = f.storeTel.value;
	if(!str){
		alert("매장 번호를 입력하세요.");
		f.storeTel.focus();
		return;
	}
	
	str = f.openingHours.value;
	if(!str){
		alert("매장 오픈 시간을 입력하세요.");
		f.openingHours.focus();
		return;
	}
	
	str = f.closingHours.value;
	if(!str){
		alert("매장 마감 시간을 입력하세요.");
		f.closingHours.focus();
		return;
	}
	// f.action = 부분 검사 맡기
	 f.action = "${pageContext.request.contextPath}/store/${mode}_ok.do";
     f.submit();
}

</script>

</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<div id="container">
		<div style="width: 600px; margin: 30px auto;">
		<table style="width: 100%; border-spacing: 0;">
		<tr height="45">
			<td align="left" class="title">
				<h3><span>|</span> 매장 </h3>
			</td>
		</tr>
		</table>
		
		<form name="storeForm" method="post">
		  <table style="width: 100%; margin-top: 20px; border-spacing: 0; border-collapse: collapse;">
		
		  <tr height="40" style="border-bottom: 1px solid #cccccc;"> 
		      <td width="100" bgcolor="#eeeeee" style="text-align: center;">매장명</td>
		      <td style="padding-left:10px;"> 
		        <input type="text" name="storeName" size="35" maxlength="20" class="boxTF" value="${dto.storeName}">
		      </td>
		  </tr>
		
		  <tr height="40" style="border-bottom: 1px solid #cccccc;"> 
		      <td width="100" bgcolor="#eeeeee" style="text-align: center;">매장 번호</td>
		      <td style="padding-left:10px;"> 
		        <input type="text" name="storeTel" size="35" maxlength="20" class="boxTF" value="${dto.storeTel}">
		      </td>
		  </tr>
		
		  <tr height="40" style="border-bottom: 1px solid #cccccc;"> 
		      <td width="100" bgcolor="#eeeeee" style="text-align: center;">매장 주소</td>
		      <td style="padding-left:10px;"> 
		        <input type="text" name="storeAddress" size="35" maxlength="20" class="boxTF" value="${dto.storeAddress}">
		      </td>
		  </tr>
		
		    <tr height="40" style="border-bottom: 1px solid #cccccc;"> 
		      <td width="100" bgcolor="#eeeeee" style="text-align: center;">오픈 시간</td>
		      <td style="padding-left:10px;"> 
		        <input type="text" name="openingHours" size="35" maxlength="20" class="boxTF" value="${dto.openingHours}">
		      </td>
		  </tr>
		
		    <tr height="40" style="border-bottom: 1px solid #cccccc;"> 
		      <td width="100" bgcolor="#eeeeee" style="text-align: center;">마감 시간</td>
		      <td style="padding-left:10px;"> 
		        <input type="text" name="closingHours" size="35" maxlength="20" class="boxTF" value="${dto.closingHours}">
		      </td>
		  </tr>
		  </table>
		
		<table style="width:100%; border-spacing: 0;">
		     <tr height="45"> 
		      <td align="center" >
		      	<c:if test="${mode=='update'}">
		      		<input type="hidden" name="storeNum" value="${dto.storeNum}">
		      		<input type="hidden" name="page" value="${page}">
		      	</c:if>
		      
		        <button type="button" class="btn" onclick="sendStore();">${mode=="update"?"수정완료":"등록완료"}</button>
		        <button type="reset" class="btn">다시입력</button>
		        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/list.do';">${mode=="update"?"수정취소":"등록취소"}</button>
		      </td>
		    </tr>
		  </table>
		 </form>
		</div> 
	</div>
	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/bxslider.js"></script>

</body>
</html>