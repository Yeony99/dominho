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
</head>
<body>
주문이 완료되었습니다.
<c:forEach var="order" items="${AllOrders}">
<div style="border: 3px solid #FF8E21; border-radius: 10px;">
<div>${order.orderNum}</div>
<div>${order.userId}</div>
<div>${order.storeName}</div>
<div>${order.orderDate}</div>
<div>${order.isDelivery}</div>
<div>${order.totalPrice}</div>
<div>${order.cardNum}</div>
<div>${order.menuName}</div>
<div>${order.orderCount}</div>
<div>${order.orderPrice}</div>
</div>
</c:forEach>


</body>
</html>