<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewPerm" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<fieldset>
<legend> <fmt:message
	key="news.permission.page.title" /> <span class="portlet-font">
<c:out value='${category.name}' /> </span> </legend>
<div class="texteBasPage">[<a href="${addCPermissionRenderUrl}"
	class="liensmenudroit"><img src="<html:imagesPath/>perm.gif" alt=""
	align="absmiddle" border="0" /> <fmt:message
	key="add.permission.page.title" /> </a>]</div>

<c:if test="${fn:length(msg) > 0}"><div class="portlet-msg-error"><c:out value="${msg}" /></div></c:if>

<DL id="news_showhide">
	<DT onClick="slide('news_showhide_superUser')"><img src="<html:imagesPath/>fc.gif" alt="" /> <fmt:message
		key="news.label.superUsers" /> <img
		src="<html:imagesPath/>help_c.gif" align="absmiddle" alt="" /></DT>
	<DD id="news_showhide_superUser"><c:choose>
		<c:when test="${fn:length(sUsers) > 0}">
			<c:forEach var="user" items="${sUsers}" varStatus="status">
				<img src="<html:imagesPath/>puce.gif" alt="" />
				<c:choose>
					<c:when test="${user.foundInLdap}">
						<c:forEach items="${attrDisplay}" var="displayAttr" varStatus="stat">
							<c:forEach items="${user.attributes[displayAttr]}"
								var="attrValue">
								<c:if test="${not stat.first && not empty attrValue}"> - </c:if>
								<c:out value="${attrValue}" />
							</c:forEach>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<fmt:message key="news.label.userDetails.notFound">
							<fmt:param>
								<c:out value="${user.principal}" />
							</fmt:param>
						</fmt:message>
					</c:otherwise>
				</c:choose>
				<br />
			</c:forEach>
		</c:when>
		<c:otherwise>
			<div><span class="portlet-msg-info"> <fmt:message
				key="news.view.noSuperUser" /> </span></div>
		</c:otherwise>
	</c:choose></DD>
</DL>
<span class="portlet-font" Style="PADDING-LEFT: 20px;"><img
	src="<html:imagesPath/>fc.gif" alt="" /> <fmt:message
	key="news.label.otherRoles" /> <a href="${viewRolesRenderUrl}" ><img
		src="<html:imagesPath/>help_c.gif" align="absmiddle" title='<fmt:message key="news.label.roles.goToDetails" />' /></a></span>
<table class="cat">
	<thead>
		<tr>
			<th class="portlet-font"><fmt:message
				key="news.label.role" /></th>
			<th class="portlet-font"><fmt:message
				key="news.label.user" /></th>
			<th align="right" class="portlet-font"><fmt:message
				key="news.label.action" /></th>
		</tr>
	</thead>
	<tr>
		<td colspan="4">
		<div class="line_c"></div>
		</td>
	</tr>
	<c:forEach items="${lists}" var="mapEntry" varStatus="role">
		<c:choose>
			<c:when test="${fn:length(mapEntry.value) > 0}">
				<tr>
					<td rowspan="${fn:length(mapEntry.value)}"
						class="portlet-form-label"><fmt:message key="${mapEntry.key}" /></td>
					<c:forEach items="${mapEntry.value}" var="listItem"
						varStatus="statusAttrValues">
						<c:if test="${statusAttrValues.count > 1}">
							<tr>
						</c:if>
						<td id="news_showhide" class="portlet-font">
						<p
							onClick="slide('news_showhide${role.index}-${statusAttrValues.index}')"><c:choose>
							<c:when test="${listItem.isGroup==1 && not empty listItem.displayName}">
								<p
									onClick="slide('news_showhide${role.index}-${statusAttrValues.index}')"><img
									title="<fmt:message key="news.img.group"/>"
									src="<html:imagesPath/>personnes.gif" border=0 /><c:out
									value="${listItem.displayName}" /></p>
								<span id="news_showhide${role.index}-${statusAttrValues.index}" class="news_showhide_hidden">
								<p><c:out value="${listItem.principal}" /></p>
								</span>
							</c:when>
							<c:when test="${listItem.isGroup==1}">
									<img title="<fmt:message key="news.img.group"/>"
										align="absmiddle" src="<html:imagesPath/>personnes.gif"
										border=0 />
									<fmt:message key="news.label.groupDetails.notFound">
										<fmt:param value="${listItem.principal}" />
									</fmt:message>
								</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${not empty listItem.fromGroup}"><img title="<fmt:message key="news.img.individual"/>"
									src="<html:imagesPath/>persfromgrp.gif" border=0 /></c:when>
									<c:otherwise><img title="<fmt:message key="news.img.individual"/>"
									src="<html:imagesPath/>personne.gif" border=0 /></c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${!empty userList[listItem.principal] && userList[listItem.principal].foundInLdap}">
										<c:out value="${userList[listItem.principal].displayName}" /></p>
										<span id="news_showhide${role.index}-${statusAttrValues.index}" class="news_showhide_hidden">
											<p><c:forEach items="${attrDisplay}" var="displayAttr" varStatus="stat">
													<c:forEach items="${userList[listItem.principal].attributes[displayAttr]}" var="attrValue">
														<c:if test="${not stat.first && not empty attrValue}"> - </c:if>
														<c:out value="${attrValue}" />
													</c:forEach>
												</c:forEach>
												<c:if test="${fn:length(listItem.fromGroup) > 0}"> - <c:out value="${listItem.fromGroup}" /></c:if>
											</p>
										</span>
									</c:when>
									<c:otherwise>
										<fmt:message key="news.label.userDetails.notFound">
											<fmt:param value="${listItem.principal}" />
										</fmt:message>
									</p>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						</td>
						<td align="right">
							<c:if test="${listItem.isGroup==0}">
								<a href="<portlet:renderURL>
												<portlet:param name="action" value="userDetails" />
												<portlet:param name="userId" value="${listItem.principal}"/>
												<portlet:param name="isGrp" value="${listItem.isGroup}"/>
										</portlet:renderURL>">
									<img title="<fmt:message key="news.img.title.view.userDetail"/>"
									src="<html:imagesPath/>search.gif" border=0 /></a>&nbsp;&nbsp;
							</c:if>
							<c:if test="${empty listItem.fromGroup}">
								<a href="<portlet:actionURL>
									<portlet:param name="action" value="deleteUserRole"/>
									<portlet:param name="userId" value="${listItem.principal}"/>
									<portlet:param name="isGrp" value="${listItem.isGroup}"/>
									<portlet:param name="ctxId" value="${listItem.ctxId}"/>
									<portlet:param name="ctx" value="${listItem.ctxType}"/>
									</portlet:actionURL>"
									onClick="return confirm('<fmt:message key="news.alert.delete.role"/>');"><img
									title="<fmt:message key="news.img.title.del.userRole"/>"
									src="<html:imagesPath/>delete.gif" border=0 /></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td rowspan="1" class="portlet-form-label"><fmt:message
						key="${mapEntry.key}" /></td>
					<td colspan="3">
					<div>
					<p class="portlet-msg-info"><fmt:message
						key="news.view.noUserInRole" /></p>
					</div>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>

	<tr>
		<td colspan="4" class="pointilles">&nbsp;</td>
	</tr>
	</c:forEach>
	<tr>
		<td colspan="4" class="line_c">&nbsp;</td>
	</tr>
</table>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>