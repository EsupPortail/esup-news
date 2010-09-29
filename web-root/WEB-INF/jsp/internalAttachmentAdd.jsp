<%@ include file="/WEB-INF/jsp/include.jsp"%>

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

<script type="text/javascript">
	function submit(url)
	{
		document.forms["${namespace}InternalAttachmentForm"].action=url;
		document.forms["${namespace}InternalAttachmentForm"].submit();
	}
</script>

<fieldset><legend><fmt:message key="news.label.attachment.addExistingFile.pageTitle" /></legend>
<form name="${namespace}InternalAttachmentForm" method="post" 
	  action="<portlet:actionURL>
                        <portlet:param name="action" value="addItem"/>
                        <c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
						<c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if>
                        <portlet:param name="_page" value="${page}"/>
               	</portlet:actionURL>"">

	<!-- To search an attachment from an existing news -->
	<div class="breadcrumb">
		<!-- Toutes les categories -->
		<a href="javascript:;" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><c:if test="${not empty topic}"><portlet:param name="tid" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');">Toutes les catégories</a>
		 
		<!-- Categorie selectionnee -->
		<c:if test="${not empty selectedCategory}">
			&gt; <a href="javascript:;" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="categoryId" value="${selectedCategory.categoryId}" /><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>')"><c:out value="${selectedCategory.name}"/></a>
		</c:if>
		
		<!-- Topic selectionne -->
		<c:if test="${not empty selectedTopic}">
			&gt; <a href="javascript:;" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="topicId" value="${selectedTopic.topicId}" /><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');"><c:out value="${selectedTopic.name}"/></a>
		</c:if>
		
		<!-- item selectionne -->
		<c:if test="${not empty selectedItem}">
			&gt; <a href="javascript:;" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="itemId" value="${selectedItem.itemId}" /><portlet:param name="selectedTopic" value="${selectedTopic.topicId}" /><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');"><c:out value="${selectedItem.title}"/></a>
		</c:if>
		
	</div>
	<c:if test="${fn:length(categories) > 0}">
		<table class="internal_attachment_table">
			<thead>
				<tr>
					<th><!-- empty --></th>
					<th><fmt:message key="news.label.attachment.type" /></th>
					<th><fmt:message key="news.label.attachment.folderName.cat"/></th>
					<th><fmt:message key="news.label.attachment.desc"/></th>
				</tr>		
			</thead>
			<tbody>
			<c:forEach items="${categories}" var="cat" varStatus="status">
			<tr>
				<td class="internal_attachment_check"><!-- empty --></td>
				<td class="internal_attachment_type"><img src="<html:imagesPath/>icone_cat_p2.gif"/></td>
				<td class="internal_attachment_name"><a href="#" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="categoryId" value="${cat.categoryId}"/><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');"><c:out value="${cat.name}"/></a></td>
				<td class="internal_attachment_desc"><c:out value="${cat.desc}"/></td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:if>
			
	<c:if test="${fn:length(topics) > 0}">
		<table class="internal_attachment_table">
			<thead>
				<tr>
					<th><!-- empty --></th>
					<th><fmt:message key="news.label.attachment.type" /></th>
					<th><fmt:message key="news.label.attachment.folderName.topic"/></th>
					<th><fmt:message key="news.label.attachment.desc"/></th>
				</tr>		
			</thead>
			<tbody>
			<c:forEach items="${topics}" var="topicEntry" varStatus="status">
			<tr>
				<td class="internal_attachment_check"><!-- empty --></td>
				<td class="internal_attachment_type"><img src="<html:imagesPath/>icone_cat_p2.gif"/></td>
				<td class="internal_attachment_name"><a href="#" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="topicId" value="${topicEntry.topicId}"/><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');"><c:out value="${topicEntry.name}"/></a>
					(${topicEntry.count} <fmt:message key="news.label.attachment.itemsWithFiles"/>)
				</td>
				<td class="internal_attachment_desc"><c:out value="${topicEntry.desc}"/></td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:if>
			
	<c:if test="${fn:length(items) > 0}">
		<table class="internal_attachment_table">
			<thead>
				<tr>
					<th><!-- empty --></th>
					<th><fmt:message key="news.label.attachment.type" /></th>
					<th><fmt:message key="news.label.attachment.folderName.item"/></th>
					<th><fmt:message key="news.label.attachment.desc"/></th>
					<th><fmt:message key="news.label.attachment.creationDate"/></th>
					<th><fmt:message key="news.label.attachment.author"/></th>
				</tr>		
			</thead>
			<tbody>
			<c:forEach items="${items}" var="itemEntry" varStatus="status">
			<tr>
				<td class="internal_attachment_check"><!-- empty --></td>
				<td class="internal_attachment_type"><img src="<html:imagesPath/>icone_cat_p2.gif"/></td>
				<td class="internal_attachment_name"><a href="#" onclick="submit('<portlet:actionURL><portlet:param name="action" value="addItem"/><portlet:param name="_page" value="${page}"/><portlet:param name="itemId" value="${itemEntry.itemId}"/><portlet:param name="selectedTopic" value="${selectedTopic.topicId}"/><c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if><c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if></portlet:actionURL>');"><c:out value="${itemEntry.title}"/></a></td>
				<td class="internal_attachment_desc"><c:out value="${itemEntry.summary}"/></td>
				<td class="internal_attachment_date"><fmt:formatDate pattern="${datePattern}" value="${itemEntry.postDate}"/></td>
				<td class="internal_attachment_author"><c:out value="${itemEntry.postedBy}"/></td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:if>
			
	<c:if test="${fn:length(attachments) > 0}">
		<table class="internal_attachment_table">
			<thead>
				<tr>
					<th><!-- empty --></th>
					<th><fmt:message key="news.label.attachment.type" /></th>
					<th><fmt:message key="news.label.attachment.fileName"/></th>
					<th><fmt:message key="news.label.attachment.desc"/></th>
					<th><fmt:message key="news.label.attachment.size"/></th>
					<th><fmt:message key="news.label.attachment.date"/></th>
					<th><fmt:message key="news.label.attachment.author"/></th>
				</tr>		
			</thead>
			<tbody>
			<c:forEach items="${attachments}" var="attEntry" varStatus="status">
			<tr>
				<td class="internal_attachment_check"><input type="checkbox" name="attachmentIds" value="${attEntry.attachmentId}"/><!-- empty --></td>
				<td class="internal_attachment_type"><img src="<html:imagesPath/>types/${fn:substring(attEntry.fileName, fn:indexOf(attEntry.fileName, '.')+1, fn:length(attEntry.fileName))}.png"/></td>
				<td class="internal_attachment_name"><c:out value="${attEntry.title} (${attEntry.fileName})"/></td>
				<td class="internal_attachment_desc"><c:out value="${attEntry.description}"/></td>
				<td class="internal_attachment_size"><c:if test="${attEntry.size != null}"><fmt:formatNumber value="${attEntry.size div 1024}" maxFractionDigits="1"/> Ko</c:if></td>
				<td class="internal_attachment_date"><fmt:formatDate pattern="${datePattern}" value="${attEntry.insertDate}"/></td>
				<td class="internal_attachment_author"></td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:if>
			
	<input type="submit" name="_target0" value="Joindre les fichiers" <c:if test="${empty attachments}">disabled="disabled"</c:if>/>
	<input type="submit" name="_cancel" value="Annuler"/>
</form>
</fieldset>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>