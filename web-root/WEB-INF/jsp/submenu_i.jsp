<portlet:renderURL var="editItemRenderUrl">
	<portlet:param name="action" value="editItem" />
	<c:if test="${not empty item}"><portlet:param name="iId" value="${item.itemId}" /></c:if>
	<c:if test="${not empty itemForm}"><portlet:param name="iId" value="${itemForm.item.itemId}" /></c:if>
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if>
</portlet:renderURL>