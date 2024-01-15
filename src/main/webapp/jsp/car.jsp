<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/usernameDescriptor.tld" prefix="username"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ include file="/WEB-INF/fragments/header/favicon.jsp"%>

<c:choose>
    <c:when test="${locale == 'uk'}">
        <fmt:setLocale value="uk"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en"/>
    </c:otherwise>
</c:choose>

<fmt:setBundle basename="local"/>


<html>
<head>
    <link type="text/css" rel="stylesheet" href="css/main.css">
    <fmt:message key="car.heading" var="car_heading"/>
    <title>${car_heading}</title>
</head>
<body>
<div class="main-content">
    <div class="head-content">
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        <tags:logout userEmail="${user.email}" curr_lang="${locale}"/>
        <h3>${car_heading}</h3>
        <fmt:message key="car.model" var="car_model"/>
        <b>${car_model}</b>: ${car.model}
    </div>
    <div  class="form">
    <c:choose>
        <c:when test="${empty error}">
            <fmt:message key="car.carclass" var="carclass"/>
            <fmt:message key="car.price_day" var="price_day"/>
            <fmt:message key="car.description" var="descr"/>
            <form action="/admin_order" method="get">
                    <b>${carclass}</b>: ${car.carclass}
                <br>
                    <b>${price_day}</b>: ${car.price}
                <br>
                    <b>${descr}</b>: ${description}
                <fmt:message key="car.back_to_order" var="back_to_order"/>
                <c:choose>
                    <c:when test="${not empty isAdmin || not empty isManager}">
                        <fmt:message key="admin.search_car.offer" var="offer"/>
                        <br>
                        <input type="submit" value="${offer}">
                        <input type="hidden" name="car_id" value="${car.car_id}">
                        <input type="hidden" name="order_id" value="${order.order_id}">
                        <h4><a class="paging" href=<c:url
                                value="/admin_order?order_id=${order_id}"/>>${back_to_order}</a></h4>
                    </c:when>
                    <c:otherwise>
                        <h4><a class="paging" href=<c:url
                                value="/user_order?order_id=${order_id}"/>>${back_to_order}</a></h4>
                    </c:otherwise>
                </c:choose>
            </form>
        </c:when>
        <c:otherwise>
            <fmt:message key="orders.error" var="err_msg"/>
            ${err_msg}
        </c:otherwise>
    </c:choose>
    </div>
</div>
<jsp:include page="/WEB-INF/fragments/header/footer.jsp"/>
</body>
</html>
