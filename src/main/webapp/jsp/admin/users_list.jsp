<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
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
<fmt:message key="user_list.title" var="user_list_header" />
<title>${user_list_header}</title>
<link type="text/css" rel="stylesheet" href="css/main.css">
</head>
<body>
	<c:choose>
		<c:when test="${not empty isAdmin || not empty isManager}">
			<div class="main-content">
				<div class="head-content">
					${user_list_header}
					<tags:language curr_lang="${locale}"
						curr_uri="${pageContext.request.requestURI}" />
					<tags:logout userEmail="${user.email}" curr_lang="${locale}" />
				</div>
				<c:choose>
					<c:when test="${empty error}">

						<div class="cars-table">
							<table class="cars">
								<tbody>
									<tr>
										<fmt:message key="admin.order.user_id" var="user_id" />
										<fmt:message key="registration.registration_form.email"	var="email" />
										<fmt:message key="user_list.access_level" var="access_level" />
										<fmt:message key="user_list.balance" var="user_balance" />

										<th>${user_id}</th>
										<th>${email}</th>
										<th>${user_balance}</th>
										<th>${access_level}</th>


									</tr>
									<c:forEach items="${users_list}" var="user_var" varStatus="loop">
										<tr>
											<td>${user_var.user_id}</td>
											<td>${user_var.email}</td>
											<td>€${balance[loop.index]}</td>
											<td>${user_var.accesslevel}</td>
<%-- SET ADMIN button. ADMIN status could be set for USER after it's been set to MANAGER --%>
											<td><c:if test="${user_var.accesslevel != 'ADMIN' && user_var.accesslevel =='MANAGER' && empty isManager  }"> 
											<%-- <td><c:if test="${user_var.accesslevel != 'ADMIN'}"> --%>
												<form action="users_list" method="post">
													<input type="hidden" name="user_id" value="${user_var.user_id}">
													<fmt:message key="user_list.set_as_admin" var="set_as_admin" />
													<input class="button" type="submit" value="${set_as_admin}"> 
													<input type="hidden" name="actionName" value="set_admin">
												</form>
											</c:if>
											</td>
											<td>
<%-- SET MANAGER button --%> 
												<c:if test="${user_var.accesslevel !='MANAGER' && user_var.accesslevel != 'ADMIN' && empty isManager && user_var.accesslevel != 'BLOCKED'}">
												<form action="users_list" method="post">
												<input type="hidden" name="user_id" value="${user_var.user_id}">
												<fmt:message key="user_list.set_as_manager" var="set_manager"/>
												<input class="button" type="submit" value="${set_manager}"> 
												<input type="hidden" name="actionName" value="set_manager">
												</form>
											</c:if>
											</td>
											
											<td>
<%-- BLOCK USER button --%>					<c:if test="${user_var.accesslevel == 'USER'||user_var.accesslevel == 'MANAGER' && empty isManager}">
												<c:if test="${user_var.accesslevel != 'BLOCKED'}">
													<form action="users_list" method="post">
													<input type="hidden" name="user_id" value="${user_var.user_id}">
													<fmt:message key="user_list.block_user" var="block_user"/>
													<input class="button" type="submit" value="${block_user}"> 
													<input type="hidden" name="actionName" value="block_user">
													</form>
												</c:if>
											</c:if>
<%-- UNBLOCK USER button --%>					
											<c:if test="${user_var.accesslevel == 'BLOCKED' && not empty isAdmin }">
												<form action="users_list" method="post">
												<input type="hidden" name="user_id" value="${user_var.user_id}">
												<fmt:message key="user_list.unblock_user" var="unblock_user"/>
												<input class="button" type="submit" value="${unblock_user}"> 
												<input type="hidden" name="actionName" value="unblock_user">
												</form>
											</c:if>
											
											</td>
										</tr>
									</c:forEach>
							</table>
						</div>
						<div class="paging">
							<c:if test="${currentPage != 1}">
								<fmt:message key="page.previous" var="previous" />
								<td><a href="users_list?page=${currentPage - 1}">${previous}</a></td>
							</c:if>

							<%--For displaying Page numbers.
                            The when condition does not display a link for the current page--%>
							<table>
								<!--  border="1" cellpadding="3" cellspacing="5" -->
								<tbody>
									<tr>
										<c:forEach begin="1" end="${noOfPages}" var="i">
											<c:choose>
												<c:when test="${currentPage eq i}">
													<td>${i}</td>
												</c:when>
												<c:otherwise>
													<td><a href="users_list?page=${i}">${i}</a></td>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</tr>
								</tbody>
							</table>

							<%--For displaying Next link --%>
							<c:if test="${currentPage lt noOfPages}">
								<fmt:message key="page.next" var="next" />
								<td><a href="users_list?page=${currentPage + 1}">${next}</a></td>
							</c:if>
						</div>
					</c:when>
					<c:otherwise>
						<fmt:message key="orders.error" var="users_list_error" />
                    ${users_list_error}
                </c:otherwise>
				</c:choose>

				<div class="paging">
					<fmt:message key="ref.my_page" var="my_page" />
					<h4>
						<a href=<c:url value="/admin"/>>${my_page}</a>
					</h4>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="form">
				<fmt:message key="auth.user_admin.error" var="auth_error" />
				${auth_error}
				<fmt:message key="ref.my_page" var="my_page" />
				<h4>
					<a href=<c:url value="/user"/>>${my_page}</a>
				</h4>
			</div>
		</c:otherwise>
	</c:choose>
	<jsp:include page="/WEB-INF/fragments/header/footer.jsp" />
</body>
</html>
