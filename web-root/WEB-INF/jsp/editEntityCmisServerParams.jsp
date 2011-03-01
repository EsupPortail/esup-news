<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="aN" value="attachmentConf" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>

<script type="text/javascript">

	function displayArea(area)
	{
		document.getElementById(area).style.display="block";
		document.getElementById('${namespace}displayedParamsArea').style.display="none";

		document.getElementById('${namespace}source1').checked = false;
		document.getElementById('${namespace}source2').checked = false;
	}

	function hideArea(area)
	{
		document.getElementById('${namespace}displayedParamsArea').style.display="block";
		document.getElementById(area).style.display="none";
		document.getElementById('serverUrl').value="";
		document.getElementById('serverLogin').value="";
		document.getElementById('serverPwd').value="";
		document.getElementById('serverPwd2').value="";
		document.getElementById('repositoryId').value="";

		window.location.href='${cancelEntityCmisServerParamsRenderUrl}';
	}

    function changeOptionsSrce(src)
    {
    	if(src == 'app')
    	{
			// use appalication options
			if(confirm("<fmt:message key="news.label.cmisserver.confirmMsg"/>"))
			{
				document.getElementById("editEntityOptions").style.display="none";
				document.getElementById("useEntityServer").value="false";

				document.getElementById("${namespace}cmisServerForm").submit();
				return true;
			}
			else
			{
				return false;
			}
    	}
    	else if(src == 'entity')
    	{
    		document.getElementById("editEntityOptions").style.display="block";
			document.getElementById("useEntityServer").value="true";
			
			return true;
    	} 	
    }
</script>

<div id="news_clear"></div>

<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>

<br />
<fieldset>
<legend> <fmt:message key="menu.viewEntityAttachmentConf" /> </legend> 
	
<html:errors path="cmisServerParamsForm" fields="true" />
<spring:nestedPath path="cmisServerParamsForm">
	
	<form name="${namespace}cmisServerForm" id="${namespace}cmisServerForm" method="post"
		  action="<portlet:actionURL>
                        <portlet:param name="action" value="editEntityCmisServerParams"/>
                        <portlet:param name="eId" value="${entity.entityId}" />
               		</portlet:actionURL>">  
               		           		 
    	<html:input type="hidden"  path="serverId"/>
    	<html:input type="hidden"  path="entityId"/>
    	<html:input type="hidden" id="useEntityServer" path="useEntityServer"/>
    	
    	<div>
			<p class="portlet-msg-info" style="font-weight:bold;">
    			<img title='' alt='' src="<html:imagesPath/>puce_e.gif"/>
    			<fmt:message key="news.label.cmisserver.entity.change"/> :
    		</p>  
			<table border="0" cellpadding="4">
				<tr>   		
		    		<td class="portlet-form-field-label">
			    		 <spring:bind path="useEntityServer">
			    		    <input type="radio" id="${namespace}source1" name="${namespace}source" value="app" onclick="changeOptionsSrce('app');"> <fmt:message key="news.label.cmisserver.from.app"/><br>
			    		    <input type="radio" id="${namespace}source2" name="${namespace}source" value="entity" onclick="changeOptionsSrce('entity');"> <fmt:message key="news.label.cmisserver.from.entity"/><br>
							<c:set var="display" value="${status.value}"/>
						</spring:bind>
					</td>
				</tr>
			</table>    
			<table border="0" cellpadding="4" id="editEntityOptions"  <c:choose>
				<c:when test="${not empty errors}">style="display:block;"</c:when>
				<c:otherwise>style="display:none;"</c:otherwise>
			</c:choose>>
				<tr>
					<td class="portlet-form-label">
						<label for="serverUrl"><fmt:message key="news.label.cmisserver.url"/><span class="portlet-msg-alert">*</span> : </label>
					</td> 
    				<td class="portlet-form-field-label">
    					<html:input type="text" id="serverUrl" size="30" path="serverUrl"/>
    				</td>
    			</tr>
    			<tr>
	    			<td class="portlet-form-label">
	    				<label for="serverLogin"><fmt:message key="news.label.cmisserver.login"/><span class="portlet-msg-alert">*</span> : </label>
	    			</td>
	    			<td class="portlet-form-field-label">
    					<html:input type="text" id="serverLogin" size="30" path="serverLogin"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
    					<label for="serverPwd"><fmt:message key="news.label.cmisserver.pwd"/><span class="portlet-msg-alert">*</span> : </label>
    				</td>
    				<td class="portlet-form-field-label">
    					<html:input type="password" id="serverPwd" size="30" path="serverPwd"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
    					<label for="serverPwd"><fmt:message key="news.label.cmisserver.pwd2"/><span class="portlet-msg-alert">*</span> : </label>
    				</td>
    				<td class="portlet-form-field-label">
    					<html:input type="password" id="serverPwd2" path="serverPwd2" size="30"/>
    				</td>
    			</tr>
    			<tr>
    				<td class="portlet-form-label">
    					<label for="repositoryId"><fmt:message key="news.label.cmisserver.repo.id"/><span class="portlet-msg-alert">*</span> :</label> 
    				</td>
    				<td class="portlet-form-field-label">
    					<html:input type="text" id="repositoryId" size="30" path="repositoryId"/>
    				</td>
    			</tr>    	
    			<tr>	
    				<td colspan="2" align="center">  
    					<input type="submit" id="${namespace}submitButton" value="<fmt:message key="button.save"/>"/>&nbsp; 
    					<input type="button" value="<fmt:message key="button.cancel"/>" onclick="window.location.href='${viewAttachmentConfRenderUrl}';"/>
    				</td>
    			</tr>
    		</table>
    	</div>

  </form>
    
</spring:nestedPath>
	
</fieldset>
<p/><div class="news_legende"> 
<p class="portlet-font"><fmt:message key="news.label.legende"/> :<br/>
<span class="portlet-msg-alert">* <fmt:message key="news.legend.field_required"/></span>
</div></p>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>