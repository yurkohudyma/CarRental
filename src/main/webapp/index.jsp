<%-- 

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/fragments/header/favicon.jsp" %>


<!-- here and hereinafter checks for proper language assignment --> 
<c:choose>
	<c:when test="${locale == 'uk'}">
		<fmt:setLocale value="uk" />
	</c:when>
	<c:otherwise>
		<fmt:setLocale value="en" />
	</c:otherwise>
</c:choose>

<fmt:setBundle basename="local" />


--%>
<html>
<head>
<%-- 
	<!--  <link rel="icon" href="img/favicon.ico" type="image/x-icon"> -->
	<fmt:message key="main.title" var="main_title" />
	<title>${main_title}</title>
	--%>
</head>
<body>
<% response.sendRedirect("/authentification"); %>
<%--

	<form action="" method="post">
		<fmt:message key="local.local_button.name.uk" var="uk_button" />
		<input type="hidden" name="loc" value="uk" /> <input type="submit"
			value="${uk_button}" />
	</form>

	<form action="" method="post">
        <fmt:message key="local.local_button.name.en" var="en_button"/>
        <input type="hidden" name="loc" value="en"/>
        <input type="submit" value="${en_button}"/>
        <br>
    </form>

	<fmt:message key="local.welcome" var="welcome"/>
    <h2>${welcome}</h2>
    <fmt:message key="local.login_ref" var="login_ref"/>
    >> <a href=<c:url value="/authentification"/>>${login_ref}</a>
    <br>
    >> <fmt:message key="local.registration_ref" var="reg_ref"/>
    <a href=<c:url value="/registration"/>>${reg_ref}</a>

<jsp:include page="/WEB-INF/fragments/header/footer.jsp"/>
--%>    
</body>
</html>
