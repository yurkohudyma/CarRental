<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/fragments/header/favicon.jsp" %> 
<c:choose>
    <c:when test="${locale == 'uk'}">
        <fmt:setLocale value="uk"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en"/>
    </c:otherwise>
</c:choose>

<fmt:setBundle basename="local"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/css/main.css">
    <fmt:message key="order.form.title" var="order_title"/>
      <fmt:message key="main.title" var="main_title"/>
    <title>${order_title}::${main_title}</title>
</head>
<body>
<!--  <div class="main-content"> -->
    <div class="head-content">
        ${order_title}
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        <tags:logout userEmail="${user.email}" curr_lang="${locale}"/>
    </div>
    <fmt:message key="order.form.nameSurname" var="nameSurname"/>
    <fmt:message key="order.form.model" var="model"/>
    <fmt:message key="order.form.carclass" var="carclass"/>
    <fmt:message key="order.form.duration" var="duration"/>
    <fmt:message key="order.form.pickup" var="pickup"/>
    <fmt:message key="order.form.dropoff" var="dropoff"/>
    <fmt:message key="order.form.comment" var="comment"/>
    <fmt:message key="order.form.passdata" var="passdata"/>
    <fmt:message key="order.form.driver" var="driver"/>
    <div class="form">
    <form action="order" method="post">
    <!-- here actionName within NewOrder routine changes to 'order' -->
        <input type="hidden" name="actionName" value="order"/>
        ${nameSurname}: <input type="text" placeholder="Степан Жменя" name="name" title="Степан Жменя" autofocus required pattern="^[А-ЯҐЇІЄ]{1}[а-яґїіє]{2,}\s[А-ЯҐЇІЄ]{1}[а-яґїіє]{2,}|[A-Z]{1}[a-z]{2,}\s[A-Z]{1}[a-z]{2,}$"/>
        <br><br>
        ${passdata}: <input type="text" placeholder="AB123456" title="AB123456 (латина latin)" name="passdata" autofocus required pattern="^[A-Z]{2}[0-9]{6}"/>
        <br><br>
        ${model}: <select class="button" name="model">
        <option value="1">Fiat 500</option>
        <option value="2">Ford Fiesta</option>
        <option value="3">Nissan Juke</option>
        <option value="4">Kia Ceed SW</option>
        <option value="5">Volvo S90</option>
        </select>
        <br><br>
        <!-- carclass selection is deprecated, while it shall be active and define car models auto reloading -->
        ${carclass}: <select class="button" name="carclass">
        <option value="MINI">Mini</option>
        <option value="ECONOMY">Economy</option>
        <option value="SUV">SUV</option>
        <option value="ESTATE">Estate</option>
        <option value="PREMIUM">Premium</option>
        </select>
        <br><br>
        ${duration}:
        <br>
        ${pickup}: <input class="button" type="date" placeholder="ДД-ММ-РРРР" name="date_in" required/>
        <br><br>
        ${dropoff}: <input class="button" type="date" placeholder="ДД-ММ-РРРР" name="date_out" required/>
        <br><br>
        ${driver}: <input type="checkbox" checked value = "true" name="driver"/>
        <br><br>
        ${comment}: <br>
        <textarea name="comment" placeholder="Ваші побажання"></textarea>
        <br><br>
        <fmt:message key="order.form.submit" var="submit_order"/>
        <input class="button" type="submit" value="${submit_order}">

        <c:if test="${not empty order_error}">
            <fmt:message key="order.form.fail" var="fail"/>
            <fmt:message key="order.form.error_msg" var="error_msg"/>
            <div class="error">
                    ${fail}: ${error_msg}
            </div>
        </c:if>
    </form>
    <c:if test="${not empty error}">
        <fmt:message key="orders.error" var="error"/>
        ${error}
    </c:if>
    <fmt:message key="ref.my_page" var="my_page"/>
    <h4><a href=<c:url value="/user"/>>${my_page}</a></h4>
    </div>
<!-- </div> -->
<jsp:include page="/WEB-INF/fragments/header/footer.jsp"/>
</body>
</html>
