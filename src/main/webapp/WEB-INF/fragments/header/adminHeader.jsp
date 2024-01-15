<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>

<fmt:message bundle="${naming}" key="admin.title" var="users"/>
<fmt:message bundle="${naming}" key="admin.label.orders" var="orders"/>
<fmt:message bundle="${naming}" key="admin.label.cars" var="cars"/>
<fmt:message bundle="${naming}" key="admin.label.users" var="users"/>
<fmt:message bundle="${naming}" key="admin.label.carsPrices" var="carPrices"/>

<!DOCTYPE html>

<!-- THIS FILE SEEMS LIKE NOT TO BE IN SERVICE //// YH -->

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="vertical-menu">
    <div class="buttons">
        <a href="controller?command=showAllOrders">${orders}</a>
    </div>
    <div class="buttons">
        <a href="controller?command=showCars&pageNumber=1&limit=5">${cars}</a>
    </div>
    <div class="buttons">
        <a href="controller?command=showUsers&pageNumber=1&limit=5">${users}</a>
    </div>
</div>
</body>
</html>

