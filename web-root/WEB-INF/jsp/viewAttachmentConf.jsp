<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>

<div id="news_headerA"> <IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"/> 
	<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>

<c:set var="currentHeaderSM" value="attachmentConf" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>

<div id="news_clear"></div>

<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>

<br />
<fieldset>
<legend> <fmt:message key="menu.viewAttachmentConf" /> </legend> 
 	
    	<div>
    		<p class="portlet-msg-info" style="font-weight:bold;">
    			<img src="<html:imagesPath/>puce_adm.gif"/>
    			<fmt:message key="news.label.attachment.prop.title"/> :
    			<a href="<portlet:renderURL>
                           <portlet:param name="action" value="editAttachmentOptions"/>
                         </portlet:renderURL>">
                  <img title='<fmt:message key="button.update"/>' alt='<fmt:message key="button.update"/>' src="<html:imagesPath/>edit.gif" style="padding-left:15px;cursor:pointer;"/>
                </a>
    		</p>    
			<table border="0" cellpadding="4">
				<tr>
					<td class="portlet-font-dim"><fmt:message key="news.label.attachment.maxSize"/> :</td> 
    				<td class="portlet-font">
    					<c:if test="${maxSize != null}">
							<fsc:format value="${maxSize}"/> 
						</c:if>
    				</td>
    			</tr>
    			<tr>
					<td class="portlet-font-dim" style="vertical-align:top;"><fmt:message key="news.label.attachment.authorisedExts"/> : </td>
					<td class="portlet-font">
						<select size="5" style="width:167px;">
							<c:forEach items="${authorizedExts}" var="extension">
								<option value="${extension}"><c:out value="${extension}"/></option>
							</c:forEach>
						</select> 		
					</td>
				</tr>
				<tr>
					<td class="portlet-font-dim" style="vertical-align:top;"><fmt:message key="news.label.attachment.forbiddenExts"/> : </td>
					<td class="portlet-font">
						<select size="5" style="width:167px;">
							<c:forEach items="${forbiddenExts}" var="extension">
								<option value="${extension}"><c:out value="${extension}"/></option>
							</c:forEach>
						</select> 
					</td>
				</tr>
    		</table>
    	</div>
    	
    	<div>
    		<p class="portlet-msg-info" style="font-weight:bold;">
    			<img src="<html:imagesPath/>puce_adm.gif"/>
    			<fmt:message key="news.label.cmisserver.default.conf" /> :
    			<a href="<portlet:renderURL>
                           <portlet:param name="action" value="editCmisServerParams"/>
                         </portlet:renderURL>">
                  <img title='<fmt:message key="button.update"/>' alt='<fmt:message key="button.update"/>' src="<html:imagesPath/>edit.gif" style="padding-left:15px;cursor:pointer;"/>
                </a>    		
    		</p>
    	
    		<table border="0" cellpadding="4">
				<tr>
					<td class="portlet-font-dim">
						<fmt:message key="news.label.cmisserver.url"/> : 
					</td>
					<td class="portlet-font">
    					<c:out value="${serverUrl}"></c:out>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-font-dim">
	    				<fmt:message key="news.label.cmisserver.login"/> : 
	    			</td>
					<td class="portlet-font">
    					<c:out value="${serverLogin}"></c:out>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-font-dim">	
    					<fmt:message key="news.label.cmisserver.pwd"/> : 
    				</td>
					<td class="portlet-font">
    					<input type="password" size="30" id="displayedServerPwd" value="${serverPwd}" disabled="disabled"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-font-dim">
    					<fmt:message key="news.label.cmisserver.repo.id"/> :
    				</td>
					<td class="portlet-font"> 
    					<c:out value="${serverRepositoryId}"></c:out>
    				</td>
    			</tr>
    		</table>
    
    	</div>
	
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>