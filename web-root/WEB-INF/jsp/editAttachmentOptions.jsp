<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css" href="${ctxPath}/css/displaytag.css">

<script type="text/javascript">


	function addToAuthorizedList()
	{
		var ext = document.getElementById('newAuthorizedExt').value;
		if(ext.indexOf('.') != -1)
		{
			ext = ext.substring(ext.indexOf('.')+1, ext.lenght);
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
			ext = ext.substring(ext.indexOf('.')+1, ext.lenght);
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
    function getExtensionsValues()
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

	  	return true;
    }
</script>


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
	
<html:errors path="attachmentOptionsForm" fields="true" />
<spring:nestedPath path="attachmentOptionsForm">
	
	<form name="${namespace}attachmentOptions" method="post"
		  action="<portlet:actionURL>
                        <portlet:param name="action" value="editAttachmentOptions"/>
               		</portlet:actionURL>">   
    		           		 
    	<html:input type="hidden"  path="optionsId"/>
    	
    	<div id="${namespace}changeOptionsArea">
			<p class="portlet-msg-info" style="font-weight:bold;">
    			<img title='' alt='' src="<html:imagesPath/>puce_adm.gif"/>
    			<fmt:message key="news.label.attachment.prop.update"/> :
    		</p>   
			<table border="0" cellpadding="4">
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
						<input type="submit" value="<fmt:message key="button.save"/>" onclick="getExtensionsValues();"/>
						<input type="button" value="<fmt:message key="button.cancel"/>" 
							onclick="window.location.href='${viewAttachmentConfRenderUrl}';"/>
					</td>
				</tr> 
			</table>  		
    	</div>
    </form>
    
</spring:nestedPath>
	
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>