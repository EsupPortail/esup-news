<%@ include file="/WEB-INF/jsp/include.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<c:set var="formName" value="addSuperUser" />

<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"><IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="Manager" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend><fmt:message
	key="menu.addSuperAdm" /> </legend> <c:set var="page"
	value="${empty page ? 0 : page}" /> <c:set var="nextPage"
	value="${page == 2 ? null : page + 1}" /> <c:set var="prevPage"
	value="${page == 0 ? null : page - 1}" /> <c:set var="tit">
	<fmt:message key="news.img.person" />
</c:set> <html:errors path="permForm" fields="true" /> <spring:nestedPath
	path="permForm">
	<form name="${namespace}AddSuperU" method="post"
		action="<portlet:actionURL>
                        <portlet:param name="action" value="addSuperAdm"/>

                        <portlet:param name="_page" value="${page}"/>
                </portlet:actionURL>">
	<table class="adm">
		<c:choose>
			<c:when test="${page == 0}">
				<tr>
					<td colspan="4" class="news_listA"> <p class="portlet-font"><fmt:message
						key="news.label.superAdmin.step1" /></p>
					</td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.research" /></td>
					<td><spring:bind path="isGroup">
						<select name="${status.expression}" size="1">

							<option value="0" selected><fmt:message
								key="news.label.person" /></option>
						</select>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind></td>
					<td align="right" class="portlet-form-label"><fmt:message
						key="news.label.contains" /></td>
					<td><html:input path="token" size="15" maxlength="30" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.role" /></td>
					<td><spring:bind path="role">
						<select name="${status.expression}" size="1">

							<option value="${role}"><fmt:message key="${role}" /></option>

						</select>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind></td>
					<td></td>
					<td></td>
				</tr>

			</c:when>
			<c:when test="${page == 1}">
				<tr>
					<td colspan="4" class="news_listA"> <p class="portlet-font"><fmt:message
						key="news.label.superAdmin.step2" /></p>
					</td>
				</tr>
				<tr>
					<td colspan="4"><c:choose>
						<c:when test="${fn:length(userList) > 0}">
							<spring:bind path="user">
								<table border=0 cellpadding=5 width="100%">
									<tr>
										<td>
										<d:table name="${userList}" id="user" sort="list"
													export="false" class="dataTable" defaultsort="2" defaultorder="ascending"
													pagesize="${nbItemsToShow}"	decorator="org.displaytag.decorator.TotalTableDecorator">
											<d:column title="Select">
												<input type="radio" name="user.userId"
													value="${user.userId}" />
											</d:column>
											<c:forEach items="${attrDisplay}" var="displayAttr">
												<d:column titleKey="news.label.${displayAttr}" sortable="true" headerClass="sortable">
													<c:forEach items="${user.attributes[displayAttr]}"
														var="attrValue">
														<c:out value="${attrValue}" />
													</c:forEach>
												</d:column>
											</c:forEach>
											<d:setProperty name="paging.banner.item_name" value="${tit}" />
											<d:setProperty name="paging.banner.items_name"
												value="${tit}s" />
										</d:table></td>
									</tr>
									<tr>
										<td><c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
										</td>
									</tr>
								</table>
							</spring:bind>
						</c:when>
						<c:otherwise>
							<div>
							<p class="portlet-msg-info"><fmt:message
								key="news.permission.noUserFound" /> <br />
							<fmt:message key="news.permission.msg.goPrevious" /></p>
							</div>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</c:when>
			<c:when test="${page == 2}">
				<tr>
					<td colspan="2" class="news_listA">
					<p class="portlet-font"> <fmt:message
						key="news.label.permission.step3" /></p>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table class="news_TabWb">
						<thead><tr>
							<th class="portlet-font"><fmt:message key="news.label.role" /></th>
							<c:forEach items="${attrDisplay}" var="displayAttr">
								<th class="portlet-font"><fmt:message
									key="news.label.${displayAttr}" /></th>
							</c:forEach>
						</tr>
						</thead>
						<tr>
							<td colspan="${fn:length(attrDisplay) +1}" class="line_a" />
						</tr>
						<tr>
							<td class="portlet-font"><fmt:message key="${permForm.role}" /></td>
							<c:forEach items="${attrDisplay}" var="displayAttr">
								<td class="portlet-font"><c:forEach
									items="${user.attributes[displayAttr]}" var="attrValue">
									<c:out value="${attrValue}" />
								</c:forEach></td>
							</c:forEach>
						</tr>
					</table>
					</td>
				</tr>
			</c:when>
		</c:choose>
		<tr>
			<th colspan="2"><input type="submit" name="_target${nextPage}"
				${empty
				nextPage ? "disabled" : ""} value="<fmt:message key="button.next" />" />
			<input type="submit" name="_finish" ${page !=2
				? "disabled" : ""} value=" <fmt:message key="button.finish" />" />
			<input type="submit" name="_target${prevPage}" ${empty
				prevPage ? "disabled" : ""} value="<fmt:message key="button.previous" />" />
			<input type="submit" name="_cancel"
				value="<fmt:message key="button.cancel" />" /></th>
		</tr>
	</table>
	</form>
</spring:nestedPath></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>