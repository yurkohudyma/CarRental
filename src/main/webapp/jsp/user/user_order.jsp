<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/usernameDescriptor.tld" prefix="username"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ include file="/WEB-INF/fragments/header/favicon.jsp" %> 
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
<fmt:message key="user.order_page.heading" var="op_heading" />
  <fmt:message key="main.title" var="main_title"/>
<title>${op_heading}::${main_title}</title>
<link type="text/css" rel="stylesheet" href="/css/main.css">
</head>
<body>
	<div class="main-content">
		<div class="head-content">
			<tags:language curr_lang="${locale}"
				curr_uri="${pageContext.request.requestURI}" />
			<tags:logout userEmail="${user.email}" curr_lang="${locale}"/>
			${op_heading}
		</div>

		<c:choose>
			<c:when test="${user.user_id==order.user_id}">

				<div class="form">
					<fmt:message key="user.order_page.order_details" var="order_details" />
					<fmt:message key="user.order_page.order_id" var="order_id" />
					<h3>${order_details}</h3> 
					<b>${order_id}</b>: ${order.order_id}
					<br><fmt:message key="user.order_page.dates" var="dates" />
					<fmt:message key="user.order_page.dates.1" var="dates_from" />
					<fmt:message key="user.order_page.dates.2" var="dates_to" />
					<b>${dates}</b>: ${order.date_begin} --> ${order.date_end} <br>
					<fmt:message key="user.order_page.status" var="status" />
					<b>${status}</b>:
					<tags:status order_status="${order.orderStatus}" curr_lang="${locale}" />
					<br>
					<fmt:message key="user.order_page.view_car_details"
						var="car_details" />
					<c:choose>
						<c:when test="${order.orderStatus == 'REQUESTED'}">
							<fmt:message key="user.order_page.requested_info"
								var="requested_info" />${requested_info}
            </c:when>
						<c:when test="${order.orderStatus == 'PENDING'}">
							<fmt:message key="user.order_page.ordered_car_id"
								var="ordered_car_id" />
                		<b>${ordered_car_id}</b>: ${order.car_id}

                <h4>

<%-- CAR DETAILS BUTTON, FWD to OrderUserServlet --%>
								<a href=<c:url value="/car/${order.car_id}"/>>${car_details}</a>
							</h4>

							<form action="user_order" method="get">
								<fmt:message key="user.order_page.approve" var="approve_order" />
								<input type="submit" value="${approve_order}"> <input
									type="hidden" name="order_id" value="${order.order_id}">
								<input type="hidden" name="actionName" value="approve">
							</form>
						</c:when>
						<c:when test="${order.orderStatus == 'APPROVED'}">
							<fmt:message key="user.order_page.bill_info" var="bill_info" />
                ${bill_info}<br>
							<fmt:message key="user.order_page.bill.1" var="bill1" />
							<fmt:message key="user.order_page.bill.2" var="bill2" />
							<fmt:message key="user.order_page.bill.3" var="bill3" />
							<fmt:message key="user.order_page.bill.4" var="bill4" />
                ${bill1} №${car.car_id} ${bill2} ${order.date_begin} ${bill3} ${order.date_end} ${bill4} ${order.price}$<br>
							<fmt:message key="user.order_page.pay_info" var="pay_info" />
                ${pay_info}:
                <form action="bill" method="get">
								<fmt:message key="user.order_page.pay" var="pay" />
								<input type="submit" value="${pay}">
							</form>
							<h4>
								<a href=<c:url value="/car/${order.car_id}"/>>${car_details}</a>
							</h4>
						</c:when>
						<c:when test="${order.orderStatus == 'PAID'}">
							<fmt:message key="user.order_page.paid_info" var="paid_info" />
                <br><b><font color = "green">${paid_info}</font></b>
                <h4>
								<a href=<c:url value="/car/${order.car_id}"/>>${car_details}</a>
							</h4>

						</c:when>
						<c:when test="${order.orderStatus == 'DECLINED'}">
							<fmt:message key="user.order_page.declined_info"
								var="declined_info" />
                ${declined_info}
            </c:when>
					</c:choose>
					<c:if test="${not empty error}">
						<fmt:message key="orders.error" var="error_msg" />
            ${error_msg}
        </c:if>
					<c:if
						test="${order.orderStatus != 'DECLINED' and order.orderStatus != 'PAID' and order.orderStatus != 'APPROVED'}">
						<form action="edit_order" method="post">
							<fmt:message key="user.order_page.edit" var="edit_order" />
							<input type="submit" value="${edit_order}"> <input
								type="hidden" name="actionName" value="editOrder">
						</form>
					</c:if>

					<c:if test="${order.orderStatus != 'DECLINED'}">
						<form action="decline_warning" method="post">
							<fmt:message key="user.order_page.decline" var="decline_order" />
							<input type="submit" value="${decline_order}"> <input
								type="hidden" name="actionName" value="decline">
						</form>
					</c:if>

					<fmt:message key="user.order_page.comments_info"
						var="comments_info" />
					<%--${comments_info}--%>

					<form action="user_order" method="post">
						<fmt:message key="user.order_page.leave_comment_info"
							var="leave_comment_info" />


						<br>
						<textarea name="comment" rows="4" cols="50"></textarea>
						<br> <input type="hidden" name="actionName"
							value="send_comment">
						<fmt:message key="user.order_page.leave_comment"
							var="leave_comment" />
						<input type="submit" value="${leave_comment}">
					</form>
					<br> <br>
					<fmt:message key="user.order_page.comments" var="comments" />
					${comments}: <br>
					<c:if test="${not empty no_comments}">
						<fmt:message key="user.order_page.no_comments" var="no_comm" />
            ${no_comm}
        </c:if>
					<c:forEach items="${comment_list}" var="comment">
						<username:usernameTagHandler user_id="${comment.user_id}"
							lang="${locale}" />: ${comment.text} <br>
					</c:forEach>
					<br>
					<div class="paging">
						<c:if test="${currentPage != 1}">
							<fmt:message key="page.previous" var="previous" />
							<td><a href="user_order?page=${currentPage - 1}">${previous}</a></td>
						</c:if>

						<%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
						<table border="1" cellpadding="3" cellspacing="5">
							<tr>
								<c:forEach begin="1" end="${noOfPages}" var="i">
									<c:choose>
										<c:when test="${currentPage eq i}">
											<td>${i}</td>
										</c:when>
										<c:otherwise>
											<td><a href="user_order?page=${i}">${i}</a></td>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</tr>
						</table>

						<%--For displaying Next link --%>
						<c:if test="${currentPage lt noOfPages}">
							<fmt:message key="page.next" var="next" />
							<td><a href="user_order?page=${currentPage + 1}">${next}</a></td>
						</c:if>
						<fmt:message key="ref.my_page" var="my_page" />
						<h4>
							<a href=<c:url value="/user"/>>${my_page}</a>
						</h4>
					</div>
			</c:when>
			<c:otherwise>
				<div class="warning">
					<fmt:message key="ref.my_page" var="my_page1" />
					<fmt:message key="auth.user_admin.error" var="auth_admin_err" />
					${auth_admin_err}
					<c:if test="${user.access_level=='ADMIN'}">
						<h4>
							<a href=<c:url value="/admin"/>>${my_page1}</a>
						</h4>
					</c:if>
					<c:if test="${user.access_level=='USER'}">
						<h4>
							<a href=<c:url value="/user"/>>${my_page1}</a>
						</h4>
					</c:if>
				</div>
	</div>
	</div>
	</c:otherwise>
	</c:choose>
	<jsp:include page="/WEB-INF/fragments/header/footer.jsp" />
</body>
</html>
