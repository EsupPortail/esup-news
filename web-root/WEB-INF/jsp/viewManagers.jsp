<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<portlet:actionURL var="delUserURL">
	<portlet:param name="action" value="deleteUser" />
</portlet:actionURL>
<portlet:actionURL var="activateUserURL">
	<portlet:param name="action" value="activateUser" />
</portlet:actionURL>
<portlet:renderURL var="userDetailsURL">
	<portlet:param name="action" value="userDetails" />
</portlet:renderURL>

<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"> <IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="manager" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>

<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>

<br />
<fieldset>
<legend> <fmt:message
	key="menu.viewManagers" /> </legend> <br />
<c:set var="tit">
	<fmt:message key="news.img.person" />
</c:set> <c:choose>
	<c:when test="${fn:length(userList) > 0}">
		<d:table name="${userList}" export="false" id="row" class="dataTable" sort="list"
			pagesize="${nbItemsToShow}" cellspacing="1" defaultorder="ascending" defaultsort="2">
			<d:column property="userId" title="ID" class="hidden"
				headerClass="hidden" media="html" />
			<d:column href="${userDetailsURL}" paramId="uid"
				paramProperty="userId"
				titleKey="news.label.displayName" sortable="true">
				<c:choose>
					<c:when test="${row.foundInLdap}">
						<c:out value="${row.displayName}" />
					</c:when>
					<c:otherwise>
						<fmt:message key="news.label.userDetails.notFound">
							<fmt:param>
								<c:out value="${row.userId}" />
							</fmt:param>
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</d:column>
			<d:column property="email" titleKey="news.label.email"
				sortable="true" />
			<d:column property="registerDate" titleKey="news.label.registerDate"
				sortable="true" format="{0,date,long}" />
			<d:column property="lastAccess" titleKey="news.label.lastAccess"
				sortable="true" format="{0,date,long}" />
			<d:column titleKey="news.label.superUser" sortable="true">
				<c:choose>
					<c:when test="${row.isSuperAdmin == 1}">
						<img title="super user" src="<html:imagesPath/>OK.gif" border="0" />
					</c:when>
					<c:otherwise>
						<img title="Not Super user" src="<html:imagesPath/>admN.gif"
							border=0 />
					</c:otherwise>
				</c:choose>
			</d:column>
			<d:column href="${activateUserURL}" paramId="uid"
				paramProperty="userId" titleKey="news.label.activateAccount"
				sortable="true">
				<c:choose>
					<c:when test="${row.enabled == 1}">
						<img title="<fmt:message key='news.img.title.disable.user'/> ?"
							src="<html:imagesPath/>enabled.gif" border=0 />
					</c:when>
					<c:otherwise>
						<img title="<fmt:message key='news.img.title.enable.user'/> ?"
							src="<html:imagesPath/>disabled.gif" border=0 />
					</c:otherwise>
				</c:choose>
			</d:column>
			<d:column href="${userDetailsURL}" paramId="uid" paramProperty="userId">
				<img title="<fmt:message key="news.img.title.view.userDetail"/>"
					src="<html:imagesPath/>search.gif" border=0 />
			</d:column>
			<d:column href="${delUserURL}" paramId="uid" paramProperty="userId">
				<img title="<fmt:message key="news.alert.delete.user"/>"
					src="<html:imagesPath/>delete.gif" border=0 
					onClick="return confirm('<fmt:message key="news.alert.delete.user"/>');"/>
			</d:column>

			<d:setProperty name="paging.banner.placement" value="bottom" />
			<d:setProperty name="paging.banner.item_name" value="${tit}" />
			<d:setProperty name="paging.banner.items_name" value="${tit}s" />
		</d:table>


		<br />
		<div id="new_add_link">
			<a href="${addSuperAdmRenderUrl}">
				<img title="<fmt:message key="news.img.title.add.superAdm"/>" 
					src="<html:imagesPath/>add.gif" border=0 />
				<fmt:message key="menu.addSuperAdm" />
			</a>
		</div>
		<br />
	</c:when>
	<c:otherwise>
		<div>
		<p class="portlet-msg-info"><fmt:message key="news.view.noUser" />
		</p>
		</div>
	</c:otherwise>
</c:choose></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>
