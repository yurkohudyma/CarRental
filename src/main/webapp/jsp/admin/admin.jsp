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

 <fmt:message key="main.title" var="main_title"/>
 <c:choose>
		<c:when test="${not empty isManager}">
			<fmt:message key="admin.heading_manager" var="manager_header" />
			<title>${manager_header}::::${main_title}</title>
		</c:when>
	<c:otherwise>
			<fmt:message key="admin.heading_admin" var="admin_header" />
			<title>${admin_header}::::${main_title}</title>
	</c:otherwise>
</c:choose>
<link type="text/css" rel="stylesheet" href="css/main.css">
</head>
<body>
	<c:choose>
		<c:when test="${not empty isAdmin || not empty isManager}">
			<div class="main-content">
				<div class="head-content">
					<tags:language curr_lang="${locale}" curr_uri="${pageContext.request.requestURI}" />
					<tags:logout userEmail="${user.email}" curr_lang="${locale}" />
					
<%-- LIST OF USERS BUTTON, INVOKING UserListServlet --%>
					<form action="users_list" method="post">
						${admin_header} <input type="hidden" name="actionName"
							value="getUsers">
						<fmt:message key="admin.users" var="users_list" />
						<input class="button" type="submit" value="${users_list}">
					</form>
					<br>
<%-- BUTTONS WITH ORDER STATUSES RETRIEVAL --%>
					<table class="order_tabs_pane">
						<tr>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="all">
									<fmt:message key="admin.orders.heading.all" var="all_orders" />
									<input class="button" type="submit" value="${all_orders}">
								</form>
							</td>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="requested">
									<fmt:message key="admin.orders.heading.requested"
										var="requested_orders" />
									<input class="button" type="submit" value="${requested_orders}">
								</form>
							</td>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="pending">
									<fmt:message key="admin.orders.heading.pending"
										var="pending_orders" />
									<input class="button" type="submit" value="${pending_orders}">
								</form>
							</td>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="approved">
									<fmt:message key="admin.orders.heading.approved"
										var="approved_orders" />
									<input class="button" type="submit" value="${approved_orders}">
								</form>
							</td>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="paid">
									<fmt:message key="admin.orders.heading.paid" var="paid_orders" />
									<input class="button" type="submit" value="${paid_orders}">
								</form>
							</td>
							<td>
								<form action="" method="post">
									<input type="hidden" name="actionName" value="declined">
									<fmt:message key="admin.orders.heading.declined"
										var="declined_orders" />
									<input class="button" type="submit" value="${declined_orders}">
								</form>
							</td>
							<td></td>
						</tr>
					</table>
				</div>
<%-- TABLE WITH ORDERS --%>
				<div class="cars-table">
					<h4>${heading}</h4>
					<c:choose>
						<c:when test="${empty no_result}">
							<table class="cars">
								<tbody>
									<tr>
										<th><fmt:message key="user.order.name" var="name" />
											${name}</th>
										<th><fmt:message key="admin.order.user_id" var="user_id" />
											${user_id}</th>
										<th><fmt:message key="user.order.date_in" var="date_in" />
											${date_in}</th>
										<th><fmt:message key="user.order.date_out" var="date_out" />
											${date_out}</th>
										<th><fmt:message key="admin.order.car_id" var="car_id" />
											${car_id}</th>
										<th><fmt:message key="user.order.carclass" var="carclass" />
											${carclass}</th>
										<th><fmt:message key="admin.order.addit_info"
												var="add_info" /> ${add_info}</th>
										<th><fmt:message key="user.order.status" var="status" />
											${status}</th>
										<th><fmt:message key="user.order.price" var="price" />
											${price}</th>
										<th><fmt:message key="admin.order.passport_data" var="passport" />
											${passport}</th>
										<th><fmt:message key="user.order.driver_needed" var="driver" />
											${driver}</th>
										</tr>
									<c:forEach items="${orders_list}" var="order">
										<tr>
											<td>${order.name}</td>
											<td>${order.user_id}</td>
											<td>${order.date_begin}</td>
											<td>${order.date_end}</td>
											<!--  ${order.orderedCarId } -->
											<td>${order.car_id}</td>
											<td>${order.carclass}</td>
											<td>${order.additionalInfo}</td>
											<td><tags:status order_status="${order.orderStatus}"
													curr_lang="${locale}" /></td>
											<td><c:choose>
													<c:when test="${order.price != 0}">
                                        						${order.price}$
                                    				</c:when>
													<c:otherwise>
													</c:otherwise>
												</c:choose></td>
												<td>${order.passport_data}</td>
												<td>${order.driverNeeded}</td>
												
											<td>
											
<%-- Order details BUTTON, redirects to OrderAdminServlet --%>
											
												<form action="admin_order" method="get">
													<input type="hidden" name="order_id" value="${order.order_id}">
													<fmt:message key="user.order.details" var="details" />
													<input type="submit" value="${details}">
												</form>
											</td>
											
<%-- if Orderstatus is DECLINED, SHOW REMOVE Order BUTTON --%>

											<c:if test="${order.orderStatus=='DECLINED'}">
												<td>
													<form action="remove_warning" method="post">
														<input type="hidden" name="order_id" value="${order.order_id}">
														<fmt:message key="user.order.remove" var="remove" />
														<input type="submit" value="${remove}"> 
														<input type="hidden" name="actionName" value="remove_order">
													</form>
												</td>
											</c:if>

										</tr>
									</c:forEach>
								</tbody>
							</table>

							<div class="paging">
								<c:if test="${currentPage != 1}">
									<fmt:message key="page.previous" var="previous" />
									<td><a href="?page=${currentPage - 1}">${previous}</a></td>
								</c:if>

<%--For displaying Page numbers. The condition does not display a link for the current page--%>
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
							<fmt:message key="admin.orders.no_orders" var="no_orders" />
						</c:otherwise>
					</c:choose>
					<c:if test="${not empty error}">
						<fmt:message key="orders.error" var="orders_search_error" />
                        ${orders_search_error}
                    </c:if>
					<fmt:message key="ref.my_user_page" var="my_user_page" />
					<h4>
						<a href=<c:url value="/user"/>>${my_user_page}</a>
					</h4>

				</div>
			</div>
			</div>
		</c:when>

		<c:otherwise>
			<fmt:message key="auth.user_admin.error" var="auth_error" />
        ${auth_error}
        <fmt:message key="ref.my_page" var="my_page" />
			<h4>
				<a class="paging" href=<c:url value="/user"/>>${my_page}</a>
			</h4>
		</c:otherwise>
	</c:choose>
	<jsp:include page="/WEB-INF/fragments/header/footer.jsp" />
</body>
</html>
