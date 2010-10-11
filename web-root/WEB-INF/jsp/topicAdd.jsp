<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="newTopic" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message
	key="add.topic.page.title" /> </legend> <html:errors path="topic" fields="true" />
<form name="${namespace}AddT" method="post"
	action="<portlet:actionURL>
                            <portlet:param name="action" value="addTopic"/>
                            </portlet:actionURL>">
<table border="0" cellpadding="4" width="95%">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.category.name" /></td>
		<td class="portlet-form-field-label"><c:out value='${category.name}' /> <html:input
			path="topic.categoryId" value="${category.categoryId}" type="hidden" />
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.topic.name" /> <span class="portlet-msg-alert">*</span></td>
		<td><html:input path="topic.name" size="60" maxlength="150" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /><span class="portlet-msg-alert">*</span></td>
		<td><html:textarea path="topic.desc" rows="3" cols="80" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.langue" /></td>
		<td><spring:bind path="topic.langue">
			<select class="portlet-font"
				name="<c:out value='${status.expression}'/>">

				<option value="fr" selected><fmt:message
					key="langue.french" /></option>
				<option value="en"><fmt:message key="langue.english" /></option>
			</select>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" class="pointilles"></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.rssAllowed" /></td>
		<td><spring:bind path="topic.rssAllowed">
			<input type="radio" name="<c:out value='${status.expression}'/>"
				value="1" checked>
			<label class="portlet-font" for="rssAllowed"><fmt:message key="news.label.yes" />
			</label>     &nbsp; &nbsp; &nbsp;
                         <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0">
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
				src="<html:imagesPath/>help_t.gif" align="absmiddle" alt="" /> </span>
			<DD id="news_showhide_help_msg_refreshConfig"><span class="portlet-font"> <fmt:message
				key="news.help.refreshConfig" /></span>
		</DL>
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshPeriod" /></td>
		<td><spring:bind path="topic.refreshPeriod">
			<span class="portlet-font"> <select
				name="<c:out value='${status.expression}'/>">
				<c:forTokens var="p" items="hour,day" delims=",">
					<option value="<c:out value='${p}'/>"><fmt:message
						key="news.label.${p}" /></option>
				</c:forTokens>
			</select> <span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.refreshFrequency" /></td>
		<td><html:input path="topic.refreshFrequency" size="10"
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
				src="<html:imagesPath/>help_t.gif" align="absmiddle" alt="" /> </span>
			<DD id="news_showhide_help_msg_accessibility"><span class="portlet-font"><fmt:message
				key="news.help.accessibility" /></span>
		</DL>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><spring:bind path="topic.publicView">
			<input type="radio" name="<c:out value='${status.expression}'/>"
				value="1" checked>
			<span class="portlet-font"><fmt:message
				key="news.label.public" /></span>      &nbsp; &nbsp; &nbsp;
                              <input type="radio"
				name="<c:out value='${status.expression}'/>" value="0">
			<span class="portlet-font"><fmt:message
				key="news.label.private" /></span>
			<span class="portlet-msg-error">${status.errorMessage}</span>
		</spring:bind></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<p>&nbsp;</p>
		<input type="submit" value='<fmt:message key="button.save"/>' />
		<input type="button" value="<fmt:message key="button.cancel"/>"
						onclick="window.location.href='${viewCatRenderUrl}';" />
		</td>
	</tr>
</table>
</form>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>