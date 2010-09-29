<portlet:renderURL var="editCatRenderUrl">
	<portlet:param name="action" value="editCategory" />
	<portlet:param name="cId" value="${category.categoryId}" />
</portlet:renderURL>

<portlet:renderURL var="newItemRenderUrl">
	<portlet:param name="action" value="addItem" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>

<portlet:renderURL var="viewCatSettingRenderUrl">
	<portlet:param name="action" value="viewCategorySetting" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>

<portlet:renderURL var="viewTopicsRenderUrl">
	<portlet:param name="action" value="topicsView" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>

<portlet:renderURL var="newTopicRenderUrl">
	<portlet:param name="action" value="addTopic" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>

<portlet:renderURL var="catAudienceRenderUrl">
	<portlet:param name="action" value="viewAudienceC" />
	<c:if test="${not empty category}"><portlet:param name="ctxId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="ctxId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
<portlet:renderURL var="addCAudienceRenderUrl">
	<portlet:param name="action" value="addAudience" />
	<portlet:param name="ctx" value="C" />
	<c:if test="${not empty category}"><portlet:param name="ctxId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="ctxId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
<portlet:renderURL var="catPermissionRenderUrl">
	<portlet:param name="action" value="viewPermissionC" />
	<c:if test="${not empty category}"><portlet:param name="ctxId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="ctxId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
<portlet:renderURL var="addCPermissionRenderUrl">
	<portlet:param name="action" value="addPermission" />
	<portlet:param name="ctx" value="C" />
	<c:if test="${not empty category}"><portlet:param name="ctxId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="ctxId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
<portlet:renderURL var="feedRSSUrl">
	<portlet:param name="action" value="xmlTopicsFeeds" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
