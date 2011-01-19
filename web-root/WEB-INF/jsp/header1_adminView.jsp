<c:if test='${pMask eq rS}'>
	<portlet:renderURL var="viewManagersRenderUrl">
		<portlet:param name="action" value="viewManagers" />
	</portlet:renderURL>
	<portlet:renderURL var="addSuperAdmRenderUrl">
		<portlet:param name="action" value="addSuperAdm" />
	</portlet:renderURL>
	
	
	<portlet:actionURL var="delTypeActionURL">
		<portlet:param name="action" value="deleteType" />
	</portlet:actionURL>
	<portlet:renderURL var="editTypeRenderURL">
		<portlet:param name="action" value="editType" />
	</portlet:renderURL>
	<portlet:actionURL var="editTypeActionURL">
		<portlet:param name="action" value="editType" />
	</portlet:actionURL>
	<portlet:renderURL var="addTypeRenderURL">
		<portlet:param name="action" value="addType" />
	</portlet:renderURL>
	<portlet:actionURL var="addTypeActionURL">
		<portlet:param name="action" value="addType" />
	</portlet:actionURL>
	<portlet:renderURL var="typeDetailsRenderURL">
		<portlet:param name="action" value="viewTypeSetting" />
	</portlet:renderURL>
	<portlet:renderURL var="viewTypesRenderUrl">
		<portlet:param name="action" value="viewTypes" />
	</portlet:renderURL>
	
	
	<portlet:renderURL var="viewAttachmentConfRenderUrl">
		<portlet:param name="action" value="viewAttachmentConf" />
	</portlet:renderURL>
	<portlet:renderURL var="editAttachmentOptionsRenderUrl">
		<portlet:param name="action" value="editAttachmentOptions" />
	</portlet:renderURL>
	<portlet:renderURL var="editCmisServerParamsRenderUrl">
		<portlet:param name="action" value="editCmisServerParams" />
	</portlet:renderURL>

	<portlet:renderURL var="addEntityRenderUrl">
		<portlet:param name="action" value="addEntity" />		
	</portlet:renderURL>
	<portlet:actionURL var="addEntityActionUrl">
		<portlet:param name="action" value="addEntity" />		
	</portlet:actionURL>
	<portlet:actionURL var="editEntityActionURL">
		<portlet:param name="action" value="editEntity" />
	</portlet:actionURL>

	<portlet:actionURL var="addFilterActionUrl">
        <portlet:param name="action" value="addFilter"/>
    </portlet:actionURL>
	<portlet:actionURL var="editFilterActionUrl">
        <portlet:param name="action" value="editFilter"/>
    </portlet:actionURL>

</c:if>
<c:if test='${pMask ge rM}'>
	<portlet:renderURL var="viewRolesRenderUrl">
		<portlet:param name="action" value="viewRoles" />
	</portlet:renderURL>
	<portlet:actionURL var="editCategoryActionUrl">
		<portlet:param name="action" value="editCategory" />
	</portlet:actionURL>
	<portlet:actionURL var="addCategoryActionUrl">
		<portlet:param name="action" value="addCategory" />
	</portlet:actionURL>
</c:if>

<div id="news_header1">
<ul>

	<li <c:if test="${currentMainMenu eq 'help'}">id="current"</c:if>>
		<a href="${helpUrl}"><fmt:message key="menu.help" /></a>
	</li>
	<c:if test='${pMask eq rS}'>
		<li>|</li>
		<li <c:if test="${currentMainMenu eq 'admin'}">id="current"</c:if>>
			<a href="${viewManagersRenderUrl}"><fmt:message key="menu.admin" /></a>
		</li>
	</c:if>
	<li>|</li>
	<li><a href="${homeUrl}"><fmt:message key="button.home" /></a></li>
	
</ul>
</div>