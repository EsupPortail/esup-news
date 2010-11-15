<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerC">
	<IMG alt="<fmt:message key='news.label.entity'/>" src="<html:imagesPath/>ic_entity.gif" align="absMiddle"> 
	<span class="titreE"><a href="${viewEntityRenderUrl}"> <c:out value="${entity.name}" /></a></span>
	<IMG alt="<fmt:message key='news.label.category'/>"	src="<html:imagesPath/>ic_cat.gif" align="absMiddle"> 
	<span class="titreC"> <c:if test="${not empty category }"><c:out value="${category.name}" /></c:if>
	<c:if test="${not empty categoryForm }"><c:out value="${categoryForm.category.name}" /></c:if></span></div>
<div id="news_headerSM">
<ul>
	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif"
		width="50"></li>
	<li <c:if test='${aN=="viewCat"}'>id="current" </c:if>><a
		href="${viewCatRenderUrl}"><fmt:message
		key="menu.topics" /> </a></li>
	<c:if test='${pMask ge rC}'>
		<li <c:if test='${aN=="newItem"}'>id="current" </c:if>><a
			href="${newItemRenderUrl}" class="portlet-menu-item" /> <fmt:message
			key="menu.newItem" /> </a></li>
	</c:if>
	<c:if test='${pMask ge rM}'>
		<li <c:if test='${aN=="newTopic"}'>id="current" </c:if>><a
			href="${newTopicRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.newTopic" /></A></li>

		<li <c:if test='${aN=="viewCatSetting"}'>id="current" </c:if>><a
			class="portlet-menu-item" href="${viewCatSettingRenderUrl}"><fmt:message
			key="menu.setting" /> </a></li>
		<li <c:if test='${aN=="viewPerm"}'>id="current" </c:if>><A
			class="portlet-menu-item" href="${catPermissionRenderUrl}"><fmt:message
			key="menu.permission" /> </A></li>
		<li <c:if test='${aN=="viewAudience"}'>id="current" </c:if>><a
			href="${catAudienceRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.audience" /> </A></li>
	</c:if>
	<li <c:if test='${aN=="viewRss"}'>id="current" </c:if>><a
		class="portlet-menu-item" href="${feedRSSUrl}"><fmt:message
		key="menu.rss" /> </a></li>
</ul>
</div>

