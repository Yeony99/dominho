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

<style type="text/css">
body{
	margin: 0px;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	// form 안의 input 형제 요소(+) 중 바로 다음에 오는 span 태그 숨기기
	$("form input + span").hide();

	$("form input").not($(":button")).not($(":reset")).focus(function(){
		$(this).next("span").show();
		$(this).css("border", "1px solid red");
	});
	
	$("form input").not($(":button")).not($(":reset")).blur(function(){
		$(this).next("span").hide();
	});
});

function sendReview(){
	var f=document.reviewForm;
	var str;
	
	str=f.orderNum.value;
	if(! str){
		alert("주문번호를 입력하세요!");
		f.orderNum.focus();
		return;
	}
	
	str=f.content.value;
	if(! str){
		alert("리뷰를 입력하세요!");
		f.content.focus();
		return;
	}
	f.action="${pageContext.request.contextPath}/review/review_ok.do";
	f.submit();
}

function deleteReview(reviewNum){
	var url = "${pageContext.request.contextPath}/review/deleteReview.do?reviewNum="+reviewNum+"&page=${page}";
	
	if(confirm("삭제하시겠습니까?"))
		location.href=url;
}
</script>
</head>
<body>
<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<form name="reviewForm" method="post" action="">
             <div class="guest-write" style="width:70%; margin: 10px auto;">
                 <div style="clear: both;">
                         <span style="font-weight: bold;">리뷰</span>
                 </div>
                 <div style="clear: both; padding-top: 10px;">
                 
                       <input type="text" name="orderNum" maxlength="100" style="width: 150px; height: 100;" value="${rdto.orderNum}">
                       <span>주문번호를 입력하세요.</span>
                  
                  </div>
                 <div style="clear: both; padding-top: 10px;">
                 
                       <input type="text" name="content" maxlength="100" style="width: 300px; height: 170;" value="${rdto.content}">
                       <span>리뷰 내용을 입력하세요.</span>
                  
                  </div>
                  <div style="text-align: left; padding-top: 10px;">
                       <button type="button" class="btn" onclick="sendReview();" style="padding:8px 25px;"> 등록하기 </button>
                  </div>           
            </div>
           </form>
         
           <div id="listGuest" style="width:70%; margin: 0px auto;">
             <c:if test="${dataCount != 0}">
                 <table style='width: 100%; margin: 10px auto 0px; border-spacing: 0px; border-collapse: collapse;'>
                    <tr height='35'>
                        <td width='50%'>
                            <span style='color: #3EA9CD; font-weight: 700;'>리뷰 ${dataCount}개</span>
                            <span>[목록, ${page}/${total_page} 페이지]</span>
                        </td>
                        <td width='50%'>
                            &nbsp;
                        </td>
                    </tr>
                    
                    <c:forEach var="rdto" items="${list}">
                         <tr height='35' bgcolor='#eeeeee'>
                         		<td width='50%' align='left'>
                         			주문 번호 : ${rdto.orderNum} 
                         		</td>
                                <td align='right' style='padding-right: 5px; border:1px solid #cccccc; border-left:none;'>
									   ${rdto.created}
                                       <c:if test="${sessionScope.member.userId=='admin'}">    
                                           | <a href="javascript:deleteReview('${rdto.reviewNum}');">삭제</a>
                                       </c:if>
                                </td>
                         </tr>
                         
                         <tr height='50'><td colspan='2' style='padding: 5px;' valign='top'>${rdto.content}</td></tr>
                    </c:forEach>                          
                         <tr><td colspan='2' height='30' align='center'>${paging}</td></tr>
                 </table>
             </c:if>
           </div>
</body>
</html>