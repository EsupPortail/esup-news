<portlet:renderURL var="viewItemListRenderUrl">
	<portlet:param name="action" value="viewItemList" />
	<portlet:param name="tId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="archivedItemsRenderUrl">
	<portlet:param name="action" value="viewTopic" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="2" />
</portlet:renderURL>
<portlet:renderURL var="scheduledItemsRenderUrl">
	<portlet:param name="action" value="viewTopic" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="3" />
</portlet:renderURL>
<portlet:renderURL var="viewCatFromTopicRenderUrl">
	<portlet:param name="action" value="viewCategory" />
	<portlet:param name="cId" value="${topic.categoryId}" />
</portlet:renderURL>
<portlet:renderURL var="viewPendingItemsRenderUrl">
	<portlet:param name="action" value="viewTopic" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="0" />
</portlet:renderURL>
<portlet:renderURL var="validateItemRenderUrl">
	<portlet:param name="action" value="validateItem" />
	<portlet:param name="id" value="${itemId}" />
</portlet:renderURL>
<portlet:renderURL var="editTopicRenderUrl">
	<portlet:param name="action" value="editTopic" />
	<portlet:param name="tId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="addTopicRenderUrl">
	<portlet:param name="action" value="addTopic" />
	<portlet:param name="cId" value="${topic.categoryId}" />
</portlet:renderURL>
<portlet:renderURL var="topicSettingRenderUrl">
	<portlet:param name="action" value="viewTopicSetting" />
	<portlet:param name="tId" value="${topic.topicId}" />

</portlet:renderURL>
<portlet:renderURL var="addItemFTRenderUrl">
	<portlet:param name="action" value="addItem" />
	<portlet:param name="cId" value="${topic.categoryId}" />
	<portlet:param name="tId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="viewTopicAudienceRenderUrl">
	<portlet:param name="action" value="viewAudienceT" />
	<!--  <portlet:param name="ctx" value="T"/>  -->
	<portlet:param name="ctxId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="addTAudienceRenderUrl">
	<portlet:param name="action" value="addAudience" />
	<portlet:param name="ctx" value="T" />
	<portlet:param name="ctxId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="viewTopicPermissionRenderUrl">
	<portlet:param name="action" value="viewPermissionT" />
	<portlet:param name="ctxId" value="${topic.topicId}" />
</portlet:renderURL>
<portlet:renderURL var="addTPermissionRenderUrl">
	<portlet:param name="action" value="addPermission" />
	<portlet:param name="ctxId" value="${topic.topicId}" />
	<portlet:param name="ctx" value="T" />
</portlet:renderURL>