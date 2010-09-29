<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"> <IMG align="absmiddle" alt="" src="<html:imagesPath/>icon_adm_ent.gif" border="5px">
<SPAN class="newsTitle"><fmt:message key="add.entity.page.title"/> </SPAN>
</div>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message key="add.entity.page.title" /> </legend> 
	<html:errors path="entityForm" fields="true" />
	<spring:nestedPath path="entityForm">
<form name="${namespace}addEntity" method="post"
	action="${addEntityActionUrl}">
<table border="0" cellspacing="0" cellpadding="4">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.entity.name" /><span class="portlet-msg-alert">*</span></td>
		<td><html:input path="entity.name" size="30"
			cssClass="portlet-form-input-field" cssStyle="width: 250px;" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /><span class="portlet-msg-alert">*</span></td>
		<td><html:textarea path="entity.description" cssStyle="width: 250px;"
			rows="8" cols="80" cssClass="portlet-form-input-field" /></td>
	</tr>
	<c:if test="${fn:length(typeList) > 0}">
	<tr>
		<td valign="top" class="portlet-form-label"><fmt:message
			key="news.label.entity.associated.types" /><span class="portlet-msg-alert">*</span></td>
		<td><spring:bind path="typesIds">
			<table border=0 cellpadding=5>
				<c:set var="col" value="0" />
				<tr style="width: 100%;">
					<c:forEach items="${typeList}" var="typeEntry" varStatus="numType">
						<td><c:if test="${typeId eq typeEntry.typeId || numType.first}">
							<c:set var="selected" value="true" />
						</c:if> <input type="checkbox" name="${status.expression}"
							cssClass="portlet-form-input-field"
							<c:if test="${selected}">checked</c:if>
							value="${typeEntry.typeId}" /> <input type="hidden"
							name="_${status.expression}" /> <c:remove var="selected" /></td>
						<td class="portlet-font" nowrap="true"><c:out
							value="${typeEntry.name}" /></td>
						<c:set var="col" value="${col+1}" />
						<c:if test="${(col % 4) eq 0}">
							</tr>
							<tr>
						</c:if>
					</c:forEach>
				</tr>
			</table>
			<span class="portlet-msg-error">${status.errorMessage}</span>
			</spring:bind>
		</td>
	</tr>
	</c:if>
	<c:if test="${fn:length(categoryList) > 0}">
	<tr>
		<td valign="top" class="portlet-form-label"><fmt:message
			key="news.label.category.without.entity" /></td>
		<td><spring:bind path="categoriesIds">
			<table border=0 cellpadding=5>
				<c:set var="col" value="0" />
				<tr style="width: 100%;">
					<c:forEach items="${categoryList}" var="categoryEntry">
						<td><c:if test="${categoryId eq categoryEntry.categoryId}">
							<c:set var="selected" value="true" />
						</c:if> <input type="checkbox" name="${status.expression}"
							cssClass="portlet-form-input-field"
							<c:if test="${selected}">checked</c:if>
							value="${categoryEntry.categoryId}" /> <input type="hidden"
							name="_${status.expression}" /> <c:remove var="selected" /></td>
						<td class="portlet-font" nowrap="true"><c:out
							value="${categoryEntry.name}" /></td>
						<c:set var="col" value="${col+1}" />
						<c:if test="${(col % 4) eq 0}">
							</tr>
							<tr>
						</c:if>
					</c:forEach>
				</tr>
			</table>
			<span class="portlet-msg-error">${status.errorMessage}</span>
			</spring:bind>
		</td>
	</tr>
	</c:if>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="<fmt:message key='button.add'/>" /> 
			<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${homeUrl}';" />
		</td>
	</tr>
</table>
</form>
</spring:nestedPath>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>