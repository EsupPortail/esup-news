<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewEntity" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
<div id="news_clear"></div>
<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>
<br />
<fieldset>
    <legend> <fmt:message
	key="news.label.categories" /> </legend>
<p />
<table class="entity">
	<c:choose>
		<c:when test="${fn:length(categoryList) gt 0}">
			<thead>
				<tr>
					<th class="portlet-font"><fmt:message
						key="news.label.category.name" /></th>
					<th class="portlet-font"><fmt:message
						key="news.label.shortDesc" /></th>
					<th class="portlet-font"><fmt:message
						key="news.label.accessibility" /></th>
					<th colspan="8" class="portlet-font"><fmt:message
						key="news.label.action" /></th>
				</tr>
			</thead>
			<tr>
				<td colspan="11">
				<div class="line_e"></div>
				</td>
			</tr>
		
		
			<c:forEach items="${categoryList}" var="category"
				varStatus="status">
				<c:choose>
					<c:when test="${category.publicView eq '0'}">
						<c:set var="p" value="private" />
					</c:when>
					<c:otherwise>
						<c:set var="p" value="pub" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td class="news_listE portlet-font"><a
						href="<portlet:renderURL>
                                                <portlet:param name="action" value="viewCategory"/>
                                                <portlet:param name="cId" value="${category.categoryId}"/>
                                        </portlet:renderURL>">${category.name}</a>
					[${category.totalCount} | <span class="newsPending">${category.pendingCount}</span>]</td>
					<td class="portlet-font"><c:out value="${category.desc}" /></td>
					<td><span class="meta"><c:choose>
						<c:when test="${category.publicView eq '1'}">
							<span class="portlet-font"> <fmt:message
								key="news.label.public" /></span>
						</c:when>
						<c:otherwise>
							<span class="portlet-font"><fmt:message
								key="news.label.private" /> </span>
						</c:otherwise>
					</c:choose> </span></td>
					<c:if test="${pMask ge rM}">
						<td><a
							href="<portlet:renderURL>
	                                                <portlet:param name="action" value="editCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                        </portlet:renderURL>"><img
							title='<fmt:message key="news.img.title.edit.category"/>'
							src="<html:imagesPath/>edit.gif" border=0 /></a></td>
						<td><c:if test="${!status.first}">
							<a
								href="<portlet:actionURL>
	                                                <portlet:param name="action" value="incrementCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                                <portlet:param name="increment" value="1"/>
	                                        </portlet:actionURL>"><img
								title="<fmt:message key='news.img.title.increase.disp.order'/>"
								src="<html:imagesPath/>up.gif" border=0 /></a>
						</c:if></td>
						<td><c:if test="${!status.first}">
							<a
								href="<portlet:actionURL>
	                                                <portlet:param name="action" value="incrementCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                                <portlet:param name="increment" value="2"/>
	                                        </portlet:actionURL>"><img
								title="<fmt:message key='news.img.title.increase.disp.order'/>"
								src="<html:imagesPath/>double_up.gif" border=0 /></a>
						</c:if></td>
						<td><c:if test="${!status.last}">
							<a
								href="<portlet:actionURL>
	                                                <portlet:param name="action" value="incrementCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                                <portlet:param name="increment" value="-1"/>
	                                        </portlet:actionURL>"><img
								title="<fmt:message key='news.img.title.decrease.disp.order'/>"
								src="<html:imagesPath/>down.gif" border=0 /></a>
						</c:if></td>
						<td><c:if test="${!status.last}">
							<a
								href="<portlet:actionURL>
	                                                <portlet:param name="action" value="incrementCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                                <portlet:param name="increment" value="-2"/>
	                                        </portlet:actionURL>"><img
								title="<fmt:message key='news.img.title.decrease.disp.order'/>"
								src="<html:imagesPath/>double_down.gif" border=0 /></a>
						</c:if></td>
						<td><a
							href="<portlet:actionURL>
	                                                <portlet:param name="action" value="deleteCategory"/>
	                                                <portlet:param name="cId" value="${category.categoryId}"/>
	                                        </portlet:actionURL>"
							onClick="return confirm('<fmt:message key="news.alert.delete.category"/>');"><img
							title='<fmt:message key="news.img.title.del.category"/>'
							src="<html:imagesPath/>delete.gif" border=0 /></a></td>
						<td><c:if test='${category.rssAllowed eq "1"}'>
							<a
								href="${portalURL}/feeds/${p}/rss?t=3&cID=${category.categoryId}"
								target="_blank"><img
								title='<fmt:message key="news.img.title.export.category"/>'
								src="<html:imagesPath/>xml.gif" border="0" /></a>
						</c:if></td>
						<td><c:if test='${category.rssAllowed eq "1"}'>
							<a
								href="${portalURL}/feeds/${p}/rss?t=5&cID=${category.categoryId}"
								target="_blank"><img
								title='<fmt:message key="news.img.title.export.opml"/>'
								src="<html:imagesPath/>opml.gif" border=0 /></a>
						</c:if></td>
					</c:if>
				</tr>

				<tr>
					<td colspan="11" class="pointilles"></td>
				</tr>

			</c:forEach>
			<tr>
				<td colspan="11"><span class="portlet-msg-alert">${status.errorMessage}</span>
			</tr>
		</c:when>
		<c:otherwise>
			<div>
			<p class="portlet-msg-info"><fmt:message
				key="news.view.noCategory" /></p>
			</div>
		</c:otherwise>
	</c:choose>
	<tr>
		<td colspan="11">
		<div class="line_e"></div>
		</td>
	</tr>
</table>
</fieldset>
<br />
<c:if test="${not empty categoryList}">
	<div class="news_legende">
	<p class="portlet-font"><fmt:message key="news.label.legende" /> :<br />
	[m  |<span class="newsPending">n</span>]  : [<fmt:message key="news.label.totalCount.cat"/>   | <span class="newsPending"> <fmt:message key="news.label.pendingCount.cat"/></span>]</p></div>
</c:if><br/>
                                                        
<%@ include file="/WEB-INF/jsp/footer.jsp" %>