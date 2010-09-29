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
	
<html:errors path="cmisServerParamsForm" fields="true" />
<spring:nestedPath path="cmisServerParamsForm">
	
	<form name="${namespace}cmisServerForm" method="post"
		  action="<portlet:actionURL>
                        <portlet:param name="action" value="editCmisServerParams"/>
               		</portlet:actionURL>">  
               		           		 
    	<html:input type="hidden"  path="serverId"/>

    	<div id="${namespace}changeParamsArea">
    		<p class="portlet-msg-info" style="font-weight:bold;">
    			<img title='' alt='' src="<html:imagesPath/>puce_adm.gif"/>
    			<fmt:message key="news.label.cmisserver.change.conf"/> :
    		</p>
    		<table border="0" cellpadding="4">
				<tr>
					<td class="portlet-form-label">
						<label for="serverUrl"><fmt:message key="news.label.cmisserver.url"/> : </label>
					</td>
					<td class="portlet-form-field-label"> 
    					<html:input type="text" id="serverUrl" size="30" path="serverUrl"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
	    				<label for="serverLogin" style="width:150px;float:left;"><fmt:message key="news.label.cmisserver.login"/> : </label>
	    			</td>
					<td class="portlet-form-field-label">
    					<html:input type="text" id="serverLogin" size="30" path="serverLogin"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
    					<label for="serverPwd" style="width:150px;float:left;"><fmt:message key="news.label.cmisserver.pwd"/> : </label>
    				</td>
					<td class="portlet-form-field-label">
    					<html:input type="password" id="serverPwd" size="30" path="serverPwd"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
    					<label for="serverPwd" style="width:150px;float:left;"><fmt:message key="news.label.cmisserver.pwd2"/> : </label>
    				</td>
					<td class="portlet-form-field-label">
    					<html:input type="password" id="serverPwd2" path="serverPwd2" size="30"/>
    				</td>
    			</tr>
    			<tr>
					<td class="portlet-form-label">
    					<label for="repositoryId" style="width:150px;float:left;"><fmt:message key="news.label.cmisserver.repo.id"/> :</label>
    				</td>
					<td class="portlet-form-field-label"> 
    					<html:input type="text" id="repositoryId" size="30" path="repositoryId"/>
    				</td>
    			</tr>    	
    			<tr>	
    				<td colspan="2" align="center">  
    					<input type="submit" value="<fmt:message key="button.save"/>"/>&nbsp; 
    					<input type="button" value="<fmt:message key="button.cancel"/>" onclick="window.location.href='${viewAttachmentConfRenderUrl}';"/>
    				</td>
    			</tr>
    		</table>
    	</div>

  </form>
    
</spring:nestedPath>
	
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>