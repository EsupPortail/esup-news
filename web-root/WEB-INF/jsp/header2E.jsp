<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerE">
	<IMG alt="<fmt:message key='news.label.entity'/>" src="<html:imagesPath/>ic_entity.gif" align="absMiddle"> 
	<span class="titreE"><c:out value="${entity.name}" /></span></div>	
	
<div id="news_headerSM">
<ul>

	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif"
		width="30"></li>
	<li <c:if test='${aN=="viewEntity"}'>id="currentE" </c:if>><a
		href="${viewEntityRenderUrl}"><fmt:message
		key="menu.categories" /> </a></li>
	<c:if test='${pMask ge rM}'>
		<li <c:if test='${aN=="newCat"}'>id="currentE" </c:if>><a
			href="${newCatRenderUrl}" class="portlet-menu-item" /> <fmt:message
			key="menu.newCat" /> </a></li>
		<li <c:if test='${aN=="viewEntitySetting"}'>id="currentE" </c:if>><a
			href="${viewEntitySettingRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.setting" /> </A></li>

		<li <c:if test='${aN=="viewPerm"}'>id="currentE" </c:if>><A
			class="portlet-menu-item" href="${viewEntityPermissionRenderUrl}"><fmt:message
			key="menu.permission" /></A></li>

	</c:if>
	<c:if test='${pMask ge rS}'>
		<li <c:if test='${aN=="viewAudience"}'>id="currentE" </c:if>><a
			href="${viewEntityAudienceRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.audience.default" /></A></li>
		<li <c:if test='${aN=="viewEntityFilter"}'>id="currentE" </c:if>><a
			href="${viewEntityFilterRenderUrl}" class="portlet-menu-item"><fmt:message
			key="menu.filter" /></A></li>
		<c:if test="${attActivate eq 'true'}">
		<li <c:if test='${aN=="attachmentConf"}'>id="currentE" </c:if>>
			<a href="${viewEntityAttachmentConfRenderUrl}" class="portlet-menu-item"> <fmt:message key="menu.viewAttachmentOptions" /> </A>
	    </li>
	    </c:if>
	</c:if>

</ul>
</div>

