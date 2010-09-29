<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewCatSetting" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
 <fieldset>
    <legend> <fmt:message key="edit.category.page.title" /> </span> </legend> 
	<html:errors path="categoryForm" fields="true" />
	<spring:nestedPath path="categoryForm">
<form name="${namespace}EditC" method="post"
	action="${editCategoryActionUrl}">
<table border="0" cellpadding="5" cellspacing="5" width="95%">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.entity.name" /></td>
		<td class="portlet-form-field-label"><c:out value='${entity.name}' />
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.category.name" /><span class="portlet-msg-alert">*</span></td>
		<td class="portlet-font"><html:input path="category.name"
			size="30" maxlength="80" /> <span class="portlet-msg-error">${status.errorMessage}</span></td>
	</tr>
	<tr>
		<td valign="top" class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /><span class="portlet-msg-alert">*</span></td>
		<td class="portlet-font"><spring:bind path="category.desc">

			<textarea id="${status.expression}" name="${status.expression}"
				rows="5" cols="70"><c:out value="${status.value}" /></textarea>


		</spring:bind> <spring:bind path="category.creationDate">
			<input type="hidden" name="<c:out value='${status.expression}'/>"
				value="${status.value}">
		</spring:bind></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.langue" /></td>
		<td class="portlet-font"><spring:bind path="category.langue">
			<select name="<c:out value='${status.expression}'/>">

				<option value="fr"
					<c:if test="${status.value == 'fr'}">selected</c:if>><fmt:message
					key="langue.french" /></option>
				<option value="en"
					<c:if test="${status.value == 'en'}">selected</c:if>><fmt:message
					key="langue.english" /></option>
			</select>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	
	<c:if test="${fn:length(typeList) > 0}">
		<tr>
			<td valign="top" class="portlet-form-label"><fmt:message
				key="news.label.category.associated.types" /><span
				class="portlet-msg-alert">*</span></td>
			<td class="portlet-font"><spring:bind path="typesIds">
				<table border=0 cellpadding=5>
					<c:set var="col" value="0" />
					<tr style="width: 100%;">
						<c:forEach items="${typeList}" var="typeEntry">
							<td>
								<c:forEach items="${categoryForm.typesIds}" var="tId">
									<c:if test="${tId eq typeEntry.typeId}">
										<c:set var="selected" value="true" />
									</c:if>
								</c:forEach> 
								<input type="checkbox" name="${status.expression}"
									cssClass="portlet-form-input-field"
									<c:if test="${selected}">checked</c:if>
									value="${typeEntry.typeId}" /> 
								<input type="hidden"
									name="_${status.expression}" /> <c:remove var="selected" />
							</td>
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
			</spring:bind></td>
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
				value="1" <c:if test="${status.value == '1'}">checked</c:if>>
			<label class="portlet-font" for="rssAllowed"><fmt:message 
				key="news.label.yes" /></label>      &nbsp; &nbsp; &nbsp;
                              <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0"
				<c:if test="${status.value == '0'}">checked</c:if>>
			<label class="portlet-font" for="rssAllowed"><fmt:message key="news.label.no" /></label>
			<span class="portlet-msg-error">${status.errorMessage}</span>
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
					<option value="<c:out value='${p}'/>"
						<c:if test="${p eq status.value}">selected </c:if>><fmt:message
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
			maxlength="30" /></td>
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
		<td class="portlet-font"><spring:bind path="category.publicView">
			<input type="radio" name="<c:out value='${status.expression}'/>"
				value="1" <c:if test="${status.value == '1'}">checked</c:if>>
			<fmt:message key="news.label.public" />      &nbsp; &nbsp; &nbsp;
                              <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0"
				<c:if test="${status.value == '0'}">checked</c:if>>
			<fmt:message key="news.label.private" />
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<p>&nbsp;</p>
			<input type="submit" value="<fmt:message key='button.save'/>" />
			<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${viewEntityRenderUrl}';" />
		</td>
	</tr>
</table>
</form>
</spring:nestedPath>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>
