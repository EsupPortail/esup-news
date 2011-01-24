<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.uhp.portlets.news.domain.Topic"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>

<%@page import="org.uhp.portlets.news.domain.Topic"%><link rel="stylesheet" type="text/css" media="all"
	href="${ctxPath}/scripts/jscalendar-1.0/skins/tiger/theme.css"
	title="tiger" />
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/lang/calendar-${slang}.js"></script>
<script type="text/javascript"
	src="${ctxPath}/scripts/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript">
	function FCKeditor_OnComplete(editorInstance) {
        	window.status = editorInstance.Description;
        }

    function removeAttachment(index){
    	value = confirm('<fmt:message key="news.alert.delete.attachment"/>');
    	if(value == true)
    	{
			var action = document.forms["${namespace}AddItem"].action;
			var start = action.substring(0, action.indexOf("_page")+7);
			var end = action.substring(action.indexOf("_page")+7, action.length);
			
			document.forms["${namespace}AddItem"].action = start + "&removeAttachment="+ index + end;
			document.forms["${namespace}AddItem"].submit();
    	}
    }
    
	function updateAttachment(index){
		var action = document.forms["${namespace}AddItem"].action;
		var start = action.substring(0, action.indexOf("_page")+7);
		var end = action.substring(action.indexOf("_page")+7, action.length);
		document.forms["${namespace}AddItem"].action = start + "&updateAttachment="+ index + end;
		
		document.getElementById("${namespace}updateAttachementButton").click();
	}	
</script>
<c:choose>
	<c:when test='${not empty topic}'>
		<%@ include file="/WEB-INF/jsp/submenu_t.jsp"%>
		<c:set var="aN" value="newItem" />
		<%@ include file="/WEB-INF/jsp/header2T.jsp"%>
	</c:when>
	<c:otherwise>
		<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
		<c:set var="aN" value="newItem" />
		<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
	</c:otherwise>
</c:choose>


	<div id="news_clear"></div>
	<c:choose>
		<c:when test="${fn:length(topicList) < 1}">
			<div>
			<p></p>
			<p class="portlet-msg-info"><fmt:message
				key="news.alert.noTopicToSelect" /></p>
			</div>
		</c:when>
		<c:otherwise>
			<html:errors path="itemForm" fields="true" />
			<spring:nestedPath path="itemForm">
				<p />
				<fieldset><legend> <fmt:message key="title.addNewAnn" /> </legend>
				<form name="${namespace}AddItem" method="post"
					action="<portlet:actionURL>
                        <portlet:param name="action" value="addItem"/>
                        <portlet:param name="tId" value="${topic.topicId}"/>
                        <portlet:param name="_page" value="${page}"/>
               		 </portlet:actionURL>">
               		 
				<table border="0" cellpadding="4" width="95%">
					<tr>
						<td class="portlet-form-label"><fmt:message key="news.label.category" /></td>
						<td class="portlet-form-field-label"><c:out value='${category.name}' /> 
							<html:input path="item.categoryId" value="${category.categoryId}" type="hidden" />
						</td>
					</tr>
					<tr>
						<td valign="top" class="portlet-form-label">
							<fmt:message key="news.label.topic" /><span class="portlet-msg-alert">*</span>
						</td>
						<td class="portlet-font">
						
							<spring:bind path="topicIds">
							<table border=0 cellpadding=5>
								<c:set var="col" value="0" />
								<tr style="width: 100%;">
									
									<c:forEach items="${topicList}" var="topicEntry">
										<td>
											<c:forEach items="${status.value}" var="topicId">
												<c:if test="${topicId eq topicEntry.topicId}">
													<c:set var="selected" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${topicEntry.topicId eq topic.topicId || fn:length(topicList) eq 1 }">
													<c:set var="selected" value="true" />
											</c:if>
											
											<input type="checkbox" name="${status.expression}" cssClass="portlet-form-input-field"
													<c:if test="${selected}">checked</c:if> value="${topicEntry.topicId}" /> 
											<input type="hidden" name="_${status.expression}" /> <c:remove var="selected" />
										</td>
										<td class="portlet-font" nowrap="true"><c:out value="${topicEntry.name}" /></td>
										<c:set var="col" value="${col+1}" />
										<c:if test="${(col % 4) eq 0}">
								</TR>
								<TR>
									</c:if>
									</c:forEach>
									<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
									
							</table>
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td valign="top" class="portlet-form-label">
							<fmt:message key="item.label.title" /><span class="portlet-msg-alert">*</span>
						</td>
						<td class="portlet-font">
							<html:input path="item.title" size="80" maxlength="150" />
						</td>
					</tr>
					<tr>
						<td valign="top" class="portlet-form-label">
							<fmt:message key="item.label.summary" />
						</td>
						<td><html:textarea path="item.summary" rows="3" cols="80" /></td>
					</tr>
					<tr>
						<td valign="top" class="portlet-form-label"><fmt:message key="item.label.body" /> <span class="portlet-msg-alert">*</span></td>
						<td>
							<spring:bind path="item.body">
							<FCK:editor instanceName="${status.expression}" toolbarSet="Default" height="400px" width="80%">
								<jsp:attribute name="value">
                            		<c:out value="${status.value}"  escapeXml="false"/>
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
						<td colspan="2">
						<DL id="news_showhide">
							<DT onClick="slide('news_showhide_help_msg')"><span class="portlet-font"> <fmt:message key="news.label.lifecycle" /> 
								<img src="<html:imagesPath/>help_c.gif" alt="" /></span>
							<DD id="news_showhide_help_msg">
								<span class="portlet-font">
								<fmt:message key="news.help.lifecycle.1" /> <c:out value="${nbDays}" /> <fmt:message key="news.help.lifecycle.2" />
								</span>
						</DL>
						</td>
					</tr>
					<tr>
						<td class="portlet-form-label"><fmt:message
							key="item.label.startDate" /></td>
						<td><html:input path="item.startDate" id="f_date_c1" size="30" maxlength="30" /> 
							<img src="<html:imagesPath/>cal.gif"
								id="f_trigger_c1"
								style="cursor: pointer; border: 1px solid #0f5294;"
								title="Date selector" onmouseover="this.style.background='red';"
								onmouseout="this.style.background=''" /> 
							<span class="portlet-font"> [<fmt:message key="label.date.format" />]</span>
						</td>
					</tr>
					<tr>
						<td class="portlet-form-label"><fmt:message key="item.label.end.time" /></td>
						<td><html:input path="item.endDate" cssClass="portlet-form-input-field" id="f_date_c2" size="30" maxlength="30" />
							<img src="<html:imagesPath/>cal.gif"
								id="f_trigger_c2"
								style="cursor: pointer; border: 1px solid #0f5294;"
								title="Date selector" onmouseover="this.style.background='red';"
								onmouseout="this.style.background=''" /> 
							<span class="portlet-font"> [<fmt:message key="label.date.format" />]</span>
						</td>
					</tr>
<script type="text/javascript">
    Calendar.setup({
        inputField     :    "f_date_c1",     
        ifFormat       :    "<c:out value='${datePatternC}'/>",      
        button         :    "f_trigger_c1",  
        align          :    "Bl",          
        singleClick    :    true
    });
</script>

					<tr>
						<td colspan="2" align="center">
							<input type="submit" name="_finish" value="<fmt:message key="button.save"/>" />&nbsp; 
							<c:choose>
								<c:when test='${not empty topic}'>
									<c:set var="url" value="${viewTopicRenderUrl}" />
								</c:when>
								<c:otherwise>
									<c:set var="url" value="${viewCatRenderUrl}" />
								</c:otherwise>
							</c:choose> 
							<input type="button" value="<fmt:message key="button.cancel"/>" onclick="window.location.href='${url}';" />
						</td>
					</tr>
				</table>
				<script type="text/javascript">
    Calendar.setup({
        inputField     :    "f_date_c2",     
        ifFormat       :    "<c:out value='${datePatternC}'/>",      
        button         :    "f_trigger_c2",  
        align          :    "Bl",           
        singleClick    :    true
    });
</script></form>
				</fieldset>
			</spring:nestedPath>
		</c:otherwise>
	</c:choose>
	<%@ include file="/WEB-INF/jsp/footer.jsp"%>