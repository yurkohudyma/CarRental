<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="userEmail" type="java.lang.String" required="true" %>
<%--   <%@ attribute name="userName" type="java.lang.String" %> --%> 

<%@ attribute name="curr_lang" type="java.util.Locale" required="true" %>

<c:choose>
    <c:when test="${curr_lang == 'uk'}">
        <fmt:setLocale value="uk"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="local"/>

<form class="logout" action="<c:url value="/authentification"/>" method="POST">
    <!--  ${userName} --> ${userEmail}
        <fmt:message key="tag.logout" var="logout"/>

    <input class="button" type="submit" value="${logout}">
    <input type="hidden" name="actionName" value="logout">
</form>
