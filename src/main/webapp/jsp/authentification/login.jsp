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
    <fmt:message key="login.header" var="log_header"/>
    <fmt:message key="main.title" var="main_title"/>
    <title>${log_header}::${main_title}</title>
    <link type="text/css" rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="main-content">
    <div class="head-content">
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        <h2>${log_header}</h2>
    </div>
    <div class="form">
        <form action="" method="post">
            <input type="hidden" name="actionName" value="authentification"/>
            <fmt:message key="login.login_form.email" var="email"/>
            ${email}:
            <input type="text" name="email" autofocus required pattern="\S+@[a-z]+.[a-z]+" 
            placeholder="user@domain.com" title="user@domain.com"/>
            <br>
            <fmt:message key="login.login_form.password" var="password"/>
            ${password}:
            <input type="password" name="password" autofocus required/>
            <br>
            <fmt:message key="login.login_form.submit" var="submit"/>
            <input class="button" type="submit" value="${submit}"/>

			<!--  if 'auth_error' attrib is set -->
            <c:if test="${not empty auth_error}">
                <fmt:message key="login.login_form.fail" var="login_fail"/>
                <div class="error">
                        <font color = "red"><br>${login_fail}: ${auth_error}</font>
                </div>
            </c:if>
            <fmt:message key="login.login_form.reg_ref" var="reg_ref"/>
            <h4><a href=<c:url value="/registration"/>>${reg_ref}</a></h4>

        </form>
        <fmt:message key="registration.registration_form.main_page" var="main"/>
        <h4><a href=<c:url value="/"/>>${main}</a></h4>
    </div>
</div>
<jsp:include page="/WEB-INF/fragments/header/footer.jsp"/>
</body>
</html>