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
   
   function listNotice() {
      var f = document.listNoticeForm;
      f.page.value="1";
      f.action="${pageContext.request.contextPath}/admin/boardList";
      f.submit();
   }
   
   <c:if test="${sessionScope.member.userId=='admin'}">
   $(function(){
      $("checkAll").click(function(){
         if($(this).is(":checked"))
            $("input[name=nums]".)prop("checked", true);
         else $("input[name=nums]").prop("checked",false);         
      });
      
      $("deleteBtn").click(function(){
         var cnt = $("input[name=nums]:checked").length;
         if(cnt==0){
            alert("삭제할 게시글을 선택하세요.");
            return false;
         }
         
         if(confirm("선택할 게시글을 삭제하시겠습니까?")) {
            var f = document.listNoticeForm;
            f.action="${pageContext.request.contextPath}/admin/board_delete";
            f.submit();
         }
      })
   });
   </c:if>
</script>

<style type="text/css">
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}

.container {
	padding: 30px 0px 0px 30px;
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
	margin-top: 42px;
	height: 45px;
}
.boxTF {
	width: 425px;
	height: 43px;
	padding-left: 15px;
	border: 1px solid #ddd;
	color: #888888;
	
}

.btn {
	width: 43px;
	height: 43px;
	background-color: white;
	border: 1px solid #ddd;
	cursor: pointer;

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
		
			<table style="width: 100%; height:120px; margin: 30px auto; border-spacing: 0px;">
				<tr>
					<td align="center" style=" width:100%; border-top: 1.5px solid #111;">
					<form name="searchForm" action="${pageContext.request.contextPath}/admin/boardList" method="post">
						<select name="condition" class="selectField">
							<option value="all" ${condition=="all"?"selecte='selected'":""}>제목+내용</option>
							<option value="subject" ${condition=="subject"?"selected='selected'":""}>제목</option>
				            <option value="content" ${condition=="content"?"selected='selected'":"" }>내용</option>			       						
						</select>
						<div class="boxTFdiv">
						<input type="text" name="keyword" class="boxTF" value="${keyword}">
						<button type="button" class="btn" onclick="listSearch()"><img alt="" src="${pageContext.request.contextPath}/resource/images/notice_search.png" style="padding-top: 5px;"></button>
						</div>
					</form>
				</td>
				
				<td align="right" width="100">
					<c:if test="${sessionScope.member.id=='admin'}">
						<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/admin/board_create';">등록</button>
					</c:if>
				</td>
			</tr>
		</table>
		
		<div>
			<form action="listNoticeForm" method="post">
			
			<table class="allDataCount">
				<tr height="20">
					<td align="left" width="50%">
					<c:if test="${sessionScope.member.id=='admin'}">
						<button type="button" class="btn" id="deleteListBtn">삭제</button>
					</c:if>
					<c:if test="${sessionScope.member.id!='admin'}">
						총 ${dataCount}건
					</c:if>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
				<tr align="center" height="55">
					<c:if test="${sessionScope.member.id=='admin'}">
						<th width="40" style="color: #787878;">
							<input type="checkbox" name="chkAll" style="margin-top: 3px;">
						</th>
					</c:if>
					<th width="105">번호</th>
					<th>제목</th>
					<th width="330">등록일</th>
					<th width="107">조회수</th>
				</tr>
				
				<c:forEach var="dto" items="${listNotice}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
						<c:if test="${sessionScope.member.id=='admin'}">
							<td>
								<input type="checkbox" name="nums" value="${dto.postNum}" style="margin-top: 3px;">
							</td>
						</c:if>
						
						<td align="left" style="padding: 20px 0px 20px 150px;">
							<a href="${boardUrl}&num=${dto.postNum}">${dto.subject}</a>
						</td>
						<td>${dto.name}</td>
						<td>${dto.created}</td>
						<td>${dto.hitCount}</td>
				</c:forEach>
				
				<c:forEach var="dto" items="${list}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
						<c:if test="${sessionScope.member.id=='admin'}">
							<td>
								<input type="checkbox" name="nums" value="${dto.postNum}" style="margin-top: 3px;">
							</td>
						</c:if>
						<td>${dto.listNum}</td>
						<td>${dto.name}</td>
						<td>${dto.created}</td>
						<td>${dto.hitCount}</td>
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
			

		
	</div>
	
	
	</div>
</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html>