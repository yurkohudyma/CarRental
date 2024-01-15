
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ include file="/WEB-INF/fragments/header/favicon.jsp"%>
<c:choose>
	<c:when test="${locale == 'uk'}">
		<fmt:setLocale value="uk" />
	</c:when>
	<c:otherwise>
		<fmt:setLocale value="en" />
	</c:otherwise>
</c:choose>

<fmt:setBundle basename="local" />

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<fmt:message key="user.header" var="user_header" />
  <fmt:message key="main.title" var="main_title"/>
<title>${user_header}::${main_title}</title>
<link type="text/css" rel="stylesheet" href="css/main.css">

</head>
<body>
	<div class="main-content">
		<div class="head-content">
			<tags:language curr_lang="${locale}"
				curr_uri="${pageContext.request.requestURI}" />
			<tags:logout userEmail="${user.email}" curr_lang="${locale}" />

			<fmt:message key="user.new_order" var="new_order" />
			<form action="" method="post">
				<fmt:message key="user.new_order.button" var="new_order_button" />
				<!-- this keypressed leads to UserServlet -->
				<input type="hidden" name="actionName" value="newOrder"> <input
					class="button" type="submit" value="${new_order_button}"> <br>
			</form>
		</div>

		<div class="cars-table">
			<div style="display: flex; width: 100%;">
				<div style="width: 50%;">

					<fmt:message key="user.my_orders" var="my_orders" />
					<p>${my_orders}:</p>
				</div>
			</div>
			<c:choose>
				<c:when test="${empty no_result}">
					<table class="cars">
						<tbody>
							<tr>
								<fmt:message key="user.order.name" var="name" />
								<th>${name}</th>
								<fmt:message key="user.order.car" var="car" />
								<th>${car}</th>
								<fmt:message key="user.order.class" var="carclass" />
								<th>${carclass}</th>
								<fmt:message key="user.order.date_in" var="order_date_in" />
								<th>${order_date_in}</th>
								<fmt:message key="user.order.date_out" var="order_date_out" />
								<th>${order_date_out}</th>
								<fmt:message key="user.order.price" var="price" />
								<th>${price}</th>
								<fmt:message key="user.order.status" var="status" />
								<th>${status}</th>
								<fmt:message key="admin.order.passport_data" var="passdata" />
								<th>${passdata}</th>
								<fmt:message key="user.order.driver_needed" var="driver_needed" />
								<th>${driver_needed}</th>


								<th></th>
								<th></th>
							</tr>
							<c:forEach items="${new_orders_list}" var="order">
								<tr>

									<td>${order.name}</td>
									<td>${order.car_id}</td>
									<td>${order.carclass}</td>
									<td>${order.date_begin}</td>
									<td>${order.date_end}</td>
									<td><c:choose>
											<c:when test="${order.price != 0}">
                           						 ${order.price}$
                        					</c:when>
										</c:choose></td>
									<td><tags:status order_status="${order.orderStatus}"
											curr_lang="${locale}" /></td>
									<td>${order.passport_data}</td>
									<td>${order.driverNeeded}</td>

									<td>
										<form action="user_order" method="get">
											<fmt:message key="user.order.details" var="details" />
											<input type="hidden" name="order_id"
												value="${order.order_id}"> <input type="submit"
												value="${details}">
										</form>
									</td>
								</tr>
							</c:forEach>
							<c:if test="${not empty old_orders_list}">
								<tr>
									<br>
									<fmt:message key="user.old_orders" var="old_orders" />
									<th>${old_orders}</th>
								</tr>
							</c:if>
							<c:forEach items="${old_orders_list}" var="order">
								<tr>
									<td>${order.name}</td>
									<td>${order.car_id}</td>
									<td>${order.carclass}</td>
									<td>${order.date_begin}</td>
									<td>${order.date_end}</td>

									<td><c:choose>
											<c:when test="${order.price != 0}">
                                			${order.price}$
                            				</c:when>

										</c:choose></td>
									<td><tags:status order_status="${order.orderStatus}"
											curr_lang="${locale}" /></td>
									<td>${order.passport_data}</td>
									<td>${order.driverNeeded}</td>
									<td>
										<form action="user_order" method="get">
											<fmt:message key="user.order.details" var="detail" />
											<input type="hidden" name="order_id"
												value="${order.order_id}"> <input type="submit"
												value="${detail}">
										</form>
									</td>
									<td>
										<form action="remove_warning" method="post">
											<input type="hidden" name="order_id"
												value="${order.order_id}">
											<fmt:message key="user.order.remove" var="remove" />
											<input type="submit" value="${remove}"> <input
												type="hidden" name="actionName" value="remove_order">
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="paging">

						<c:if test="${currentPage != 1}">
							<fmt:message key="page.previous" var="previous" />
							<td><a href="?page=${currentPage - 1}">${previous}</a></td>
						</c:if>

						<%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
						<table border="1" cellpadding="3" cellspacing="5">
							<tbody>
								<tr>
									<c:forEach begin="1" end="${noOfPages}" var="i">
										<c:choose>
											<c:when test="${currentPage eq i}">
												<td>${i}</td>
											</c:when>
											<c:otherwise>
												<td><a href="?page=${i}">${i}</a></td>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</tr>
							</tbody>
						</table>

						<%--For displaying Next link --%>
						<c:if test="${currentPage lt noOfPages}">
							<fmt:message key="page.next" var="next" />
							<td><a href="?page=${currentPage + 1}">${next}</a></td>
						</c:if>
				</c:when>
				<c:otherwise>
					<fmt:message key="user.orders.no_orders" var="no_result" />
                ${no_result}
            </c:otherwise>
			</c:choose>

			<c:if test="${not empty isAdmin || not empty isManager}">
				<fmt:message key="user.user_admin.user_page" var="as_admin" />
				<h4>
					<a class="paging" href=<c:url value="/admin"/>>${as_admin}</a>
				</h4>
			</c:if>
		</div>
	</div>
	<c:if test="${not empty error}">
		<fmt:message key="orders.error" var="orders_list_error" />
        ${orders_list_error}
    </c:if>
	</div>
	<jsp:include page="/WEB-INF/fragments/header/footer.jsp" />
</body>
</html>
