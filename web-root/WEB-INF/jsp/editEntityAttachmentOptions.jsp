<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">

	function displayArea(area)
	{
		document.getElementById(area).style.display="block";
		document.getElementById('${namespace}displayedOptionsArea').style.display="none";

		document.getElementById('${namespace}source1').checked = false;
		document.getElementById('${namespace}source2').checked = false;
	}
	function hideArea(area)
	{
		document.getElementById('${namespace}displayedOptionsArea').style.display="block";
		document.getElementById(area).style.display="none";

		window.location.href='${cancelEntityAttachementOptionsRenderUrl}';
	}

	function addToAuthorizedList()
	{
		var ext = document.getElementById('newAuthorizedExt').value;
		if(ext.indexOf('.') != -1)
		{
			ext = ext.substring(ext.indexOf('.')+1, ext.length);
		}
		var select = document.getElementById("${namespace}AuthorizedList");
		select.options[select.options.length] = new Option(ext, ext);

		document.getElementById('newAuthorizedExt').value = '';		
	}
	function addToForbiddenList()
	{
		var ext = document.getElementById('newForbiddenExt').value;
		if(ext.indexOf('.') != -1)
		{
			ext = ext.substring(ext.indexOf('.')+1, ext.length);
		}
		var select = document.getElementById("${namespace}ForbiddenList");
		select.options[select.options.length] = new Option(ext, ext);

		document.getElementById('newForbiddenExt').value='';				
	}
	function removeSelectedOption(selectList)
	{
	  var elSel = document.getElementById(selectList);
	  var i;
	  for (i = elSel.length - 1; i>=0; i--) {
	    if (elSel.options[i].selected) {
	      elSel.remove(i);
	    }
	  }
	}
    function submitValues()
    {
    	var authSelect = document.getElementById("${namespace}AuthorizedList");
		var authExtList='';
       	var i;
	  	for (i = authSelect.length - 1; i>=0; i--) {
	  	    opt = authSelect.options[i].value;
	  	  	authExtList = authExtList + opt + ";"
	  	}
	  	document.getElementById("authorizedExts").value = authExtList;
	  	
    	var forbSelect = document.getElementById("${namespace}ForbiddenList");
		var forbExtList='';
       	var i;
	  	for (i = forbSelect.length - 1; i>=0; i--) {
	  	    opt = forbSelect.options[i].value;
	  	  forbExtList = forbExtList + opt + ";"
	  	}
	  	document.getElementById("forbiddenExts").value = forbExtList;
	  	document.getElementById("useEntityOptions").value="true";
	  	
	  	return true;
    }

    function changeOptionsSrce(src)
    {
    	if(src == 'app') 
    	{
			// use appalication options
			if(confirm("<fmt:message key="news.label.attachment.prop.confirmMsg"/>"))
			{
				document.getElementById("editEntityOptions").style.display="none";
				document.getElementById("useEntityOptions").value="false";
				//submit form
				document.getElementById("${namespace}attachmentOptions").submit();
			}
			else
			{
				return false;
			}
    	}
    	else if(src == 'entity')
    	{
    		document.getElementById("editEntityOptions").style.display="block";
    		document.getElementById("useEntityOptions").value="true";
			return true;
    	} 	
    }

</script>

<c:set var="aN" value="attachmentConf" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>

<div id="news_clear"></div>

<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>

<br />
<fieldset>
<legend> <fmt:message key="menu.viewEntityAttachmentConf" /> </legend> 
	
<html:errors path="attachmentOptionsForm" fields="true" />
<spring:nestedPath path="attachmentOptionsForm">
	
	<form name="${namespace}attachmentOptions" id="${namespace}attachmentOptions" method="post"
		  action="<portlet:actionURL>
                        <portlet:param name="action" value="editEntityAttachmentOptions"/>
                        <portlet:param name="eId" value="${entity.entityId}" />
               		</portlet:actionURL>">   
               		           		 
    	<html:input type="hidden"  path="optionsId"/>
    	<html:input type="hidden"  path="entityId"/>
   		<html:input type="hidden" id="useEntityOptions" path="useEntityOptions"/>
   		
    	<div id="${namespace}changeOptionsArea">
			<p class="portlet-msg-info" style="font-weight:bold;">
    			<img title='' alt='' src="<html:imagesPath/>puce_e.gif"/>
    			<fmt:message key="news.label.attachment.prop.update"/> :
    		</p>   			
			<table border="0" cellpadding="4">
				<tr>   		
		    		<td class="portlet-form-field-label">
			    		 <spring:bind path="useEntityOptions">
			    		    <input type="radio" id="${namespace}source1" name="${namespace}source" value="app" onclick="changeOptionsSrce('app');"> <fmt:message key="news.label.attachment.prop.from.app"/><br>
			    		    <input type="radio" id="${namespace}source2" name="${namespace}source" value="entity" onclick="changeOptionsSrce('entity');"> <fmt:message key="news.label.attachment.prop.from.entity"/><br>
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
					<td class="portlet-form-label"><fmt:message key="news.label.attachment.maxSize"/><span class="portlet-msg-alert">*</span> :</td> 
    				<td class="portlet-form-field-label">
    					<html:input type="text" id="maxSize" path="maxSize" size="20"/>
    				</td>
    			</tr>
    			<tr>
					<td class="portlet-form-label" style="vertical-align:top;"><fmt:message key="news.label.attachment.authorisedExts"/> : </td>
					<td class="portlet-form-field-label">
			    		<p style="font-weight:normal;">
			    			<fmt:message key="news.label.attachment.add.ext"/> : <input type="text" id="newAuthorizedExt" size="20" />&nbsp;
			    			<input type="button" value="<fmt:message key="button.add"/>"  onclick="addToAuthorizedList();"/>&nbsp;
			    		</p>
			    		<p style="font-weight:normal;">
			    			<spring:bind path="authorizedList">
							<select id="${namespace}AuthorizedList" size="5" style="width:150px;">
								<c:forEach items="${status.value}" var="extension">
									<option value="${extension}"><c:out value="${extension}"/></option>
								</c:forEach>
							</select> 
							</spring:bind>
							<html:input type="hidden"  path="authorizedExts" id="authorizedExts"/>
			    		</p>
			    		<p style="font-weight:normal;">
			    			<fmt:message key="news.label.attachment.del.ext"/> :&nbsp;
			    			<input type="button" id="deleteAuthorizedButton" value="<fmt:message key="button.delete"/>"  
			    				   onclick="removeSelectedOption('${namespace}AuthorizedList');"/>&nbsp;
			    		</p>  
			    	</td>
			    </tr>
				<tr>
					<td class="portlet-form-label" style="vertical-align:top;"><fmt:message key="news.label.attachment.forbiddenExts"/> : </td>
					<td class="portlet-form-field-label">		
			        		<p style="font-weight:normal;">
				    			<fmt:message key="news.label.attachment.add.ext"/> : <input type="text" id="newForbiddenExt" size="20" />&nbsp;
				    			<input type="button" value="<fmt:message key="button.add"/>"  onclick="addToForbiddenList();"/>&nbsp;
				    		</p>
				    		<p style="font-weight:normal;">
				    			<spring:bind path="forbiddenList">
								<select id="${namespace}ForbiddenList" size="5" style="width:150px;">
									<c:forEach items="${status.value}" var="extension">
										<option value="${extension}"><c:out value="${extension}"/></option>
									</c:forEach>
								</select> 
								</spring:bind>
								<html:input type="hidden"  path="forbiddenExts" id="forbiddenExts"/>
				    		</p>
				    		<p style="font-weight:normal;">
				    			<fmt:message key="news.label.attachment.del.ext"/> :&nbsp;
				    			<input type="button" id="deleteForbiddenButton" value="<fmt:message key="button.delete"/>"  
				    				   onclick="removeSelectedOption('${namespace}ForbiddenList');"/>&nbsp;
				    		</p> 
				    </td>
				</tr>
				<tr>
					<td colspan="2" align="center">   
						<input type="submit" value="<fmt:message key="button.save"/>" onclick="submitValues();"/>
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