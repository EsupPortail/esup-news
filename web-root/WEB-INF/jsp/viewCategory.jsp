<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewCat" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<fieldset>
<legend> <fmt:message
	key="news.label.topics.associeted.category" /> <span
	class="portlet-font">"<c:out value='${category.name}' />"</span> </legend>
<p />
<table class="cat">
	<c:choose>
		<c:when test="${fn:length(topicList) > 0}">
			<thead>
				<tr>
					<th class="portlet-font"><fmt:message
						key="news.label.topic.name" /></th>
					<th class="portlet-font"><fmt:message
						key="news.label.shortDesc" /></th>
					<th class="portlet-font"><fmt:message
						key="news.label.accessibility" /></th>
					<th colspan="6" class="portlet-font"><fmt:message
						key="news.label.action" /></th>
				</tr>
			</thead>
			<tr>
				<td colspan="9">
				<div class="line_c"></div>
				</td>
			</tr>

			<c:forEach var="topic" items="${topicList}" varStatus="status">
				<tr>
					<td class="news_list portlet-font"><c:choose>
						<c:when test="${topic.pendingCount gt 0}">
							<c:set var="r_s" value="0" />
						</c:when>
						<c:otherwise>
							<c:set var="r_s" value="1" />
						</c:otherwise>
					</c:choose> <a class="lien" href="<portlet:renderURL>
                                                <portlet:param name="action" value="viewTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="status" value="${r_s}"/>
                                        </portlet:renderURL>">${topic.name}</a>
					<c:if test='${pMask ge rM}'> [${topic.totalCount} |<span
							class="newsPending">${topic.pendingCount}</span>] </c:if></td>
					<td class="portlet-font"><c:out value="${topic.desc}" /></td>
					<td><span class="meta"><c:choose>
						<c:when test="${topic.publicView eq '1'}">
							<span class="portlet-font"> <fmt:message
								key="news.label.public" /></span>
						</c:when>
						<c:otherwise>
							<span class="portlet-font"><fmt:message
								key="news.label.private" /> </span>
						</c:otherwise>
					</c:choose> </span></td>
					<td><c:if test='${pMask ge rM}'>
						<c:if test="${!status.first}">
							<a
								href="<portlet:actionURL>
                                                <portlet:param name="action" value="incrementTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="increment" value="1"/>
                                        </portlet:actionURL>"><img
								title="<fmt:message key='news.img.title.increase.disp.order'/>"
								src="<html:imagesPath/>up.gif" border=0 /></a>
						</c:if></td>
					<td><c:if test="${!status.first}">
						<a
							href="<portlet:actionURL>
                                                <portlet:param name="action" value="incrementTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="increment" value="2"/>
                                        </portlet:actionURL>"><img
							title="<fmt:message key='news.img.title.increase.disp.order'/>"
							src="<html:imagesPath/>double_up.gif" border=0 /></a>
					</c:if></td>
					<td><c:if test="${!status.last}">
						<a
							href="<portlet:actionURL>
                                                <portlet:param name="action" value="incrementTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="increment" value="-1"/>
                                        </portlet:actionURL>"><img
							title="<fmt:message key='news.img.title.decrease.disp.order'/>"
							src="<html:imagesPath/>down.gif" border=0 /></a>
					</c:if></td>
					<td><c:if test="${!status.last}">
						<a
							href="<portlet:actionURL>
                                                <portlet:param name="action" value="incrementTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="increment" value="-2"/>
                                        </portlet:actionURL>"><img
							title="<fmt:message key='news.img.title.decrease.disp.order'/>"
							src="<html:imagesPath/>double_down.gif" border=0 /></a>
					</c:if></td>
					<td><a
						href="<portlet:renderURL>
                                                <portlet:param name="action" value="editTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                        </portlet:renderURL>"><img
						title="<fmt:message key='news.img.title.edit.topic'/>"
						src="<html:imagesPath/>edit.gif" border=0 /></a></td>
					<td><a
						href='<portlet:actionURL>
                                                <portlet:param name="action" value="deleteTopic"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                        </portlet:actionURL>'
						onClick="return confirm('<fmt:message key="news.alert.delete.topic"/>');"><img
						title="<fmt:message key='news.img.title.del.topic'/>"
						src="<html:imagesPath/>delete.gif" border=0 /></a> </c:if></td>
				</tr>
				<tr>
					<td colspan="9" class="pointilles"></td>
				</tr>
			</c:forEach>

		</c:when>
		<c:otherwise>
			<div>
			<p class="portlet-msg-info"><fmt:message key="news.view.noTopic" />
			</p>
			</div>
		</c:otherwise>
	</c:choose>
	<tr>
		<td colspan="9">
		<div class="line_c"></div>
		</td>
	</tr>
</table>
</fieldset>
<c:if test='${(pMask ge rM) && fn:length(topicList) > 0 }'><p/><div class="news_legende"> 
<p class="portlet-font"><fmt:message key="news.label.legende"/> :<br/>
[m  |<span class="newsPending">n</span>] : [<fmt:message key="news.label.totalCount.topic"/> | <span class="newsPending"> <fmt:message key="news.label.pendingCount.topic"/></span>]</div></c:if></p>
<%@ include file="/WEB-INF/jsp/footer.jsp" %> 