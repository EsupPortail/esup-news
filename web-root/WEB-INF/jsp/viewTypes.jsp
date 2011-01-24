<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>

<div id="news_headerA"> <IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"/> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="type" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>

<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>

<br />
<fieldset>
<legend> <fmt:message
	key="menu.viewTypes" /> </legend> <br />
<c:set var="tit">
	<fmt:message key="news.img.person" />
</c:set> <c:choose>
	<c:when test="${fn:length(typeList) > 0}">
		<d:table name="${typeList}" export="false" id="row" class="dataTable" sort="list"
			pagesize="${nbItemsToShow}" cellspacing="1" defaultorder="ascending" defaultsort="2">
			<d:column property="typeId" title="ID" class="hidden"
				headerClass="hidden" media="html" />
			<d:column href="${typeDetailsRenderURL}" paramId="${portletParamPrefixe}${pltc_target}typId"
				paramProperty="typeId" property="name"
				titleKey="news.label.type.name" sortable="true" />
			<d:column property="description" titleKey="news.label.type.description"
				sortable="true" />
			<d:column href="${editTypeRenderURL}" paramId="${portletParamPrefixe}${pltc_target}typId"
				paramProperty="typeId">
				<img title="<fmt:message key="news.img.title.view.typeDetail"/>"
					src="<html:imagesPath/>edit.gif" border=0 />
			</d:column>
			<d:column href="${delTypeActionURL}" paramId="${portletParamPrefixe}${pltc_target}typId" paramProperty="typeId">
				<img title="<fmt:message key="news.alert.delete.type"/>"
					src="<html:imagesPath/>delete.gif" border=0 
					onClick="return confirm('<fmt:message key="news.alert.delete.type"/>');"/>
			</d:column>
			<d:column>
				<a href="${portalURL}/feeds/pub/rss?t=6&type=${row.name}"
					target="_blank"><img
					title="<fmt:message key='news.img.title.export.type'/>"
					src="<html:imagesPath/>xml.gif" border="0" /></a>
			</d:column>

			<d:setProperty name="paging.banner.placement" value="bottom" />
			<d:setProperty name="paging.banner.item_name" value="${tit}" />
			<d:setProperty name="paging.banner.items_name" value="${tit}s" />
		</d:table>


		<br />
		<div id="new_add_link">
			<a href="${addTypeRenderURL}">
				<img title="<fmt:message key="news.img.title.add.newType"/>" 
					src="<html:imagesPath/>add.gif" border=0 />
				<fmt:message key="menu.newType" />
			</a>
		</div>
		<br />
	</c:when>
	<c:otherwise>
		<div>
		<p class="portlet-msg-info"><fmt:message key="news.view.noType" />
		</p>
		</div>
		<br />
		<div id="new_add_link"><a href="${addTypeRenderURL}"> <img
			title="<fmt:message key="news.img.title.add.newType"/>"
			src="<html:imagesPath/>add.gif" border=0 /> <fmt:message
			key="menu.newType" /> </a></div>
		<br/>
	</c:otherwise>
</c:choose></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>