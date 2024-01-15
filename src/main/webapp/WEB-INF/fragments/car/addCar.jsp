<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>
<fmt:message bundle="${naming}" key="table.label.cars" var="cars"/>
<fmt:message bundle="${naming}" key="table.label.carNumber" var="carId"/>
<fmt:message bundle="${naming}" key="table.label.type" var="type"/>
<fmt:message bundle="${naming}" key="button.label.add" var="add"/>
<fmt:message bundle="${naming}" key="button.label.cancel" var="cancel"/>
<fmt:message bundle="${naming}" key="car.label.car" var="car"/>
<fmt:message bundle="${naming}" key="car.label.business" var="business"/>
<fmt:message bundle="${naming}" key="car.label.deluxe" var="deluxe"/>
<fmt:message bundle="${naming}" key="car.label.duplex" var="duplex"/>
<fmt:message bundle="${naming}" key="car.label.familyCar" var="familyCar"/>
<fmt:message bundle="${naming}" key="car.label.standard" var="standard"/>
<fmt:message bundle="${naming}" key="car.label.president" var="president"/>


<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/modalStyle.css">
    <script src="${pageContext.request.contextPath}/js/addCar.js"></script>
</head>
<body>
<div id="addCar" class="modal">

    <div class="modal-content animate">
        <form action="${pageContext.servletContext.contextPath}/controller?command=saveCar" method="post">
            <label for="addCarNumber"><b>${carId}</b></label>
            <input type="text" id="addCarNumber" name="addCarNumber"
                   pattern="^([A-Za-z1-9]{1}[A-Za-z0-9]{0,9})$" required>
            <label for="addTypeCar"><b>${type}</b></label>
            <select id="addTypeCar" name="addTypeCar" required>
                <option selected disabled>${type}</option>
                <option value="car">${car}</option>
                <option value="Business">${business}</option>
                <option value="Deluxe">${deluxe}</option>
                <option value="Duplex">${duplex}</option>
                <option value="FamilyCar">${familyCar}</option>
                <option value="Standard">${standard}</option>
                <option value="President">${president}</option>
            </select>
            <div>
                <input class="prcButton" type="submit" value="${add}"/>
            </div>
        </form>
        <div>
            <button class="cancelButton" onclick="document.getElementById('addCar').style.display='none'">${cancel}
            </button>
        </div>
    </div>
</div>

<%--<jsp:include page="/WEB-INF/fragments/car/addCarNotify.jsp"/>--%>
</body>
</html>