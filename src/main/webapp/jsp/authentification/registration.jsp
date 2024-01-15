<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <fmt:message key="registration.header" var="reg_header"/>
    <fmt:message key="main.title" var="main_title"/>
    
    <title>${reg_header}::${main_title}</title>
</head>
<body>
<div class="main-content">
    <div class="head-content">
        <tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}"/>
        <h2>${reg_header}</h2>
    </div>
    <div class="form">
        <form action="registration" method="post">
            <input type="hidden" name="actionName" value="registration"/>
            <!--  
            <fmt:message key="registration.registration_form.name" var="name"/>
            ${name}:
            <input type="text" name="name"/>
            <br>-->
            <fmt:message key="registration.registration_form.email" var="email"/>
            ${email}:
            <input type="text" name="email" autofocus required pattern="\S+@[a-z]+.[a-z]+" 
            placeholder="user@domain.com" title="user@domain.com"/>
            <br>
            <fmt:message key="registration.registration_form.password" var="password"/>
            ${password}:
            <input type="password" name="password" autofocus required/>
            <br>
            <fmt:message key="registration.registration_form.submit" var="submit"/>
            <input class="button" type="submit" value="${submit}">
            <br>

            <c:if test="${not empty reg_error}">
                <div class="error">
                    <fmt:message key="registration.registration_form.fail" var="reg_fail"/>
                        <font color = "red"><br>${reg_fail}: ${reg_error}</font>
                </div>
            </c:if>
            <br>
            <fmt:message key="registration.registration_form.authentification.log_in" var="log_in"/>
            <fmt:message key="registration.registration_form.authentification.if_registered" var="if_regd"/>
            <a href=<c:url value="/authentification"/>>${log_in}</a> ${if_regd}
        </form>
        <fmt:message key="registration.registration_form.main_page" var="main"/>
        <h4><a href=<c:url value="/"/>>${main}</a></h4>
    </div>
</div>
<jsp:include page="/WEB-INF/fragments/header/footer.jsp"/>
</body>
</html>
