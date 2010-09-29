<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewRss" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<fieldset>
    <legend> <fmt:message
	key="feed.export.title" /> <span class="portlet-font"><c:out
	value='${category.name}' /> </span> </legend>

<p class="portlet-font"><fmt:message key="feed.export.format.desc" /></p>

<p class="portlet-font"><fmt:message key="news.msg.rssfeed" /></p>

<%--<p class="portlet-font">&nbsp; &nbsp; &nbsp; <img
	src="<html:imagesPath/>r_cat.gif" border="0" /> <fmt:message
	key="feed.export.most.recent.1" /> : "${category.name}"</p> --%>
	
<p class="news_CatTitle portlet-font"><fmt:message key="feed.export.most.recent.1"/> :  "${category.name}" </p>
<%
    String days[] = { "1", "7", "30" };
    session.setAttribute("days", days);
%> <c:choose>
	<c:when test="${category.rssAllowed eq '1'}">
		<table class="cat">
			<c:choose>
				<c:when test="${category.publicView eq '1'}">
					<c:forEach var="nbLastDays" items="${days}">
						<tr>
							<td class="news_list portlet-font"> <fmt:message
								key="feed.export.most.recent.last.${nbLastDays}" /></td>
							<td><a
								href="<c:out value='${feed.portalURL}'/><c:out value='${feed.pubPath}'/>?t=2&cID=${category.categoryId}&feedType=${feed.feedType}&dayCount=${nbLastDays}"
								target="_blank"><img src="<html:imagesPath/>rss.gif"
								border="0" /> <c:out value='${feed.portalURL}' /><c:out
								value='${feed.pubPath}' />?t=2&cID=${category.categoryId}&feedType=${feed.feedType}&dayCount=${nbLastDays}</a></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach var="dCount" items="${days}">
						<tr>
							<td class="news_list portlet-font"> <fmt:message
								key="feed.export.most.recent.last.${nbLastDays}" /></td>
							<td><a
								href="<c:out value='${feed.portalURL}'/><c:out value='${feed.privPath}'/>?t=2&cID=${category.categoryId}&feedType=${feed.feedType}&dayCount=${nbLastDays}"
								target="_blank"><img
								title='<fmt:message key="news.img.title.rssFeed"/>'
								src="<html:imagesPath/>rss2.gif" border="0" /><c:out
								value='${feed.portalURL}' /><c:out value='${feed.privPath}' />?t=2&cID=${category.categoryId}&feedType=${feed.feedType}&dayCount=${nbLastDays}</a></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</c:when>

	<c:otherwise>
		<fmt:message key="news.label.rssNotAllowed" />
	</c:otherwise>
</c:choose>

<%-- <p class="portlet-font">&nbsp; &nbsp; &nbsp; <img
	src="<html:imagesPath/>r_cat.gif" border="0" /> <fmt:message
	key="feed.export.topic" /></p>--%>
<p class="news_CatTitle portlet-font">  <fmt:message key="feed.export.topic"/></p>
<table class="cat">
	<c:choose>
		<c:when test="${fn:length(topicList) gt 0}">
			<thead>
				<tr>
					<th class="portlet-font"><fmt:message
						key="news.label.topic.name" /></th>
					<th class="portlet-font"><fmt:message key="news.label.xmlURL" /></th>
					<th class="portlet-font"><fmt:message
						key="news.label.accessibility" /></th>
				</tr>
			</thead>
			<tr>
				<td colspan="3" class="line_c"></td>
			</tr>
			<c:forEach var="topic" items="${topicList}" varStatus="status">
				<tr>
					<td class="portlet-font"><b><img
						src="<html:imagesPath/>puce_1.gif" alt="" /> <a
						href="<portlet:renderURL>
                                                <portlet:param name="action" value="viewTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="status" value="1"/>
                                        </portlet:renderURL>">${topic.name}</a></td>
					<c:choose>
						<c:when test="${topic.rssAllowed eq '1'}">
							<c:choose>
								<c:when test="${topic.publicView eq '1'}">
									<td class="portlet-font"><a
										href="<c:out value='${feed.portalURL}'/><c:out value='${feed.pubPath}'/>?t=1&topicID=${topic.topicId}&feedType=${feed.feedType}"
										target="_blank"><img src="<html:imagesPath/>rss.gif"
										border="0" /> <c:out value='${feed.portalURL}' /><c:out
										value='${feed.pubPath}' />?t=1&topicID=${topic.topicId}&feedType=${feed.feedType}</a></td>
									<td class="meta"><fmt:message key="news.label.public" /></td>
								</c:when>
								<c:otherwise>
									<td class="portlet-font"><a
										href="<c:out value='${feed.portalURL}'/><c:out value='${feed.privPath}'/>?t=1&topicID=${topic.topicId}&feedType=${feed.feedType}"
										target="_blank"><img src="<html:imagesPath/>rss2.gif"
										border="0" /> <c:out value='${feed.portalURL}' /><c:out
										value='${feed.privPath}' />?t=1&topicID=${topic.topicId}&feedType=${feed.feedType}</a></td>
									<td class="meta"><fmt:message key="news.label.private" />
									</td>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<td colspan="2" class="portlet-font"><fmt:message
								key="news.label.rssNotAllowed" /></td>
						</c:otherwise>
					</c:choose>

				</tr>
				<tr>
					<td colspan="3" class="pointilles"></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
				<div class="line_c"></div>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<div>
			<p class="portlet-msg-info"><fmt:message key="news.view.noTopic" />
			</p>
			</div>
		</c:otherwise>
	</c:choose>
</table>
<p class="portlet-font"><fmt:message key="feed.export.url" /></p>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>