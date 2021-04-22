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

<title>회원가입 - 도민호 피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.memberForm {
	margin: 50px auto;
	width: 500px;
}

.idbox {
	border-bottom: 2px solid #adadad;
	margin: 30px;
	padding: 10px 10px;
}

.pwdbox {
	border-bottom: 2px solid #adadad;
	margin: 30px;
	padding: 10px 10px;
}

.id {
	width: 100%;
	border: none;
	outline: none;
	color: #636e72;
	font-size: 16px;
	height: 25px;
	background: none;
}

.pw {
	width: 100%;
	border: none;
	outline: none;
	color: #636e72;
	font-size: 16px;
	height: 25px;
	background: none;
}

table {
	width: 100%;
	margin: 20px auto 0px;
	border-spacing: 0px;
}

.joinbtn {
	width:100%;
	position: relative;
	left: 50%;
	transform: translateX(-50%);
	margin-bottom: 40px;
	height: 40px;
	background: linear-gradient(125deg, #FCD6B8, #FF8E21, #CA3D2A);
	background-position: left;
	background-size: 200%;
	color: white;
	font-weight: bold;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	transition: 0.4s;
	display: inline;
	cursor: pointer;
	margin: 40px 0;
}

.joinbtn:hover {
	background-position: right;
}

.btn {
	display: block;
    float: left;
    position: relative;
    left: 320px;
    bottom: 28px;
    border: none;
    border-radius: 10px;
    height: 30px;
    width: 100px;
}
</style>
<script type="text/javascript">
	function memberOk() {
		var f = document.memberForm;
		var str;

		str = f.userId.value;
		str = str.trim();
		if (!str) {
			alert("아이디를 입력하세요. ");
			f.userId.focus();
			return;
		}
		if (!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) {
			alert("아이디는 5~10자이며 첫글자는 영문자이어야 합니다.");
			f.userId.focus();
			return;
		}
		f.userId.value = str;

		str = f.userPwd.value;
		str = str.trim();
		if (!str) {
			alert("패스워드를 입력하세요. ");
			f.userPwd.focus();
			return;
		}
		if (!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,30}$/i.test(str)) {
			alert("패스워드는 5~30자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
			f.userPwd.focus();
			return;
		}
		f.userPwd.value = str;

		if (str != f.userPwdCheck.value) {
			alert("패스워드가 일치하지 않습니다. ");
			f.userPwdCheck.focus();
			return;
		}

		str = f.userName.value;
		str = str.trim();
		if (!str) {
			alert("이름을 입력하세요. ");
			f.userName.focus();
			return;
		}
		f.userName.value = str;

		str = f.birth.value;
		str = str.trim();
		if (!str || !isValidDateFormat(str)) {
			alert("생년월일를 입력하세요[YYYY-MM-DD]. ");
			f.birth.focus();
			return;
		}

		str = f.tel.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel.focus();
			return;
		}

		if (!/^(\d+)$/.test(str)) {
			alert("숫자만 가능합니다. ");
			f.tel.focus();
			return;
		}

		str = f.email.value;
		str = str.trim();
		if (!str) {
			alert("이메일을 입력하세요. ");
			f.email1.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/member/${mode}_ok.do";
		f.submit();
	}

	function changeEmail() {
		var f = document.memberForm;

		var str = f.selectEmail.value;
		if (str != "direct") {
			f.email2.value = str;
			f.email2.readOnly = true;
			f.email1.focus();
		} else {
			f.email2.value = "";
			f.email2.readOnly = false;
			f.email1.focus();
		}
	}
</script>
</head>
<body>
	<header class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<form name="memberForm" method="post" class="memberForm">
		<h2 style="text-align: center;">회원가입</h2>
		
		<div class="idbox">
		<h4>아이디</h4> <br>
			<input type="text" name="userId" class="id" value="${dto.userId}" placeholder="아이디는 5~10자 사이로 입력해주세요.">
		</div>
		<div class="idbox">
		<h4>이름</h4> <br>
			<input type="text" name="userId" class="id" value="${dto.userName}" maxlength="30" placeholder="이름을 입력해주세요.">
		</div>
		<div class="pwdbox">
		<h4>패스워드</h4> <br>
			<input type="password" name="userPwd" class="pw" placeholder="패스워드 (5자 이상 + 영문 혹은 숫자)">
		</div>
		<div class="pwdbox">
			<input type="password" name="userPwd" class="pw" placeholder="패스워드를 한 번 더 입력해주세요.">
		</div>
		<div class="idbox">
		<h4>생일</h4> <br>
			<input type="text" name="userId" class="id" value="${dto.birth}" placeholder="생일을 입력하세요. (2021-01-01)">
		</div>
		<div class="idbox">
		<h4>휴대폰 번호</h4> <br>
			<input type="text" name="userId" class="id" value="${dto.tel}" placeholder="휴대폰 번호를 입력해주세요.">
		</div>
		<div class="idbox">
		<h4>주소</h4> <br>
		<input type="text" name="zip" id="zip" value="${dto.zip}" class="id" readonly="readonly" placeholder="우편번호">
			 <button type="button" class="btn" onclick="daumPostcode();">우편번호</button>
		</div>
		<div class="idbox">
		<input type="text" name="addr1" id="addr1" value="${dto.addr1}" maxlength="50" class="id" placeholder="기본 주소" readonly="readonly">
		</div>
		<div class="idbox">
		<input type="text" name="addr2" id="addr2" value="${dto.addr2}" maxlength="50" class="id"  placeholder="상세 주소를 입력해주세요.">
	
		</div>
		<button type="button" class="joinbtn" onclick="memberOk();">회원가입</button>
		
		<table>
			<!-- 
			   <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">아이디</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="userId" id="userId" value="${dto.userId}"
                         onchange="userIdCheck();" style="width: 95%;"
                         ${mode=="update" ? "readonly='readonly' ":""}
                         maxlength="15" class="boxTF" placeholder="아이디">
			        </p>
			        <p class="help-block">아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야 합니다.</p>
			      </td>
			  </tr>
			
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">패스워드</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="password" name="userPwd" maxlength="15" class="boxTF"
			                       style="width:95%;" placeholder="패스워드">
			        </p>
			        <p class="help-block">패스워드는 5~10자 이내이며, 하나 이상의 숫자나 특수문자가 포함되어야 합니다.</p>
			      </td>
			  </tr>
			
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">패스워드 확인</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="password" name="userPwdCheck" maxlength="15" class="boxTF"
			                       style="width: 95%;" placeholder="패스워드 확인">
			        </p>
			        <p class="help-block">패스워드를 한번 더 입력해주세요.</p>
			      </td>
			  </tr>
			
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">이름</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="userName" value="${dto.userName}" maxlength="30" class="boxTF"
		                       style="width: 95%;"
		                      ${mode=="update" ? "readonly='readonly' ":""}
		                      placeholder="이름">
			        </p>
			      </td>
			  </tr>
			
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">생년월일</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="birth" value="${dto.birth}" maxlength="10" 
			                       class="boxTF" style="width: 95%;" placeholder="생년월일">
			        </p>
			        <p class="help-block">생년월일은 2000-01-01 형식으로 입력 합니다.</p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">이메일</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <select name="selectEmail" onchange="changeEmail();" class="selectField">
			                <option value="">선 택</option>
			                <option value="naver.com" ${dto.email2=="naver.com" ? "selected='selected'" : ""}>naver.com</option>
			                <option value="hanmail.net" ${dto.email2=="kakao.com" ? "selected='selected'" : ""}>kakao.com</option>
			                <option value="hotmail.com" ${dto.email2=="nate.com" ? "selected='selected'" : ""}>nate.com</option>
			                <option value="gmail.com" ${dto.email2=="gmail.com" ? "selected='selected'" : ""}>gmail.com</option>
			                <option value="direct">직접입력</option>
			            </select>
			            <input type="text" name="email1" value="${dto.email1}" size="13" maxlength="30"  class="boxTF">
			            @ 
			            <input type="text" name="email2" value="${dto.email2}" size="13"maxlength="30"  class="boxTF" readonly="readonly">
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">전화번호</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <select class="selectField" id="tel1" name="tel1" >
			                <option value="">선 택</option>
			                <option value="010" ${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
			                <option value="011" ${dto.tel1=="011" ? "selected='selected'" : ""}>011</option>
			                <option value="016" ${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
			                <option value="017" ${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
			                <option value="018" ${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
			                <option value="019" ${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
			            </select>
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">우편번호</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="zip" id="zip" value="${dto.zip}"
			                       class="boxTF" readonly="readonly">
			            <button type="button" class="btn" onclick="daumPostcode();">우편번호</button>          
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
			            <label style="font-weight: 900;">주소</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-top: 1px; margin-bottom: 5px;">
			            <input type="text" name="addr1" id="addr1" value="${dto.addr1}" maxlength="50" 
			                       class="boxTF" style="width: 95%;" placeholder="기본 주소" readonly="readonly">
			        </p>
			        <p style="margin-bottom: 5px;">
			            <input type="text" name="addr2" id="addr2" value="${dto.addr2}" maxlength="50" 
			                       class="boxTF" style="width: 95%;" placeholder="나머지 주소">
			        </p>
			      </td>
			  </tr>
			  <c:if test="${mode=='created'}">
				  <tr>
				      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
				            <label style="font-weight: 900;">약관동의</label>
				      </td>
				      <td style="padding: 0 0 15px 15px;">
				        <p style="margin-top: 7px; margin-bottom: 5px;">
				             <label>
				                 <input id="agree" name="agree" type="checkbox" checked="checked"
				                      onchange="form.sendButton.disabled = !checked"> <a href="#">이용약관</a>에 동의합니다.
				             </label>
				        </p>
				      </td>
				  </tr>
			  </c:if>
			  </table>
			
			  <table style="width:100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" name="sendButton" class="joinbtn" onclick="memberOk();">${mode=="member"?"회원가입":"정보수정"}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/';">${mode=="member"?"가입취소":"수정취소"}</button>
			      </td>
			    </tr>
			    <tr height="30">
			        <td align="center" style="color: blue;">${message}</td>
			    </tr> -->
		</table>
	</form>


	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>

</body>
</html>