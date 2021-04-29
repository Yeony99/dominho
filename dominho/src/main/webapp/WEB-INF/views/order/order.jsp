<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dominho Pizza | 주문하기</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<script type="text/javascript">
window.history.forward();
function noBack() {//뒤로가기로 못 오게(재주문 방지)

	window.history.forward();

}


$(document).ready(function() {
	discount();
})
	function daumPostcode() {
		new daum.Postcode(
				{
					oncomplete : function(data) {
						var fullAddr = '';
						var extraAddr = '';

						if (data.userSelectedType === 'R') { //도로명 주소
							fullAddr = data.roadAddress;

						} else { //지번 주소
							fullAddr = data.jibunAddress;
						}

						if (data.userSelectedType === 'R') {
							//법정동명이 있을 경우 추가한다.
							if (data.bname !== '') {
								extraAddr += data.bname;
							}
							// 건물명이 있을 경우 추가한다.
							if (data.buildingName !== '') {
								extraAddr += (extraAddr !== '' ? ', '
										+ data.buildingName : data.buildingName);
							}
							// 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
							fullAddr += (extraAddr !== '' ? ' (' + extraAddr
									+ ')' : '');
						}
						// 우편번호와 주소 정보를 해당 필드에 넣는다.
						document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
						document.getElementById('addr1').value = fullAddr;
						// 커서를 상세주소 필드로 이동한다.
						document.getElementById('addr2').focus();
					}
				}).open();
	}
	function check() {
		var f = document.orderForm
		var str;
		str = f.tel.value;
		str = str.trim();
		if (!str) {
			alert("전화번호를 입력하세요. ");
			f.tel.focus();
			return false;
		}
		if (!/^(\d+)$/.test(str)) {
			alert("전화번호는 숫자만 가능합니다. ");
			f.tel.focus();
			return false;
		}

		var radioVal = $('input[name="customRadioInline1"]:checked').val();
		if (radioVal == '배달') {
			str = f.zip.value;
			str = str.trim();
			if (!str) {
				alert("우편번호를 입력하세요. ");
				f.tel.focus();
				return false;
			}
			str = f.addr1.value;
			str = str.trim();
			if (!str) {
				alert("기본주소를 입력하세요. ");
				f.tel.focus();
				return false;
			}
			str = f.addr2.value;
			str = str.trim();
			if (!str) {
				alert("상세주소를 입력하세요. ");
				f.tel.focus();
				return false;
			}
			str = f.store2.value;
			if (str=='배달 매장 선택') {
				alert("매장을 선택하세요 ");
				f.tel.focus();
				return false;
			}
		}

		else if (radioVal == '포장') {
			str = f.store1.value;
			console.log(str);
			if (str=='포장 매장 선택') {
				alert("매장을 선택하세요 ");
				f.tel.focus();
				return false;
			}
		}
		radioVal = $('input[name="customRadioInline2"]:checked').val();
		if (radioVal == '카드결제') {
			str = f.cardNum.value;
			if (!str) {
				alert("카드 번호를 입력하세요");
				f.tel.focus();
				return false;
			}

		}

		return true;
	}
	function discount() {
		var radioVal = $('input[name="customRadioInline1"]:checked').val();
		if (radioVal == '배달') {
			$(".price").html("결제: "+${totalPrice}+"원")
		}else{
			$(".price").html("결제: "+${totalPrice*0.8}+"원(포장할인 20%적용" +${totalPrice*0.2}+"원 할인)");
			
		}
	}
</script>
<style type="text/css">
main {
	width: 80%;
	margin: 0 auto;
}

main h2 {
	text-align: center;
	color: #CA3D2A;
	font-weight: bold;
}

.box1 {
	border: 3px solid #0E191A;
	width: 100%;
	height: auto;
	display: flex;
	justify-content: space-around;
	align-items: center;
	margin-bottom: 10px;
	border-radius: 20px;
}

.prices {
	width: 100%;
	height: auto;
	display: flex;
	justify-content: space-around;
	align-items: center;
	margin-bottom: 10px;
	border-radius: 20px;
	background: linear-gradient(125deg, #FCD6B8, #FF8E21, #CA3D2A);
	color: white;
	border: none;
}
</style>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>
	<main>
		<h3>주문</h3>
		<br>
		<h2>주문 메뉴 정보</h2>
		<hr>
		<form name="orderForm" action="${pageContext.request.contextPath}/order/order_ok.do" method="post" onsubmit="return check();">
			<c:forEach var="dto" items="${cartlist}">
				<input type="hidden" name="menus" value="${dto.menuNum},${dto.quantity},${dto.price}">
				<div class="box1">
					<img src="${pageContext.request.contextPath}/uploads/menu/${dto.imageFileName}" alt="Card image cap" width="130px" height="130px">
					<p>${dto.menuName}×${dto.quantity}</p>
					<p>총 ${dto.price}원</p>
				</div>
			</c:forEach>
			<input type="hidden" value="${cartlist}">
			<div class="box">
				<div class="custom-control custom-radio custom-control-inline">
					<input type="radio" id="customRadioInline1" name="customRadioInline1" class="custom-control-input" value="배달" checked="checked" onclick="discount()">
					<label class="custom-control-label" for="customRadioInline1">배달</label>
				</div>
				<div class="custom-control custom-radio custom-control-inline">
					<input type="radio" id="customRadioInline2" name="customRadioInline1" class="custom-control-input" value="포장" onclick="discount()">
					<label class="custom-control-label" for="customRadioInline2">포장</label>
				</div>
			</div>
			<br>
			<div class="box">
				<h4>연락처</h4>
				<input class="form-control" type="text" name="tel" placeholder="핸드폰 번호를 숫자만 입력해주세요.">
			</div>
			<br>
			<div id="forWrap" style="display: none;">
				<div class="box">
					<h4>포장매장</h4>
					<select name="store1" class="custom-select custom-select-lg mb-3">
						<option selected>포장 매장 선택</option>
						<c:forEach var="store" items="${storelist}">
							<option value="${store.storeNum}">${store.storeName}(${store.storeAddress})</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div id="forDelivery">
				<div class="box">
					<h4>배달주소</h4>
					<button type="button" class="btn" onclick="daumPostcode();">우편번호</button>
					<input class="form-control" type="text" name="zip" id="zip" readonly="readonly" placeholder="우편번호">
				</div>
				<div class="box">
					<input class="form-control" type="text" name="addr1" id="addr1" maxlength="50" placeholder="기본 주소" readonly="readonly">
				</div>
				<div class="box">
					<input class="form-control" type="text" name="addr2" id="addr2" maxlength="50" placeholder="상세 주소를 입력해주세요.">
				</div>
				<br>
				<div class="box">
					<h4>배달매장</h4>
					<select name="store2" class="custom-select custom-select-lg mb-3">
						<option selected>배달 매장 선택</option>
						<c:forEach var="store" items="${storelist}">
							<option value="${store.storeNum}">${store.storeName}(${store.storeAddress})</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<br>
			<div class="box">
				<h4>요청사항</h4>
				<input class="form-control" type="text" name="requests" maxlength="100" placeholder="요청사항을 입력해주세요.">
			</div>
			<br>
			<div class="box">
				<h4>결제방법</h4>
				<div class="pay custom-control custom-radio ">
					<input type="radio" id="customRadioInline3" name="customRadioInline2" class="custom-control-input" value="카드결제" checked="checked">
					<label class="custom-control-label" for="customRadioInline3">카드결제</label> <br>
					<input type="text" name="cardNum" id="cardNum" class="form-control">
				</div>
				<div class="custom-control custom-radio ">
					<input type="radio" id="customRadioInline4" name="customRadioInline2" class="custom-control-input" value="만나서결제">
					<label class="custom-control-label" for="customRadioInline4">만나서 결제</label>
				</div>
			</div>
			<br>
			<c:if test="${fn:length(cartlist) > 0}">

				<div class="prices">
					<input type="hidden" value="${totalPrice}" name="totalPrice">
					<button type="submit" class="btn btn-danger btn-lg">주문완료하기</button>
					<div class="price"></div>
				</div>
			</c:if>


		</form>





	</main>
	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
	<script type="text/javascript">
		$("input[name='customRadioInline1']").change(function() {
			var radioVal = $('input[name="customRadioInline1"]:checked').val();
			if (radioVal == '포장') {
				$('#forDelivery').hide()
				$('#forWrap').show()
			} else {
				$('#forDelivery').show()
				$('#forWrap').hide()
			}
		})

		$("input[name='customRadioInline2']").change(function() {
			var radioVal = $('input[name="customRadioInline2"]:checked').val();
			if (radioVal == '만나서결제') {
				$('#cardNum').hide()
			} else {
				$('#cardNum').show()

			}
		})
	</script>
</body>
</html>