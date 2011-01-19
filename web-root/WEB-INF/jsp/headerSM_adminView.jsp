<div id="news_headerSM">
<ul>
	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif" width="50"></li>	
	<li <c:if test="${currentHeaderSM eq 'role'}">id="currentA"</c:if>>
		<a href="${viewRolesRenderUrl}" class="portlet-menu-item"> <fmt:message key="menu.roles" /> </a>
	</li>
	<c:if test="${pMask eq rS }">
	<li <c:if test="${currentHeaderSM eq 'manager'}">id="currentA"</c:if>>
		<a href="${viewManagersRenderUrl}" class="portlet-menu-item" />	<fmt:message key="menu.viewManagers" /> </a>
	</li>
	</c:if>
	<c:if test="${pMask eq rS }">
	<li <c:if test="${currentHeaderSM eq 'type'}">id="currentA"</c:if>>
		<a href="${viewTypesRenderUrl}" class="portlet-menu-item"> <fmt:message key="menu.viewTypes" /> </A>
	</li>
	</c:if>
	<c:if test="${attActivate eq 'true' && pMask eq rS}">
	<li <c:if test="${currentHeaderSM eq 'attachmentConf'}">id="currentA"</c:if>>
		<a href="${viewAttachmentConfRenderUrl}" class="portlet-menu-item"> <fmt:message key="menu.viewAttachmentOptions" /> </A>
	</li>
	</c:if>
</ul>
</div>