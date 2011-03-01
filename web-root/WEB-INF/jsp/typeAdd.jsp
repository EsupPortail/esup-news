<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"><IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="type" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>

<fieldset>
    <legend> <fmt:message key="add.type.page.title" /> </legend> 
	<html:errors path="typeForm" fields="true" />
	<spring:nestedPath path="typeForm">
<form name="${namespace}addType" method="post"
	action="${addTypeActionURL}">
<table border="0" cellspacing="0" cellpadding="4">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.type.name" /><span class="portlet-msg-alert">*</span></td>
		<td><html:input path="type.name" size="30"
			cssClass="portlet-form-input-field" cssStyle="width: 250px;" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /><span class="portlet-msg-alert">*</span></td>
		<td><html:textarea path="type.description" cssStyle="width: 250px;"
			rows="8" cols="80" cssClass="portlet-form-input-field" /></td>
	</tr>
	<c:if test="${fn:length(entityList) > 0}">
	<tr>
		<td valign="top" class="portlet-form-label"><fmt:message
			key="news.label.type.associated.entities" /><span class="portlet-msg-alert"></span></td>
		<td class="portlet-font"><spring:bind path="entitiesIds">
			<table border=0 cellpadding=5>
				<c:set var="col" value="0" />
				<tr style="width: 100%;">
					<c:forEach items="${entityList}" var="entityEntry">
						<td>
							<input type="checkbox" name="${status.expression}"
								cssClass="portlet-form-input-field"
								value="${entityEntry.entityId}" /> 
							<input type="hidden" name="_${status.expression}" />
						</td>
						<td class="portlet-font" nowrap="true"><c:out
							value="${entityEntry.name}" /></td>
						<c:set var="col" value="${col+1}" />
						<c:if test="${(col % 4) eq 0}">
							</tr>
							<tr>
						</c:if>
					</c:forEach>
				</tr>
			</table>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	</c:if>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="<fmt:message key='button.add'/>" /> 
			<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${viewTypesRenderUrl}';" />
		</td>
	</tr>
</table>
</form>
</spring:nestedPath>
</fieldset>
<p/><div class="news_legende"> 
<p class="portlet-font"><fmt:message key="news.label.legende"/> :<br/>
<span class="portlet-msg-alert">* <fmt:message key="news.legend.field_required"/></span>
</div></p>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>