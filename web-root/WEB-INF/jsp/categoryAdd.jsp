<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="newCat" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
<div id="news_clear"></div>
 <fieldset>
    <legend> <fmt:message key="add.category.page.title" /> </legend> 
	<html:errors path="categoryForm" fields="true" />
	<spring:nestedPath path="categoryForm" >
<form name="${namespace}addCategory" method="post"
	action="${addCategoryActionUrl}">
<table border="0" cellspacing="0" cellpadding="4" width="95%">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.entity.name" /></td>
		<td class="portlet-form-field-label"><c:out value='${entity.name}' />
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><spring:message
			code="news.label.category.name" /><span class="portlet-msg-alert">*</span></td>
		<td><html:input path="category.name" size="30"
			cssClass="portlet-form-input-field" cssStyle="width: 250px;" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /><span class="portlet-msg-alert">*</span></td>
		<td><html:textarea path="category.desc" cssStyle="width: 250px;"
			rows="8" cols="80" cssClass="portlet-form-input-field" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.langue" /></td>
		<td><spring:bind path="category.langue">
			<select name="<c:out value='${status.expression}'/>" style="width: 250px;">
				<option value="fr" cssClass="portlet-form-input-field"
					<c:if test="${status.value == 'fr'}">selected</c:if>><fmt:message
					key="langue.french" /></option>
				<option value="en" cssClass="portlet-form-input-field"
					<c:if test="${status.value == 'en'}">selected</c:if>><fmt:message
					key="langue.english" /></option>
			</select>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<c:if test="${fn:length(typeList) > 0}">
	<tr>
		<td valign="top" class="portlet-form-label"><fmt:message
			key="news.label.category.associated.types" /><span class="portlet-msg-alert">*</span></td>
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
	<tr>
		<td colspan="2" class="pointilles"></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.rssAllowed" /></td>
		<td><spring:bind path="category.rssAllowed">

			<input type="radio" name="<c:out value='${status.expression}'/>"
				value="1" checked>
			<label class="portlet-font" for="rssAllowed"><fmt:message
				key="news.label.yes" /></label>      &nbsp; &nbsp; &nbsp;
                              <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0">
			<label class="portlet-font" for="rssAllowed"><fmt:message
				key="news.label.no" /></label> <span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" class="pointilles"></td>
	</tr>
	<tr>
		<td colspan="2">
		<DL id="news_showhide">
			<DT onClick="slide('news_showhide_help_msg_refreshConfig')"><span class="portlet-font"> <fmt:message
				key="news.label.refreshConfig" /> <img
				src="<html:imagesPath/>help_c.gif" align="absmiddle" alt="" /> </span>
			<DD id="news_showhide_help_msg_refreshConfig"><span class="portlet-font"> <fmt:message
				key="news.help.refreshConfig" /></span>
		</DL>
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshPeriod" /></td>
		<td><spring:bind path="category.refreshPeriod">
			<select name="<c:out value='${status.expression}'/>">
				<c:forTokens var="p" items="hour,day" delims=",">
					<option value="<c:out value='${p}'/>"><fmt:message
						key="news.label.${p}" /></option>
				</c:forTokens>
			</select>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshFrequency" /></td>
		<td><html:input path="category.refreshFrequency" size="10"
			maxlength="30" value="1" /></td>
	</tr>
	<tr>
		<td colspan="2" class="pointilles"></td>
	</tr>
	<tr>
		<td colspan="2">
		<DL id="news_showhide">
			<DT onClick="slide('news_showhide_help_msg_accessibility')"><span class="portlet-form-label"> <fmt:message
				key="news.label.accessibility" /> <img
				src="<html:imagesPath/>help_c.gif" align="absmiddle" alt="" /> </span>
			<DD id="news_showhide_help_msg_accessibility"><span class="portlet-font"><fmt:message
				key="news.help.accessibility" /></span>
		</DL>
		</td>
	</tr>
	<tr>
		<td></td>
		<td class="portlet-font"><spring:bind
			path="category.publicView">
			<input type="radio" name="<c:out value='${status.expression}'/>"
				value="1" checked>
			<fmt:message key="news.label.public" />&nbsp; &nbsp; &nbsp;
                              <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0">
			<fmt:message key="news.label.private" />
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<p>&nbsp;</p>
			<input type="submit" value="<fmt:message key='button.add'/>" /> 
			<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${viewEntityRenderUrl}';" />
		</td>
	</tr>
</table>
</form>
</spring:nestedPath>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>