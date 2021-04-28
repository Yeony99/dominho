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
function sendBoard() {
	var f = document.boardForm;
	
	var str = f.subject.value;
	
	if(!str){
		alert("제목이 입력되지 않았습니다.");
		f.subject.focus();
		return;
	}
	
	str = f.content.value;
	if(!str){
		alert("내용이 입력되지 않았습니다.");
		f.content.focus();
		return;
	}
	
	f.action="${pageContext.request.contextPath}/admin/board_ok_${mode}";
	f.submit();
}

<c:if test="${mode}=='update'}">
	function deleteFile(fileNum) {
		if(! confirm("파일을 삭제하시겠습니까?")){
			return;
		}
		
		var url = "${pageContext.request.contextPath}/admin/deleteFile?postNum=${dto.postNum}&fileNum="+fileNum+"&page={page}";
		location.href=url;
	}
</c:if>

</script>

<style type="text/css">
.container {
	padding: 50px 0px 0px 30px;
}

.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}

.boxTA {
	resize: none;
	max-height: 295px;
	overflow-y: scroll;
	width: 650px;
	height: 295px;
	border: solid 1px #ddd;
	border-radius: 0;
	appearance: none;
	line-height: 20px;
	margin-left: 10px;
	margin-top: 20px;
	padding-left: 15px;
	padding-top: 15px;
}

.boxTF {
	height: 42px;
	width: 650px;
	border: solid 1px #ddd;
	padding-left: 15px;
}
.btn{
	width: 150px;
	height: 50px;
	font-size: medium;
	border-radius: 0;
	background-color: #424242;
	color: white;
	cursor: pointer;
	border: none;
	
}
</style>
</head>
<body>
<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
	<div class="body_con" style="width: 1200px;">
		<div class="body_title">
			<span>공지사항</span>
		</div>
		
		<div>
			<form name="boardForm" method="post" enctype="multipart/form-data">
				<table style="width: 100%;margin: 30px auto; border-spacing: 0px; border-collapse: collapse; border-top: 2px solid #111;">
					<tr align="left" height=100px; style=" border-bottom: 1px solid #ddd; ">
						<td style="text-align: center; ">제목</td>
						<td style="padding-left: 10px; ">
							<input type="text" name="subject" maxlength="50" class="boxTF" value="${dto.subject}">
						</td>			
					</tr>
					<tr align="left" style="border-bottom: 1px solid #ddd; height: 355px;">
						<td style="text-align: center; width: 250px;">내용</td>
						<td valign="top">
							<textarea name="content" class="boxTA">${dto.content}</textarea>
						</td>
					</tr>
					<tr align="left" height=100px; style="border-bottom: 1px solid #ddd; ">
						<td style="text-align: center;">첨부파일</td>
						<td style="padding-left: 10px;">
							<input type="file" name="selectFile" class="boxTF" size="53" style="height: 40px; padding-top: 15px;" multiple="multiple">
						</td>
					</tr>					
					
					<c:if test="${mode=='update'}">
						<c:forEach var="vo" items="${listFile}">
							<tr align="left" style="border-bottom: 1px solid #ddd" height="80px;">
								<td width="100" style="text-align: center;">첨부된 파일</td>
								<td style="padding-left: 10px;">
									<a href="javascript:deleteFile('${vo.fileNum}');"><i class="far fa-trash-alt"></i></a>
									${vo.originalFilename}
								</td>
							</tr>
						</c:forEach>
					</c:if>					
				</table>
				
				<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
					<tr height="60" >
						<td align="center" style="padding-bottom: 30px;">
							<button type="button" class="btn" onclick="sendBoard();">${mode=='update'?'수정':'등록'}</button>
							<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/admin/boardList';">${mode=='update'?'수정취소':'등록취소'}</button>
							<button type="reset" class="btn">재입력</button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="postNum" value="${dto.postNum}">
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
</body>
</html>