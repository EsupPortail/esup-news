<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewEntitySetting" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
<div id="news_clear"></div>
<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>
<br />
<fieldset>
    <legend> <fmt:message
	key="news.label.entity.setting" /> <span class="portlet-font"><c:out
	value='${entity.name}' /></span> </legend>
<c:if test='${pMask ge rS}'><div class="texteBasPage">[<a href="${editEntityRenderUrl}"
	class="liensmenudroit"><img src="<html:imagesPath/>edit.gif" alt=""
	align="absmiddle" border="0" /> <fmt:message key="menu.modify" /></a>]</div></c:if>
<table border="0" cellpadding="4">
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.entity.name" /></td>
		<td class="portlet-font"><c:out value="${entity.name}" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.shortDesc" /></td>
		<td class="portlet-font"><c:out value="${entity.description}"
			escapeXml="false" /></td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.createdBy" /></td>
		<c:choose>
			<c:when
				test="${!empty userList[entity.createdBy] && userList[entity.createdBy].foundInLdap}">
				<td class="portlet-font"><a class="lien"
					href="<portlet:renderURL>
							     <portlet:param name="action" value="userDetails"/>
							     <portlet:param name="uid" value="${entity.createdBy}"/>
							     <portlet:param name="eId" value="${entity.entityId}"/>     
							</portlet:renderURL>">
				<c:out value="${userList[entity.createdBy].displayName}" /> </a>
			</c:when>
			<c:otherwise>
				<td class="portlet-font"><fmt:message
					key="news.label.userDetails.notFound">
					<fmt:param value="${entity.createdBy}" />
				</fmt:message>
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	<tr>
		<td class="portlet-form-label"><fmt:message
			key="news.label.creationDate" /></td>
		<td class="portlet-font"><fmt:formatDate
			value="${entity.creationDate}" type="date"
			pattern="${datePattern} HH:mm:ss" /></td>
	</tr>
	<tr>
	<c:choose>	
		<c:when test="${fn:length(typeList) > 0}">
				<td class="portlet-form-label"><fmt:message
					key="news.label.entity.associated.types" /></td>
				<td class="portlet-font">
					<c:forEach var="type" items="${typeList}" varStatus="status">
						<c:if test="${not status.first}">, </c:if>
						<c:out value="${type.name}" /> <a
								href="${portalURL}/feeds/pub/rss?t=4&entityID=${entity.entityId}&type=${type.name}"
								target="_blank"><img
								title="<fmt:message key='news.img.title.export.entity'/>"
								src="<html:imagesPath/>xml.gif" border="0" /></a>
					</c:forEach>
				</td>
			</tr>		
		</c:when>
		<c:otherwise>
				<td class="portlet-font-dim"><fmt:message
					key="news.label.entity.associated.types" /></td>
				<td class="portlet-font"><fmt:message
					key="news.label.entity.no.associated.types" /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	
	
</table>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>