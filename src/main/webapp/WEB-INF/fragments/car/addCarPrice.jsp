<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>
<fmt:message bundle="${naming}" key="table.label.cars" var="cars"/>
<fmt:message bundle="${naming}" key="table.label.carNumber" var="carId"/>
<fmt:message bundle="${naming}" key="table.label.type" var="type"/>
<fmt:message bundle="${naming}" key="button.label.add" var="add"/>
<fmt:message bundle="${naming}" key="table.label.endDate" var="endDate"/>
<fmt:message bundle="${naming}" key="table.label.startDate" var="startDate"/>
<fmt:message bundle="${naming}" key="table.label.enterPrice" var="enterPrice"/>
<fmt:message bundle="${naming}" key="button.label.cancel" var="cancel"/>
<fmt:message bundle="${naming}" key="table.label.price" var="price"/>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/modalStyle.css">
    <script src="${pageContext.request.contextPath}/js/addCarPrice.js"></script>
</head>
<body>
<div id="addCarPrice" class="modal">

    <div class="modal-content animate">
        <form action="${pageContext.servletContext.contextPath}/controller?command=addCarPrice&limit=${requestScope.carLimit}&carPage=${requestScope.carPage}"
              method="post">
            <label for="carId"><b>${carId}</b></label>
            <input id="carIdHidden" name="carId" type="hidden" value=""
                   pattern="^([1-9]{1}[0-9]{0,10})$">
            <input id="carId" name="carId" type="text" disabled value=""
                   pattern="^([1-9]{1}[0-9]{0,10})$">
            <label for="startDate"><b>${startDate}</b></label>
            <input id="startDate" type="date" name="startDate" required>

            <label for="endDate"><b>${endDate}</b></label>
            <input id="endDate" type="date" name="endDate" placeholder="dd" required>
            <label for="price"><b>${price}</b></label>
            <input id="price" type="text" name="price" placeholder="${enterPrice}"
                   pattern="^([1-9]{1}[0-9]{0,8}\.?[0-9]{0,2})$" required>

            <div>
                <input class="prcButton" type="submit" value="${add}"/>
            </div>
        </form>
        <div>
            <button class="cancelButton"
                    onclick="document.getElementById('addCarPrice').style.display='none'">${cancel}
            </button>
        </div>
    </div>

</div>
</body>
</html>