<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="news_headerT">
	<IMG alt="<fmt:message key='news.label.entity'/>" src="<html:imagesPath/>ic_entity.gif" align="absMiddle"> 
	<span class="titreE"><a href="${viewEntityRenderUrl}"> <c:out value="${entity.name}" /></a></span>
	<IMG alt="<fmt:message key='news.label.category'/>" src="<html:imagesPath/>ic_cat.gif" align="absMiddle"> 
	<span class="titreC"><a href="${viewCatRenderUrl}"> <c:out value="${category.name}" /></a></span> 
	<IMG alt="<fmt:message key='news.label.topic'/>" src="<html:imagesPath/>ic_topic.gif" align="absMiddle"> 
	<span class="titreT"> <c:out value="${topic.name}" /> </span></div>
<div id="news_headerSM">
<ul>

	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif"
		width="30"></li>
	<li <c:if test='${aN=="viewPending"}'>id="currentT" </c:if>><a
		href="${viewPendingItemsRenderUrl}" class="portlet-menu-item"><fmt:message
		key="menu.pendingItems" /> (${topic.pendingCount}) </a></li>
	<li <c:if test='${aN=="viewTopic"}'>id="currentT" </c:if>><a
		href="${viewTopicRenderUrl}"><fmt:message
		key="menu.Items" /> (${topic.count})</a></li>
	<li <c:if test='${aN=="scheduledItems"}'>id="currentT" </c:if>><a
		class="portlet-menu-item" href="${scheduledItemsRenderUrl}"><fmt:message
		key="menu.ScheduledItems" /> (${topic.scheduleCount})</a></li>
	<li <c:if test='${aN=="archivedItems"}'>id="currentT" </c:if>><a
		class="portlet-menu-item" href="${archivedItemsRenderUrl}"><fmt:message
		key="menu.ArchivedItems" /> (${topic.archivedCount})</a></li>
	<li <c:if test='${aN=="newItem"}'>id="currentT" </c:if>><a
		href="${addItemFTRenderUrl}" class="portlet-menu-item"><fmt:message
		key="menu.newItem" /> </a></li>
	<c:if test="${pMask ge rM}" >
		<li <c:if test='${aN=="viewTopicSetting"}'>id="currentT" </c:if>><a
			href="${topicSettingRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.setting" /> </a></li>

		<li <c:if test='${aN=="viewPerm"}'>id="currentT" </c:if>><A
			class="portlet-menu-item" href="${viewTopicPermissionRenderUrl}"><fmt:message
			key="menu.permission" /></a></li>

		<li <c:if test='${aN=="viewAudience"}'>id="currentT" </c:if>><a
			href="${viewTopicAudienceRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.audience" /></a></li>
	</c:if>

</ul>
</div>

