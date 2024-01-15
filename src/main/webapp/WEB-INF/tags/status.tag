<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="order_status" type="ua.hudyma.constants.OrderStatus" required="true" %>
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

<c:if test="${order_status=='DECLINED'}">
    <fmt:message key="order.status.declined" var="declined"/>
    ${declined}
</c:if>
<c:if test="${order_status=='REQUESTED'}">
    <fmt:message key="order.status.requested" var="requested"/>
    ${requested}
</c:if>
<c:if test="${order_status=='PENDING'}">
    <fmt:message key="order.status.pending" var="pending"/>
    ${pending}
</c:if>
<c:if test="${order_status=='APPROVED'}">
    <fmt:message key="order.status.approved" var="approved"/>
    ${approved}
</c:if>
<c:if test="${order_status=='PAID'}">
    <fmt:message key="order.status.paid" var="paid"/>
    ${paid}
</c:if>