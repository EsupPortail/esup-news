<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>

<div id="news_headerI">
	<IMG height=1 alt="" src="<html:imagesPath/>spacer.gif" width=1>
	<IMG alt="<fmt:message key='news.label.entity'/>" src="<html:imagesPath/>ic_entity.gif" align="absMiddle"> 
	<span class="titreE"><a href="${viewEntityRenderUrl}"> <c:out value="${entity.name}" /></a></span>
	<IMG alt="<fmt:message key='news.label.category'/>" src="<html:imagesPath/>ic_cat.gif" align="absMiddle"> 
	<span class="titreC"><a href="${viewCatRenderUrl}"> <c:out	value="${category.name}" /></a></span> 
	<c:if test="${not empty topic}">
	<IMG alt="<fmt:message key='news.label.topic'/>" src="<html:imagesPath/>ic_topic.gif" align="absMiddle"> 
	<span class="titreT"><a href="${viewTopicRenderUrl}"> <c:out value="${topic.name}" /></a></span></c:if>
</div>
<div id="news_headerSM">
<ul>
	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif"
		width="50"></li>
	<li <c:if test='${aN == "view"}'>id="currentI" </c:if>><a class="portlet-menu-item" href='${viewItemRenderUrl}'>
			<fmt:message key="menu.Item.preview" /></a></li>
	<c:if test ='${(pMask ge rM) or ( pMask ge rC && userId eq item.postedBy) }'>
	<li <c:if test='${aN == "edit"}'>id="currentI" </c:if>><a href='${editItemRenderUrl}'
		class="portlet-menu-item"><fmt:message key="menu.Item.edit" /></A></li></c:if>
</ul>
</div>
