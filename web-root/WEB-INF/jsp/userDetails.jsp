<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"><IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"/> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="manager" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<c:set var="regdate" scope="session">
	<fmt:formatDate value="${user.registerDate}" />
</c:set>
<c:set var="lastdate" scope="session">
	<fmt:formatDate value="${user.lastAccess}" />
</c:set>

<div id="news_clear"></div>
<fieldset>
<legend> <fmt:message
	key="news.label.userDetails.view.header" /> </legend> <br />

<c:choose>
	<c:when test="${user.foundInLdap}">
		<p class="portlet-section-text-bold">
			<img src="<html:imagesPath/>puce_adm.gif" alt="" /> ${user.displayName}	(${user.userId})</p>
		<span class="portlet-form-label"><fmt:message key="news.label.email" /> : </span>
		<a href="mailto:${user.email}">${user.email}</a>
		<br />

		<c:forEach items="${attrDisplay}" var="displayAttr">
				<span class="portlet-form-label"><fmt:message
					key="news.label.${displayAttr}" /> : </span>
				<c:forEach items="${user.attributes[displayAttr]}" var="attrValue">
					<c:out value="${attrValue}" />
				</c:forEach>
				<br />
		</c:forEach>
	</c:when>
	<c:otherwise>
		<p class="portlet-section-text-bold">
			<img src="<html:imagesPath/>puce_adm.gif" alt="" />
		<fmt:message key="news.label.userDetails.notFound">
			<fmt:param>
				<c:out value="${user.userId}" />
			</fmt:param>
		</fmt:message>
		</p>
		<br/>
	</c:otherwise>
</c:choose>
	
<span class="portlet-form-label"><fmt:message
	key="news.label.registerDate" /> : </span><fmt:formatDate
	value="${user.registerDate}" type="both" dateStyle="full"
	timeStyle="short" pattern="${datePattern}" /><span /> <br />
<span class="portlet-form-label"> <fmt:message
	key="news.label.lastAccess" /> : </span> <c:choose>
	<c:when test="${user.lastAccess gt user.registerDate}">
		<fmt:formatDate value="${user.lastAccess}" type="both"
			timeStyle="long" dateStyle="long" pattern="${datePattern}" />
	</c:when>
	<c:otherwise>
		<fmt:message key="news.msg.no_lastAccess" />
	</c:otherwise>
</c:choose><span /> <br />
<span class="portlet-form-label"><fmt:message
	key="news.label.activateAccount" /> </span> <c:choose>
	<c:when test="${user.enabled eq 1}">
		<fmt:message key='news.label.yes' />
	</c:when>
	<c:otherwise>
		<fmt:message key='news.label.no' />
	</c:otherwise>
</c:choose><br />
<p class="portlet-font"><img src="<html:imagesPath/>fa.gif" alt="" /><fmt:message
	key="news.label.assignedRoles" /> : <c:choose>
	<c:when test="${uMask eq rS}">
		<fmt:message key="ROLE_ADMIN" />
	</c:when>
	<c:otherwise>
		<p />
		<table class="cat">
			<thead>
				<tr>
					<th class="portlet-font"><fmt:message key="news.label.entity" />
					(<img src="<html:imagesPath/>icone_entity.gif" align="absMiddle" />)
					/ <fmt:message key="news.label.category" />
					(<img src="<html:imagesPath/>icone_cat_p2.gif" align="absMiddle" />)
					/ <fmt:message key="news.label.topic" /> (<img
						src="<html:imagesPath/>icone_theme_p2.gif" align="absMiddle" />)</th>
					<th class="portlet-font"><fmt:message key="news.label.role" /></th>
				</tr>
			</thead>
			<tr>
				<td colspan="2">
				<div class="line_a"></div>
				</td>
			</tr>
			<c:forEach var="erm" items="${eRoleMap}">
				<tr>
					<td class="portlet-font"><p class="news_E"> <c:out value="${erm.key}" /> </p></td>
					<td class="portlet-font"><fmt:message key="${erm.value}" /><br /></td>
				</tr>
				<c:forEach var="crm" items="${cRoleMap[erm.key]}">
					<tr>
						<td class="portlet-font"><p class="news_C"> <c:out value="${crm.key}" /> </p></td>
						<td class="portlet-font"><fmt:message key="${crm.value}" /><br /></td>
					</tr>
					<c:forEach var="trm" items="${tRoleMap[crm.key]}">
						<tr>
							<td class="portlet-font"><p class="news_T"> <c:out value="${trm.key}" /> </p></td>
							<td class="portlet-font"><fmt:message key="${trm.value}" /><br /></td>
						</tr>
	
					</c:forEach>
				</c:forEach>
				<tr>
					<td colspan="2" class="pointilles"></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2">
				<div class="line_a"></div>
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></p></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>

