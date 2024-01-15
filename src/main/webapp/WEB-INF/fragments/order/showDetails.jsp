<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>
<fmt:message bundle="${naming}" key="table.label.cars" var="cars"/>
<fmt:message bundle="${naming}" key="table.label.carNumber" var="carId"/>
<fmt:message bundle="${naming}" key="table.label.type" var="type"/>
<fmt:message bundle="${naming}" key="button.label.add" var="add"/>
<fmt:message bundle="${naming}" key="table.label.checkInDate" var="checkInDate"/>
<fmt:message bundle="${naming}" key="table.label.checkOutDate" var="checkOutDate"/>
<fmt:message bundle="${naming}" key="table.label.invoiceDate" var="invoiceDate"/>
<fmt:message bundle="${naming}" key="table.label.name" var="name"/>
<fmt:message bundle="${naming}" key="table.label.carNumber" var="carNumber"/>
<fmt:message bundle="${naming}" key="table.label.price" var="price"/>
<fmt:message bundle="${naming}" key="button.label.cancel" var="cancel"/>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/modalStyle.css">
    <script src="${pageContext.request.contextPath}/js/showDetails.js"></script>
</head>
<body>
<div id="showDetails" class="modal">

    <div class="modal-content animate">
        <label for="completeName"><b>${name}</b></label>
        <input type="text" id="completeName" value=""
               readonly>
        <label><b>${checkInDate}</b></label>
        <input type="text" id="completeCheckInDate" name="completeCheckInDate" value=""
               readonly>

        <label><b>${checkOutDate}</b></label>
        <input type="text" id="completeCheckOutDate" name="completeCheckOutDate" value="" readonly>

        <label for="completeCarNumber"><b>${carNumber}</b></label>
        <input id="completeCarNumber" type="text" value="" readonly>

        <label for="completePrice"><b>${price}</b></label>
        <input type="text" id="completePrice" value="" readonly>

        <label><b>${invoiceDate}</b></label>
        <input type="text" id="completeInvoiceDate" value="" readonly>

        <div>
            <button class="cancelButton" type="submit"
                    onclick="document.getElementById('showDetails').style.display='none'">${cancel}
            </button>
        </div>
    </div>
</div>
</body>
</html>
