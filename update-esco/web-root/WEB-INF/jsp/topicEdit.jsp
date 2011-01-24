<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewTopicSetting" />
<%@ include file="/WEB-INF/jsp/submenu_t.jsp"%>
<%@ include file="/WEB-INF/jsp/header2T.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message
	key="edit.topic.page.title" /> <span class="portlet-font">"<c:out
	value='${topic.name}' />" </span> </legend> <html:errors path="topic" fields="true" />
<form name="${namespace}EditT" method="post"
	action="<portlet:actionURL>
                        <portlet:param name="action" value="editTopic"/>
                        <portlet:param name="tId" value="${topic.topicId}"/>
                </portlet:actionURL>">
<p></p>
<table border="0" cellpadding="5" cellspacing="5">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.category.name" /></td>
		<td class="portlet-form-field-label"><c:out value='${category.name}' /> 
			<html:input path="topic.categoryId" value="${category.categoryId}" type="hidden" />
			<html:input path="topic.rssAllowed" value="1" type="hidden" />
			<html:input path="topic.publicView" value="1" type="hidden" />
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.topic.name" /> <span class="portlet-msg-alert">*</span></td>
		<td><html:input path="topic.name" size="30" maxlength="80" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /> <span class="portlet-msg-alert">*</span></td>
		<td><html:textarea path="topic.desc" rows="3" cols="80" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.langue" /></td>
		<td><spring:bind path="topic.langue">
			<select name="<c:out value='${status.expression}'/>">
				<option value="fr"
					<c:if test="${status.value == 'fr'}">selected</c:if>><fmt:message
					key="langue.french" /></option>
				<option value="en"
					<c:if test="${status.value == 'en'}">selected</c:if>><fmt:message
					key="langue.english" /></option>
			</select>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" class="pointilles"></td>
	</tr>
	<tr>
		<td colspan="2">
		<DL id="news_showhide">
			<DT onClick="slide('news_showhide_help_msg')"><span class="portlet-font"> <fmt:message
				key="news.label.refreshConfig" /> <img
				src="<html:imagesPath/>help_t.gif" align="absmiddle" alt="" /> </span>
			<DD id="news_showhide_help_msg"><span class="portlet-font"><fmt:message
				key="news.help.refreshConfig" /></span>
		</DL>
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshPeriod" /></td>
		<td><spring:bind path="topic.refreshPeriod">
			<span class="portlet-font"> </span>
			<select name="<c:out value='${status.expression}'/>">
				<c:forTokens var="p" items="hour,day" delims=",">
					<option value="<c:out value='${p}'/>"
						<c:if test="${p eq status.value}">selected </c:if>><fmt:message
						key="news.label.${p}" /></option>

				</c:forTokens>
			</select>
			<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
		</spring:bind></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshFrequency" /></td>
		<td><html:input path="topic.refreshFrequency" size="10"
			maxlength="30" /> <span class="portlet-font"></span></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<p>&nbsp;</p>
		<input type="submit" value="<fmt:message key="button.save" />"/>
		<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${viewCatRenderUrl}';" />
		</td>
	</tr>
</table>
</form>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>