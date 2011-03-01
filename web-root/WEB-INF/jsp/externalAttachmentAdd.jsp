<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script type="text/javascript">
	function checkFileName(){
		var list = "<c:out value="${existingFileNames}"/>";
		if(list == ""){
			document.getElementById("${namespace}okButton").click();
		} else {
			var current = document.getElementById("${namespace}file").value;
			var regexp = "^(.*)(,)?"+ current +"(,)?(.*)$";
			if(list.search(regexp) > -1)	{
				if(confirm("<fmt:message key="ITEM_FILE_ALREADY_UPLOADED"/>")){
					document.getElementById("${namespace}okButton").click();
				}
			} else {
				document.getElementById("${namespace}okButton").click();
			}
			return false;
		}
	}
</script>
<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerC">
	<!-- empty -->
</div>
<div id="news_headerSM">
<ul>
	<!-- empty -->
</ul>
</div>

<br/>
<html:errors path="itemForm" fields="true" />
<spring:nestedPath path="itemForm">
<fieldset><legend><fmt:message key="news.label.attachment.addAFile.pageTitle" /></legend>
<form method="post" enctype="multipart/form-data" 
		action="<portlet:actionURL>
                        <portlet:param name="action" value="addItem"/>
                        <c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
						<c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if>
                        <portlet:param name="_page" value="${page}"/>
               	</portlet:actionURL>">
               	
	<!-- Data for the new attachment -->
	<table border="0" cellpadding="4" width="95%">
		<tr>
			<td class="portlet-form-label"><fmt:message key="news.label.attachment.title"/><span class="portlet-msg-alert">*</span> : </td>
			<td class="portlet-form-field-label">
				<html:input path="external.title" type="text" class="input"  size="69" maxlength="150" />
			</td>
		</tr>
		<tr>
			<td class="portlet-form-label"><fmt:message key="news.label.attachment.desc"/> : </td>
			<td class="portlet-form-field-label">
				<html:textarea path="external.desc" rows="3" cols="80" class="input" />
			</td>
		<tr>
			<td class="portlet-form-label"><fmt:message key="news.label.attachment.file"/><span class="portlet-msg-alert">*</span> : </td>
			<td class="portlet-form-field-label">
				<html:input path="external.file" id="${namespace}file" type="file" class="input" size="69"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<!-- Buttons -->
				<input type="button" value="<fmt:message key="button.add"/>" onclick="checkFileName()"/> 
				<input type="submit" id="${namespace}okButton" name="_target0" value="<fmt:message key="button.add"/>" style="display:none;"/>
				<input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>			
			</td>
		</tr>
	</table>
</form>	

</fieldset>
</spring:nestedPath>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>