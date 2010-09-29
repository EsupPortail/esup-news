<portlet:renderURL var="viewEntitySettingRenderUrl">
	<portlet:param name="action" value="viewEntitySetting" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="editEntityRenderUrl">
	<portlet:param name="action" value="editEntity" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>

<portlet:renderURL var="viewEntityAttachmentConfRenderUrl">
	<portlet:param name="action" value="viewEntityAttachmentConf" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="editEntityAttachmentOptionsRenderUrl">
	<portlet:param name="action" value="editEntityAttachmentOptions" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="cancelEntityAttachmentOptionsRenderUrl">
	<portlet:param name="action" value="editEntityAttachmentOptions" />
	<portlet:param name="eId" value="${entity.entityId}" />
	<portlet:param name="cancel" value="cancel" />
</portlet:renderURL>
<portlet:renderURL var="editEntityCmisServerParamsRenderUrl">
	<portlet:param name="action" value="editEntityCmisServerParams" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="cancelEntityCmisServerParamsRenderUrl">
	<portlet:param name="action" value="editEntityCmisServerParams" />
	<portlet:param name="eId" value="${entity.entityId}" />
	<portlet:param name="cancel" value="cancel" />
</portlet:renderURL>

<portlet:renderURL var="newCatRenderUrl">
	<portlet:param name="action" value="addCategory" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="viewCatRenderUrl">
	<portlet:param name="action" value="viewCategory" />
	<portlet:param name="cId" value="${category.categoryId}" />
</portlet:renderURL>
<portlet:renderURL var="editCatRenderUrl">
	<portlet:param name="action" value="editCategory" />
	<portlet:param name="cId" value="${category.categoryId}" />
</portlet:renderURL>

<portlet:renderURL var="viewEntityPermissionRenderUrl">
	<portlet:param name="action" value="viewPermissionE" />
	<portlet:param name="ctxId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="addEntityPermissionRenderUrl">
	<portlet:param name="action" value="addPermission" />
	<portlet:param name="ctxId" value="${entity.entityId}" />
	<portlet:param name="ctx" value="E" />
</portlet:renderURL>

<portlet:renderURL var="viewEntityAudienceRenderUrl">
	<portlet:param name="action" value="viewAudienceE" />
	<portlet:param name="ctxId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="addEntityAudienceRenderUrl">
	<portlet:param name="action" value="addAudience" />
	<portlet:param name="ctxId" value="${entity.entityId}" />
	<portlet:param name="ctx" value="E" />
</portlet:renderURL>

<portlet:renderURL var="viewEntityFilterRenderUrl">
	<portlet:param name="action" value="viewFilters" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="addEntityFilterRenderUrl">
	<portlet:param name="action" value="addFilter" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>