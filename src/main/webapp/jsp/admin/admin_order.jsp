<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="username" uri="/WEB-INF/tld/usernameDescriptor.tld"%>
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

<html>
<head>
<fmt:message key="admin.order_page.heading" var="order_page_admin_head" />
  <fmt:message key="main.title" var="main_title"/>
<title>${order_page_admin_head}::::${main_title}</title>
</head>
<body>

	<div class="main-content">
		<div class="head-content">
			<tags:language curr_lang="${locale}"
				curr_uri="${pageContext.request.requestURI}" />
			<tags:logout userEmail="${user.email}" curr_lang="${locale}" />
			${order_page_admin_head}
		</div>

		<fmt:message key="user.order_page.order_details" var="order_details" />
		<fmt:message key="user.order_page.order_id" var="order_id" />
		<fmt:message key="admin.order_page.user_id" var="user_id" />
		<fmt:message key="user.order_page.dates" var="dates" />
		<fmt:message key="user.order_page.dates.1" var="dates_from" />
		<fmt:message key="user.order_page.dates.2" var="dates_to" />
		<fmt:message key="user.order_page.status" var="status" />
		<fmt:message key="user.order.price" var="price" />
		<fmt:message key="user.order.carclass" var="carclass" />
		<fmt:message key="user.order.car" var="car" />
		<fmt:message key="order.form.passdata" var="passdata" />
		<fmt:message key="order.form.driver" var="driver" />
		
		<c:choose>
			<c:when test="${not empty isAdmin || not empty isManager}">
				<div class="form">
					<h3>${order_details}:</h3> <br> 
					<b>${order_id}</b>: ${order.order_id}<br>
					<b>${user_id}</b>: ${order.user_id}<br> 
					<b>${dates}</b>: ${order.date_begin} --> ${order.date_end}<br>
					<b>${carclass}</b>: ${order.carclass}<br>
					<b>${price}</b>: ${order.price}<br>
					<b>${car}</b>: ${order.car_id}<br>
					<b>${passdata}</b>: ${order.passport_data}<br>
					<b>${driver}</b>: ${order.driverNeeded}<br>
					<b>${status}</b>: <tags:status order_status="${order.orderStatus}" curr_lang="${locale}" />
					<br><br>
<%-- REQUESTED --%>
					<c:choose>
						<c:when test="${order.orderStatus == 'REQUESTED'}">
							<fmt:message key="admin.order_page.requested.find_car" var="find" />
							<form action="search_car" method="post">
								<input class="button" type="submit" value="${find}"> <input
									type="hidden" name="actionName" value="find_car">
							</form><br>
						</c:when>
<%-- PENDING  --%>		<c:when test="${order.orderStatus == 'PENDING'}">
							<fmt:message key="admin.order_page.pending.find_an_car"
								var="find_another" />
							<fmt:message key="admin.order_page.ordered_car_id"
								var="ordered_car" />
                			<%-- ${ordered_car}: ${order.orderedCarId} --%>
                		<form action="search_car" method="post">
								<input type="submit" value="${find_another}"> <input
									type="hidden" name="actionName" value="find_car">
							</form>
						</c:when>
<%-- APPROVED --%>
						<c:when test="${order.orderStatus == 'APPROVED'}">
							<fmt:message key="admin.order_page.car_id" var="car_id" />
							<fmt:message key="admin.order_page.approved.bill" var="bill_info" />
							<!-- temp changed for car_id -->
							<!--  ${car_id}: ${order.orderedCarId} -->
                			${car_id}: ${order.car_id}
                			<br>
                			${bill_info}
            			</c:when>
<%-- PAID  --%>
						<c:when test="${order.orderStatus == 'PAID'}">
							<fmt:message key="admin.order.addit_info" var="add_info" />
                			${add_info}: ${order.additionalInfo}
                <br>
							<fmt:message key="order.admin.paid.add_info" var="add_order_info" />
							<fmt:message key="admin.order_page.paid_info" var="paid_info" />
                			${paid_info}
                <br>
							<br>
							<form action="admin_order" method="post">
								<textarea name="additional_info" rows="4" cols="50"></textarea>
								<br> <input type="hidden" name="actionName"
									value="add_info"> <input type="submit"
									value="${add_order_info}">
							</form>
						</c:when>
<%-- -- DECLINED  --%>						
						<c:when test="${order.orderStatus == 'DECLINED'}">
							<fmt:message key="admin.order_page.declined_info"
								var="declined_info" />
                		${declined_info}
            </c:when>

					</c:choose>
					<c:if test="${not empty error}">
						<fmt:message key="orders.error" var="error_msg" />
            ${error_msg}
        </c:if>
         
<%-- DECLINED  --%>       
					<c:if test="${order.orderStatus != 'DECLINED'}">
						<fmt:message key="user.order_page.decline" var="decline_order" />
						<form action="decline_warning" method="post">
							<input type="hidden" name="order_id" value="${order.order_id}">
							<input type="submit" value="${decline_order}"> <input
								type="hidden" name="actionName" value="decline">
						</form>
						<br>
					</c:if>
					<fmt:message key="ref.my_page" var="my_page" />


					<form action="admin_order" method="post">
						<fmt:message key="user.order_page.leave_comment_info"
							var="comment_leave" />
						<fmt:message key="user.order_page.leave_comment"
							var="comment_submit" />
						<%--${comment_leave}: <br>--%>
						<textarea name="comment" rows="4" cols="50"></textarea>
						<input type="hidden" name="actionName" value="send_comment">
						<br> <input type="submit" value="${comment_submit}">
					</form>
					<br>

					<fmt:message key="user.order_page.comments" var="comments" />
					<div class="comments">${comments}:</div>
					<c:choose>
						<c:when test="${not empty no_comments}">
							<fmt:message key="user.order_page.no_comments" var="no_comm" />
            ${no_comm}
        </c:when>
						<c:otherwise>
							<c:forEach items="${comment_list}" var="comment">
								<username:usernameTagHandler user_id="${comment.user_id}"
									lang="${locale}" />: ${comment.text} <br>
							</c:forEach>
				</div>
				<div class="paging">
					<c:if test="${currentPage != 1}">
						<fmt:message key="page.previous" var="previous" />
						<td><a href="admin_order?page=${currentPage - 1}">${previous}</a></td>
					</c:if>

					<%--For displaying Page numbers.
            The when condition does not display a link for the current page --%>
					<table border="1" cellpadding="3" cellspacing="5">
						<tr>
							<c:forEach begin="1" end="${noOfPages}" var="i">
								<c:choose>
									<c:when test="${currentPage eq i}">
										<td>${i}</td>
									</c:when>
									<c:otherwise>
										<td><a href="admin_order?page=${i}">${i}</a></td>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</tr>
					</table>

					<%--For displaying Next link --%>
					<c:if test="${currentPage lt noOfPages}">
						<fmt:message key="page.next" var="next" />
						<td><a href="admin_order?page=${currentPage + 1}">${next}</a></td>
					</c:if>
				</div>

				</c:otherwise>
		</c:choose>
		<div class="paging">
			<h4>
				<a href=<c:url value="/admin"/>>${my_page}</a>
			</h4>
		</div>
		</c:when>
		<c:otherwise>
			<fmt:message key="ref.my_page" var="my_page1" />
			<fmt:message key="auth.user_admin.error" var="auth_admin_err" />
        ${auth_admin_err}
    <h4>
				<a class="paging" href=<c:url value="/user"/>>${my_page1}</a>
			</h4>
		</c:otherwise>
		</c:choose>
		<jsp:include page="/WEB-INF/fragments/header/footer.jsp" />
	</div>
</body>
</html>
