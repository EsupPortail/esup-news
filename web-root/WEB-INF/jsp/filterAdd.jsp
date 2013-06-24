<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewEntityFilter" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
<div id="news_clear"></div>
<br />
<fieldset>
    <legend> <fmt:message key="add.filter.page.title" /> </legend>
	<html:errors path="filter" fields="true" />
	<spring:nestedPath path="filter">
	<form name="${namespace}addFilter" method="post"
		action="${addFilterActionUrl}">
	<table border="0" cellspacing="0" cellpadding="4">
		<tr>
			<td class="portlet-form-label"><fmt:message
				key="news.label.entity.name" /></td>
			<td class="portlet-form-field-label"><c:out
				value='${entity.name}' /> <html:input path="entityId"
				value="${entity.entityId}" type="hidden" /></td>
		</tr>

		<tr>
			<td class="portlet-form-label"><fmt:message
				key="news.label.filter.type" /><span class="portlet-msg-alert">*</span></td>
			<td><spring:bind path="type">
			<select class="portlet-font" name="<c:out value='${status.expression}'/>"
				onchange="disableFilterSelect('news_filter_select_attr', this.options[this.selectedIndex].value);
				disableFilterSelect('news_filter_select_op', this.options[this.selectedIndex].value);">
				<c:forEach var="filterType" items="${filterTypeList}" varStatus="i">
					<option value="${filterType}"
					<c:if test="${i.first}">selected <c:set var="filterTypeValue" value="${filterType}" /></c:if>>
						<fmt:message key="news.label.filter.type.${filterType}" />
					</option>
				</c:forEach>
			</select>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
			</spring:bind></td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message
				key="news.label.filter.attribute" /><span class="portlet-msg-alert">*</span></td>
			<td><spring:bind path="attribute">
			<select id="news_filter_select_attrLDAP" class="portlet-font" name="<c:out value='${status.expression}'/>"
					<c:if test="${filterTypeValue eq 'Group'}">disabled style="display:none"</c:if>>
				<c:forEach var="attr" items="${attrLdapFilter}" varStatus="i">
					<option value="${attr}" <c:if test="${attr eq status.value}">selected</c:if>>
						<c:out value="${attr}" />
					</option>
				</c:forEach>
			</select>
			<select id="news_filter_select_attrGroup" class="portlet-font" name="<c:out value='${status.expression}'/>"
					<c:if test="${filterTypeValue eq 'LDAP'}">disabled style="display:none"</c:if>>
				<option value="groupName" selected><fmt:message	key="news.label.filter.attribute.groupName" /></option>
			</select>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
			</spring:bind></td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message
				key="news.label.filter.operator" /><span class="portlet-msg-alert">*</span></td>
			<td><spring:bind path="operator">
			<select id="news_filter_select_opLDAP" class="portlet-font" name="<c:out value='${status.expression}'/>"
					<c:if test="${filterTypeValue eq 'Group'}">disabled style="display:none"</c:if>>
				<c:forEach var="attr" items="${operatorListLDAP}" varStatus="i">
					<option value="${attr}" <c:if test="${attr eq status.value}">selected</c:if>>
						<c:out value="${attr.code}" />
					</option>
				</c:forEach>
			</select>
			<select id="news_filter_select_opGroup" class="portlet-font" name="<c:out value='${status.expression}'/>"
					<c:if test="${filterTypeValue eq 'LDAP'}">disabled style="display:none"</c:if>>
				<c:forEach var="attr" items="${operatorListGROUP}" varStatus="i">
					<option value="${attr}" <c:if test="${attr eq status.value}">selected</c:if>>
						<c:out value="${attr.code}" />
					</option>
				</c:forEach>
			</select>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
			</spring:bind></td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message
				key="news.label.filter.criteria" /><span class="portlet-msg-alert">*</span></td>
			<td><html:input path="value" size="60" maxlength="150" value="${filter.value}"/></td>
		</tr>

		<tr>
			<td colspan="2" align="center"><input type="submit"
				value="<fmt:message key='button.add'/>" /> <input type="button"
				value="<fmt:message key="button.cancel"/>"
				onclick="window.location.href='${viewEntityFilterRenderUrl}';" /></td>
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