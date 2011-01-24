<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewCatSetting" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message
	key="news.label.category.setting" /> <span class="portlet-font"><c:out
	value='${category.name}' /></span> </legend>
<div class="texteBasPage">[<a href="${editCatRenderUrl}"
	class="liensmenudroit"><img src="<html:imagesPath/>edit.gif" alt=""
	align="absmiddle" border="0" /> <fmt:message key="menu.modify" /></a>]</div>
<table border="0" cellpadding="4">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.category.name" /></td>
		<td class="portlet-font"><c:out value="${category.name}" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /></td>
		<td class="portlet-font"><c:out value="${category.desc}"
			escapeXml="false" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.langue" /></td>
		<td class="portlet-font"><c:if test="${category.langue == 'fr'}">
			<fmt:message key="langue.french" />
		</c:if><c:if test="${category.langue == 'en'}">
			<fmt:message key="langue.english" />
		</c:if></td>
	</tr>
	<c:choose>	
		<c:when test="${fn:length(typeList) > 0}">
				<td class="portlet-form-label"><fmt:message
					key="news.label.category.associated.types" /></td>
				<td class="portlet-font">
					<c:forEach var="type" items="${typeList}" varStatus="status">
						<c:if test="${not status.first}">, </c:if>
						<c:out value="${type.name}" />
					</c:forEach>
				</td>
			</tr>		
		</c:when>
		<c:otherwise>
				<td class="portlet-form-label"><fmt:message
					key="news.label.category.associated.types" /></td>
				<td class="portlet-font"><fmt:message
					key="news.label.category.no.associated.types" /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.createdBy" /></td>
		<td class="portlet-font"><a class="lien"
			href="<portlet:renderURL>
     <portlet:param name="action" value="userDetails"/>
     <portlet:param name="userId" value="${category.createdBy}"/>
     <portlet:param name="cId" value="${category.categoryId}"/>     
</portlet:renderURL>"><c:choose>
			<c:when
				test="${!empty userList[category.createdBy] && userList[category.createdBy].foundInLdap}">
				<c:out value="${userList[category.createdBy].displayName}" />
			</c:when>
			<c:otherwise>
				<fmt:message key="news.label.userDetails.notFound">
					<fmt:param value="${category.createdBy}" />
				</fmt:message>
			</c:otherwise>
		</c:choose></a></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.creationDate" /></td>
		<td class="portlet-font"><fmt:formatDate
			value="${category.creationDate}" type="date"
			pattern="${datePattern} HH:mm:ss" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.rssAllowed" /></td>
		<td class="portlet-font"><c:choose>
			<c:when test="${category.rssAllowed == 1}">
				<fmt:message key="news.label.yes" />
			</c:when>
			<c:otherwise>
				<div>
				<p class="portlet-msg-info"><fmt:message key="news.label.no" /></p>
				</div>
			</c:otherwise>
		</c:choose></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.accessibility" /></td>
		<td class="portlet-font"><c:choose>
			<c:when test="${category.publicView == 1}">
				<fmt:message key="news.label.public" />
			</c:when>
			<c:otherwise>
				<p class="portlet-msg-info"><fmt:message
					key="news.label.private" />
				<P>
			</c:otherwise>
		</c:choose></td>
	</tr>
</table>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>