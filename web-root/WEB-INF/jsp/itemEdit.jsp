<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>
<%--<link rel="stylesheet" type="text/css" media="all"
	href="${ctxPath}/scripts/jscalendar-1.0/skins/tiger/theme.css"
	title="tiger" />
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/lang/calendar-${slang}.js"></script>
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/calendar-setup.js"></script>
--%>
<script type="text/javascript">
			function FCKeditor_OnComplete(editorInstance) {
				window.status = editorInstance.Description;
			}

	function removeAttachment(index){
    	value = confirm('<fmt:message key="news.alert.delete.attachment"/>');
    	if(value == true)
    	{
			var action = document.forms["${namespace}EditItem"].action;
			var start = action.substring(0, action.indexOf("_page")+7);
			var end = action.substring(action.indexOf("_page")+7, action.length);
			
			document.forms["${namespace}EditItem"].action = start + "&${portletParamPrefixe}${pltc_target}removeAttachment="+ index + end;
			document.forms["${namespace}EditItem"].submit();
    	}
	}	
	function updateAttachment(index){
		var action = document.forms["${namespace}EditItem"].action;
		var start = action.substring(0, action.indexOf("_page")+7);
		var end = action.substring(action.indexOf("_page")+7, action.length);
		document.forms["${namespace}EditItem"].action = start + "&${portletParamPrefixe}${pltc_target}updateAttachment="+ index + end;
		
		document.getElementById("${namespace}updateAttachementButton").click();
	}		
</script>

<%@ include file="/WEB-INF/jsp/submenu_i.jsp"%>
<c:set var="aN" value="edit"/>
<%@ include file="/WEB-INF/jsp/header2I.jsp"%>
<div id="news_clear"></div>
 <fieldset>
    <legend> <fmt:message key="edit.item.page.title" /> </legend> 
    <html:errors path="itemForm" fields="true" /> 
	
	<spring:nestedPath path="itemForm">
	<form name="${namespace}EditItem" method="post"
		action="<portlet:actionURL>
                        <portlet:param name="action" value="editItem"/>
                        <portlet:param name="iId" value="${itemForm.item.itemId}"/>
                        <portlet:param name="tId" value="${topic.topicId}"/>
                        <portlet:param name="_page" value="${page}"/>
                </portlet:actionURL>">
	<table border="0" cellspacing="5" cellpadding="5" width="95%">
		<tr>
			<td class="portlet-form-label"><fmt:message	key="news.label.category" /> :</td>
			<td class="portlet-form-field-label"><c:out value="${category.name}" />
				<html:input path="item.categoryId" type="hidden" />
			</td>
		</tr>
		<tr>
			<td style="vertical-align: top;" class="portlet-form-label"><fmt:message key="news.label.topic" /> <span class="portlet-msg-alert">*</span> :</td>
			<td class="portlet-font">
			<c:choose>
				<c:when test="${fn:length(topicList) > 0}">
					<spring:bind path="topicIds">
						<table border="0" cellpadding="5">
							<c:set var="col" value="0" />
							<tr style="width: 100%;">
								<c:forEach items="${topicList}" var="topicEntry">
									<td>
										<c:forEach items="${status.value}" var="topicId">
											<c:if test="${topicId eq topicEntry.topicId}">
												<c:set var="selected" value="true" />
											</c:if>
										</c:forEach> 
										<input type="checkbox" name="${status.expression}" cssClass="portlet-form-input-field"
											<c:if test="${selected}">checked</c:if> value="${topicEntry.topicId}" /> 
										<input type="hidden" name="_${status.expression}" /> <c:remove var="selected" />
									</td>
									<td class="portlet-font" nowrap="true">
										<c:out value="${topicEntry.name}" />
									</td>
									<c:set var="col" value="${col+1}" />
									<c:if test="${(col % 4) == 0}">
									</TR>
									<TR>
									</c:if>
								</c:forEach>
								<div><c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if></div>
						</table>
					</spring:bind>
				</c:when>
				<c:otherwise>
					<div class="portlet-msg-info"><fmt:message
						key="news.view.noTopic" /></div>
				</c:otherwise>
			</c:choose>
			</td>
		</tr>
		<tr>
			<td valign="top" class="portlet-form-label"><fmt:message key="item.label.title" /> <span class="portlet-msg-alert">*</span></td>
			<td class="portlet-font"><html:input path="item.title" size="80" maxlength="150" /></td>
		</tr>
		<tr>
			<td class="portlet-form-label" style="vertical-align: top;"><fmt:message key="item.label.summary" /></td>
			<td><html:textarea path="item.summary" rows="3" cols="80" /></td>
		</tr>
		<tr>
			<td class="portlet-form-label" style="vertical-align: top;"><fmt:message key="item.label.body" /> <span class="portlet-msg-alert">*</span></td>
			<td><spring:bind path="item.body">
				<FCK:editor instanceName="${status.expression}" toolbarSet="Default"
					height="400px" width="80%">
					<jsp:attribute name="value">
						<c:out value="${status.value}" escapeXml="false"/>
					</jsp:attribute>
					<jsp:body>
						<FCK:config CustomConfigurationsPath="${ctxPath}/fckeditor/myconfig.js" />
					</jsp:body>
				</FCK:editor>
			  </spring:bind>
			</td>
		</tr>
		<c:if test="${attActivate eq 'true'}">
		<tr>
			<td colspan="2">
			<!-- Pieces jointes  -->
			<fieldset style="width:90%;"><legend> <fmt:message key="news.label.attachment.fieldset.title" /> </legend>
			<c:choose>
				<c:when test="${fn:length(attachmentList) > 0}">
				<table class="attachments">
					<tr class="attachments_header">
						<th><fmt:message key="news.label.attachment.title" /></th>
						<th><fmt:message key="news.label.attachment.desc" /></th>
						<th><fmt:message key="news.label.attachment.type" /></th>
						<th><fmt:message key="news.label.attachment.date" /></th>
						<th><fmt:message key="news.label.attachment.actions.edit" /></th>
						<th><fmt:message key="news.label.attachment.actions.delete" /></th>
					</tr>
						<c:forEach items="${attachmentList}" var="attachmentEntry" varStatus="status" >
							<tr class="${status.first ? 'first_line' : ''}">
								<td class="attachments_title" style="width:300px;"><c:out value="${attachmentEntry.title}"/></td>
								<td><c:out value="${attachmentEntry.desc}"/></td>
								<td style="text-align:center; width:70px; padding-left:0px;">
									<img src="<html:imagesPath/>types/${attachmentEntry.type}.png"
										alt="${attachmentEntry.type}" title="${attachmentEntry.type}"/>
								</td>
								<td style="width:120px;">
									<fmt:formatDate pattern="${datePattern}" value="${attachmentEntry.insertDate}"/>
								</td>
								<td style="text-align:center; width:70px; padding-left:0px;">
									<a href="javascript:updateAttachment('${status.index}');">
										<img src="<html:imagesPath/>edit.gif" 
											alt="<fmt:message key="button.edit"/>"
											title="<fmt:message key="button.edit"/>"/>
									</a>
								</td>
								<td style="text-align:center; width:70px; padding-left:0px;">
									<a href="javascript:removeAttachment('${status.index}');">
										<img src="<html:imagesPath/>delete.gif" 
											alt="<fmt:message key="button.delete"/>"
											title="<fmt:message key="button.delete"/>"/>
									</a>
								</td>
							</tr>
						</c:forEach>
					<tr>
						<td colspan="4" class="attachments_buttons">
							<input type="submit" name="_target1" value="<fmt:message key="news.label.attachment.addAFile" />"/>
							<input type="submit" name="_target2" value="<fmt:message key="news.label.attachment.addExistingFile" />"/>
							<input type="submit" name="_target3" value="<fmt:message key="news.label.attachment.editAttachement"/>" id="${namespace}updateAttachementButton" style="display:none;"/>
						</td>
					</tr>
				</table>
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.attachment.noAttachment" />
					<table class="attachments">
						<tr>
							<td class="attachments_buttons">
								<input type="submit" name="_target1" value="<fmt:message key="news.label.attachment.addAFile" />"/>
								<input type="submit" name="_target2" value="<fmt:message key="news.label.attachment.addExistingFile" />"/>
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
			</fieldset>
			</td>
		</tr>
		</c:if>
		<tr>
			<td colspan="2" class="pointilles"></td>
		</tr>
		<tr>
			<td colspan="2">
			<DL id="news_showhide">
				<DT onClick="slide('news_showhide_help_msg')">
				<span class="portlet-font"> 
					<fmt:message key="news.label.lifecycle" /> <img src="<html:imagesPath/>help_i.gif" alt="" />
				</span>
				<DD id="news_showhide_help_msg">
				<span class="portlet-font"> 
					<fmt:message key="news.help.lifecycle.1" /> <c:out value="${nbDays}" /> <fmt:message key="news.help.lifecycle.2" />
				</span>
			</DL>
			</td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message key="item.label.startDate" /></td>
			<td>
				<html:input path="item.startDate" size="30" maxlength="30" id="f_date_c" /> 
				<%--<img src="<html:imagesPath/>cal.gif"
					id="f_trigger_c" style="cursor: pointer; border: 1px solid red;"
					title="Date selector" onmouseover="this.style.background='red';"
					onmouseout="this.style.background=''" /> --%>
					<span class="portlet-font">[<fmt:message key="label.date.format" />]</span> 
					<%--<script type="text/javascript">
					    Calendar.setup({
					        inputField     :    "f_date_c",
					        ifFormat       :    "<c:out value='${datePatternC}'/>",
					        button         :    "f_trigger_c",
					        align          :    "Bl",
					        singleClick    :    true
					    });
					</script>--%>
			</td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message key="item.label.end.time" /></td>
			<td>
				<html:input path="item.endDate" size="30" maxlength="30" id="f_date_d" />
				<%--<img src="<html:imagesPath/>cal.gif"
				id="f_trigger_d" style="cursor: pointer; border: 1px solid red;"
				title="Date selector" onmouseover="this.style.background='red';"
				onmouseout="this.style.background=''" /> --%>
				<span class="portlet-font">[<fmt:message key="label.date.format" />]</span> 
				<%--<script type="text/javascript">
				    Calendar.setup({
				        inputField     :    "f_date_d",
				        ifFormat       :    "<c:out value='${datePatternC}'/>",
				        button         :    "f_trigger_d",
				        align          :    "Tl",
				        singleClick    :    true
				    });
				</script>--%>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="pointilles"></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" name="_finish" value='<fmt:message key="button.save" />' />
				<input type="submit" name="_cancel" value="<fmt:message key="button.cancel" />" />
			</td>
		</tr>
	</table>
	</form>
</spring:nestedPath></fieldset>
<p/><div class="news_legende"> 
<p class="portlet-font"><fmt:message key="news.label.legende"/> :<br/>
<span class="portlet-msg-alert">* <fmt:message key="news.legend.field_required"/></span>
</div></p>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>