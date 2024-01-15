<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<html>
<head>
    <fmt:message key="auth_error.heading" var="auth_err_heading"/>
    <title>${auth_err_heading}</title>
</head>
<body>

<div class="main-content">
    <div class="head-content">
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        ${auth_err_heading}
    </div>
    <div class="form">
        <fmt:message key="auth_error.info" var="auth_err_info"/>
        ${auth_err_info}:
        <br>
        <fmt:message key="auth_error.log_in" var="log_in"/>
        <fmt:message key="auth_error.register" var="auth_err_register"/>
        <h4><a href=<c:url value="/authentification"/>>${log_in}</a></h4>
        <h4><a href=<c:url value="/registration"/>>${auth_err_register}</a></h4>
    </div>
</div>
</body>
</html>