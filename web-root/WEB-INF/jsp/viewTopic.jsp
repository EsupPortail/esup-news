<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<%@ include file="/WEB-INF/jsp/submenu_t.jsp" %>
<c:choose>
	<c:when test="${s eq '1'}">
		<c:set var="aN" value="viewTopic" />
		<c:set var="titleKey" value="view.topic.publishedItems.page.title" />
	</c:when>
	<c:when test="${s eq '2'}">
		<c:set var="aN" value="archivedItems" />
		<c:set var="titleKey" value="view.topic.archivedItems.page.title" />
	</c:when>
	<c:when test="${s eq '3'}">
		<c:set var="aN" value="scheduledItems" />
		<c:set var="titleKey" value="view.topic.scheduledItems.page.title" />
	</c:when>
	<c:otherwise>
		<c:set var="aN" value="viewPending" />
		<c:set var="titleKey" value="view.topic.pendingItems.page.title" />
	</c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/jsp/header2T.jsp" %>
<div id="news_clear"></div>
<portlet:renderURL var="baseRenderUrl">
	<portlet:param name="action" value="viewItem" />
	<portlet:param name="id" value="${item.itemId}" />
	<portlet:param name="tId" value="${topic.topicId}"/>
</portlet:renderURL>
<portlet:renderURL var="userDetailsURL">
	<portlet:param name="action" value="userDetails" />
	<portlet:param name="tId" value="${topic.topicId}"/>
    <portlet:param name="cId" value="${topic.categoryId}"/>
</portlet:renderURL>
<portlet:actionURL var="upURL">
	<portlet:param name="action" value="incrementItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="increment" value="1" />
</portlet:actionURL>
<portlet:actionURL var="upTopURL">
	<portlet:param name="action" value="incrementItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="increment" value="2" />
</portlet:actionURL>
<portlet:actionURL var="downURL">
	<portlet:param name="action" value="incrementItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="increment" value="-1" />
</portlet:actionURL>
<portlet:actionURL var="downBottomURL">
	<portlet:param name="action" value="incrementItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="increment" value="-2" />
</portlet:actionURL>

<portlet:actionURL var="delURL">
	<portlet:param name="action" value="deleteItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="${s}" />
	<portlet:param name="all" value="0" />
</portlet:actionURL>
<portlet:actionURL var="rDelURL">
	<portlet:param name="action" value="deleteItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="${s}" />
	<portlet:param name="all" value="1" />
</portlet:actionURL>
<portlet:actionURL var="apprURL">
	<portlet:param name="action" value="apprItem" />
	<portlet:param name="tId" value="${topic.topicId}" />
</portlet:actionURL>
<br />
<c:set var="tit">
	<fmt:message key="news.label.item" />
</c:set>
<fieldset>
<legend> <fmt:message
	key="${titleKey}" /> </legend>
<p /><c:if test="${not empty itemList}">
	<div class="texteBasPage">[<a
		href="${portalURL}/feeds/private/item?c=2&tID=${topic.topicId}&s=${s}"
		target="_blank" class="liensmenudroit"><img
		src="<html:imagesPath/>print.gif" alt="" align="absmiddle" border="0" /><fmt:message
		key="menu.items.print" /></a>]</div>
</c:if> <d:table name="${itemList}" export="false" id="row" class="dataTable"
	pagesize="${nbItemsToShow}" cellspacing="1">
	<d:column property="itemId" title="ID" class="hidden"
		headerClass="hidden" media="html" />

	<c:choose>
		<c:when test="${s=='1'}">
			<d:column property="title" titleKey="item.label.title"
				href="${baseRenderUrl}" paramId="id" paramProperty="itemId" />
			<d:column titleKey="item.label.created.by" href="${userDetailsURL}" paramId="uid"
				paramProperty="postedBy"><c:choose>
				<c:when
					test="${!empty userList[row.postedBy] && userList[row.postedBy].foundInLdap}">
					<c:out value="${userList[row.postedBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param value="${row.postedBy}" />
					</fmt:message>
				</c:otherwise>
			</c:choose></d:column>
			<d:column property="postDate" titleKey="item.label.created.date"
				format="{0,date,long}" />
			<d:column property="startDate" titleKey="item.label.start.time"
				format="{0,date,long}" />
			<d:column property="endDate" titleKey="item.label.end.time"
				format="{0,date,long}" />
			<d:column titleKey="item.label.updated.by" href="${userDetailsURL}" paramId="uid"
				paramProperty="lastUpdatedBy"><c:choose>
				<c:when
					test="${!empty userList[row.lastUpdatedBy] && userList[row.lastUpdatedBy].foundInLdap}">
					<c:out value="${userList[row.lastUpdatedBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param><c:out value="${row.lastUpdatedBy}" /></fmt:param>
					</fmt:message>
				</c:otherwise>
			</c:choose></d:column>
			<d:column property="lastUpdatedDate"
				titleKey="item.label.updated.time" format="{0,date,long}" />
			<c:if test='${pMask ge rM}'>
				<c:choose>
					<c:when test="${!(row_rowNum eq 1)}"><d:column href="${upURL}" paramId="id" paramProperty="itemId">
						<img
							title="<fmt:message key='news.img.title.increase.disp.order'/>"
							src="<html:imagesPath/>up.gif" border=0 />
					</d:column></c:when>
					<c:otherwise>
						<d:column></d:column>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${!(row_rowNum eq 1)}"><d:column href="${upTopURL}" paramId="id" paramProperty="itemId">
					<img
						title="<fmt:message key='news.img.title.increase.disp.order'/>"
						src="<html:imagesPath/>double_up.gif" border=0 />
				</d:column></c:when>
					<c:otherwise>
						<d:column></d:column>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not (row_rowNum eq fn:length(itemList))}"><d:column href="${downURL}" paramId="id" paramProperty="itemId">
					<img
						title="<fmt:message key='news.img.title.decrease.disp.order'/>"
						src="<html:imagesPath/>down.gif" border=0 />
				</d:column></c:when>
					<c:otherwise>
						<d:column></d:column>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not (row_rowNum eq fn:length(itemList))}"><d:column href="${downBottomURL}" paramId="id" paramProperty="itemId">
					<img
						title="<fmt:message key='news.img.title.decrease.disp.order'/>"
						src="<html:imagesPath/>double_down.gif" border=0 />
				</d:column></c:when>
					<c:otherwise>
						<d:column></d:column>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:when>
		<c:otherwise>
			<d:column property="title" titleKey="item.label.title"
				href="${baseRenderUrl}" paramId="id" paramProperty="itemId"
				sortable="true" headerClass="sortable"  />
			<d:column titleKey="item.label.created.by" href="${userDetailsURL}" paramId="uid"
				paramProperty="postedBy" sortable="true" headerClass="sortable" ><c:choose>
				<c:when
					test="${!empty userList[row.postedBy] && userList[row.postedBy].foundInLdap}">
					<c:out value="${userList[row.postedBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param value="${row.postedBy}" />
					</fmt:message>
				</c:otherwise>
			</c:choose></d:column>
			<d:column property="postDate" titleKey="item.label.created.date"
				format="{0,date,long}" sortable="true" headerClass="sortable" />
			<d:column property="startDate" titleKey="item.label.start.time"
				format="{0,date,long}" sortable="true" headerClass="sortable" />
			<d:column property="endDate" titleKey="item.label.end.time"
				format="{0,date,long}" sortable="true" headerClass="sortable" />
			<d:column titleKey="item.label.updated.by" href="${userDetailsURL}" paramId="uid"
				paramProperty="lastUpdatedBy" sortable="true" headerClass="sortable" ><c:choose>
				<c:when
					test="${!empty userList[row.lastUpdatedBy] && userList[row.lastUpdatedBy].foundInLdap}">
					<c:out value="${userList[row.lastUpdatedBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param><c:out value="${row.lastUpdatedBy}" /></fmt:param>
					</fmt:message>
				</c:otherwise>
			</c:choose></d:column>
			<d:column property="lastUpdatedDate" titleKey="item.label.updated.time" 
				format="{0,date,long}" sortable="true" headerClass="sortable" />
		</c:otherwise>
	</c:choose>
	<c:if test='${pMask ge rM}'>
		<portlet:renderURL var="editURL">
			<portlet:param name="action" value="editItem" />
			<portlet:param name="tId" value="${topic.topicId}" />
			<portlet:param name="cId" value="${topic.categoryId}" />
		</portlet:renderURL>
		<d:column href="${editURL}" paramId="id" paramProperty="itemId">
			<img title='<fmt:message key="news.img.title.edit.item"/>'
				src="<html:imagesPath/>edit.gif" border=0 />
		</d:column>
		<c:if test="${s!='2'}">
			<d:column href="${apprURL}" paramId="id" paramProperty="itemId">
				<c:choose>
					<c:when test="${row.status == 1}">
						<img title="<fmt:message key='button.invalidate'/>"
							src="<html:imagesPath/>valide.gif" border=0 />
					</c:when>
					<c:otherwise>
						<img title="<fmt:message key='button.validate'/>"
							src="<html:imagesPath/>invalide.gif" border=0 />
					</c:otherwise>
				</c:choose>
			</d:column>
		</c:if>
		<d:column href="${delURL}" paramId="id" paramProperty="itemId">
			<img title="<fmt:message key='news.img.title.dissociate.item'/>"
			onClick="return confirm('<fmt:message key="news.alert.dissociate.item"/>');"
				src="<html:imagesPath/>poubelle.gif" border=0 />
		</d:column>
		<d:column href="${rDelURL}" paramId="id" paramProperty="itemId">
			<img title="<fmt:message key='news.alert.delete.item'/>"
			onClick="return confirm('<fmt:message key="news.alert.delete.item"/>');"
				src="<html:imagesPath/>delete.gif" border=0 />
		</d:column>
	</c:if>
	<d:setProperty name="paging.banner.placement" value="bottom" />
	<d:setProperty name="paging.banner.item_name" value="${tit}" />
	<d:setProperty name="paging.banner.items_name" value="${tit}s" />
</d:table>
<p /><c:if test="${(pMask ge rM) && (not empty itemList)}">
	<div class="news_legende">
	<p class="portlet-font"><fmt:message key="news.label.legende" /> :<br />
	<img src="<html:imagesPath/>valide.gif" border=0 /> <fmt:message
		key="news.item.validated" /> &nbsp; &nbsp; &nbsp; &nbsp; <img
		src="<html:imagesPath/>invalide.gif" border=0 /> <fmt:message
		key="news.item.invalidated" /></p>
	</div>
</c:if>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>
</html>