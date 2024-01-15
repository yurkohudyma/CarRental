<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="curr_lang" type="java.util.Locale" required="true" %>
<%@ attribute name="curr_uri" type="java.lang.String" required="true" %>
<fmt:setBundle basename="local"/>

<c:choose>
    <c:when test="${curr_lang == 'uk'}">
        <fmt:setLocale value="uk"/>
        <fmt:setBundle basename="local"/>
        <form class="logout" action="" method="post">
            <fmt:message key="local.local_button.english" var="en_button"/>
            <input type="hidden" name="actionName" value="change_lang">
            <input type="hidden" name="from" value="${curr_uri}">
            <input type="hidden" name="locale" value="en"/>
            <input class="button" type="submit" value="${en_button}"/>
            <br>
        </form>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en"/>
        <fmt:setBundle basename="local"/>
        <form class="logout" action="" method="post">
            <fmt:message key="local.local_button.ukrainian" var="uk_button"/>
            <input type="hidden" name="actionName" value="change_lang">
            <input type="hidden" name="from" value="${curr_uri}">
            <input type="hidden" name="locale" value="uk"/>
            <input class="button" type="submit" value="${uk_button}"/><br>
        </form>
    </c:otherwise>
</c:choose>