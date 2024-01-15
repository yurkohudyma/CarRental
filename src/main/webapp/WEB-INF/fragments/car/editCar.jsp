<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>
<fmt:message bundle="${naming}" key="table.label.cars" var="cars"/>
<fmt:message bundle="${naming}" key="table.label.carNumber" var="carId"/>
<fmt:message bundle="${naming}" key="table.label.type" var="type"/>
<fmt:message bundle="${naming}" key="button.label.add" var="add"/>
<fmt:message bundle="${naming}" key="button.label.edit" var="edit"/>
<fmt:message bundle="${naming}" key="button.label.cancel" var="cancel"/>
<fmt:message bundle="${naming}" key="car.label.car" var="car"/>
<fmt:message bundle="${naming}" key="car.label.familyCar" var="familyCar"/>
<fmt:message bundle="${naming}" key="car.label.standard" var="standard"/>
<fmt:message bundle="${naming}" key="car.label.president" var="president"/>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/modalStyle.css">
    <script src="${pageContext.request.contextPath}/js/edit.js"></script>
    <script src="${pageContext.request.contextPath}/js/editCar.js"></script>
</head>
<body>

<div id="editCar" class="modal">


    <div class="modal-content animate">
        <form action="${pageContext.servletContext.contextPath}/controller?command=saveCar"
              method="post">
            <input type="hidden" id="carId" name="carId"
                   pattern="^([1-9]{1}[0-9]{0,10})$" value="">

            <label for="editCarNumber"><b>${carId}</b></label>
            <input type="text" id="editCarNumber" name="editCarNumber" value=""
                   pattern="^([A-Za-z1-9]{1}[A-Za-z0-9]{0,9})$" required>

            <label for="editTypeCar"><b>${type}</b></label>
            <select id="editTypeCar" name="editTypeCar" value="" required>
                <option disabled>${type}</option>
                <option value="MINI">${car}</option>
                <option value="ECONOMY">${standard}</option>
                <option value="SUV">4x4</option>
                <option value="ESTATE">${familyCar}</option>
                <option value="PREMIUM">${president}</option>
                </select>
            <div>
                <input class="button" type="submit" value="${edit}"/>
            </div>
        </form>
        <button class="cancelButton" onclick="document.getElementById('editCar').style.display='none'">${cancel}
        </button>
    </div>
</div>

</div>
</body>
