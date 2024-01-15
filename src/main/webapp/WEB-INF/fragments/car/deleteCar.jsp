<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="naming" var="naming"/>
<fmt:message bundle="${naming}" key="car.label.deleteCar" var="deleteCar"/>
<fmt:message bundle="${naming}" key="button.label.no" var="no"/>
<fmt:message bundle="${naming}" key="button.label.yes" var="yes"/>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/notifyStyle.css">
    <script src="${pageContext.request.contextPath}/js/processOrder.js"></script>
</head>
<body>
<div id="deleteProve" class="pay-modal">

    <div class="pay-modal-content animate">
        <form action="${pageContext.request.contextPath}/controller?command=deleteCar" method="post">
            <input name="deletedCarId" id="deletedCarId" type="hidden" value=""/>
            <label class="paymentLabel"><b>${deleteCar}</b></label>
            <div>
                <div class="resultButtons">
                    <button class="yesButton" id="yesBtn" type="submit">${yes}
                    </button>
                </div>
            </div>
        </form>
        <div class="resultButtons">
            <button class="noButton" id="noBtn" type="submit"
                    onclick="document.getElementById('deleteProve').style.display='none'">${no}
            </button>
        </div>
    </div>

</div>
</body>
</html>
