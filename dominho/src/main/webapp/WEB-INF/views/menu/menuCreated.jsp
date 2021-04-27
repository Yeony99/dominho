<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
    function sendMenu() {
        var f = document.menuForm;

    	var str = f.menuName.value;
        if(!str) {
            alert("메뉴 이름을 입력하세요. ");
            f.menuName.focus();
            return;
        }
        
        str = f.menuPrice.value;
        if(!str) {
            alert("메뉴 가격을 입력하세요. ");
            f.menuPrice.focus();
            return;
        }
        
        str = f.menuType.value;
        if(!str) {
            alert("메뉴 분류를 입력하세요. ");
            f.menuType.focus();
            return;
        }

    	str = f.menuExplain.value;
        if(!str) {
            alert("메뉴 설명을 입력하세요. ");
            f.menuExplain.focus();
            return;
        }
        
        var mode = "${mode}";
        if( mode=="menuCreated" || (mode=="update" && f.selectFile.value != "") ) {
        	if(! /(\.gif|\.jpg|\.png|\.jpeg)$/i.test(f.selectFile.value)) {
        		alert("이미지 파일만 가능합니다.");
        		f.selectFile.focus();
        		return;
        	}
        }

    	f.action="${pageContext.request.contextPath}/menu/${mode}_ok.do";
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
			<form name="menuForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">메&nbsp;&nbsp;&nbsp;&nbsp;뉴</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="menuName" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.menuName}">
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">가격</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="menuPrice" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.menuPrice}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">분류</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="menuType" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.menuType}">
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			          <textarea name="menuExplain" rows="12" class="boxTA" style="width: 95%;">${dto.menuExplain}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;"> 이미지 </td>
			      <td style="padding-left:10px;"> 
			          <input type="file" name="selectFile" class="boxTF" size="53" style="height: 25px;">
			       </td>
			  </tr> 
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.menuNum}">
			      		<input type="hidden" name="imageFilename" value="${dto.imageFilename}">
			      		<input type="hidden" name="page" value="${page}">
			      	</c:if>
			        <button type="button" class="btn" onclick="sendMenu();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/menu/menuList.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			        <c:if test="${mode=='update'}">
			        	<input type="hidden" name="menuNum" value="${dto.menuNum}">
			        	<input type="hidden" name="page" value="${page}">
			        </c:if>

				  </td>
			    </tr>
			  </table>
			</form>
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