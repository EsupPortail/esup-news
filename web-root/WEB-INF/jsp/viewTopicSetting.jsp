<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewTopicSetting" />
<%@ include file="/WEB-INF/jsp/submenu_t.jsp"%>
<%@ include file="/WEB-INF/jsp/header2T.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message
	key="news.label.topic.setting" /> </legend>
<TABLE width="98%" cellSpacing="2" cellPadding="0" border="0">
	<TBODY>
		<TR>
			<TD>
			<div class="texteBasPage">[<a href="${editTopicRenderUrl}"
				class="liensmenudroit"><img src="<html:imagesPath/>edit.gif"
				alt="" align="absmiddle" border="0" /> <fmt:message
				key="menu.modify" /></a>]</div>
			<table border="0" cellpadding="4">
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.topic.name" /></td>
					<td class="portlet-font"><c:out value="${topic.name}" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.shortDesc" /></td>
					<td class="portlet-font"><c:out value="${topic.desc}"
						escapeXml="false" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.createdBy" /></td>
					<td class="portlet-font"><a class="lien"
						href="<portlet:renderURL>
     <portlet:param name="action" value="userDetails"/>
     <portlet:param name="uid" value="${topic.createdBy}"/>
     <portlet:param name="tId" value="${topic.topicId}"/>
     <portlet:param name="cId" value="${topic.categoryId}"/>     
</portlet:renderURL>"><c:choose>
				<c:when
					test="${!empty userList[topic.createdBy] && userList[topic.createdBy].foundInLdap}">
					<c:out value="${userList[topic.createdBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param value="${topic.createdBy}" />
					</fmt:message>
				</c:otherwise>
			</c:choose></a></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.creationDate" /></td>
					<td class="portlet-font"><fmt:formatDate
						value="${topic.creationDate}" type="date"
						pattern="${datePattern} HH:mm:ss" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.rssAllowed" /></td>
					<td class="portlet-font"><c:choose>
						<c:when test="${topic.rssAllowed == 1}">
							<fmt:message key="news.label.yes" />
						</c:when>
						<c:otherwise>
							<fmt:message key="news.label.no" />
						</c:otherwise>
					</c:choose></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.accessibility" /></td>
					<td class="portlet-font"><c:choose>
						<c:when test="${topic.publicView == 1}">
							<fmt:message key="news.label.public" />
						</c:when>
						<c:otherwise>
							<fmt:message key="news.label.private" />
						</c:otherwise>
					</c:choose></td>
				</tr>
			</table>
			</TD>
		</TR>
</TABLE>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>