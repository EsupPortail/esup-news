<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewAudience" />
<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="news_clear"></div>
<c:set var="ctxUrl" value="${addCAudienceRenderUrl}" />
<br />
<fieldset>
<legend> <fmt:message
	key="news.audience.page.title" /> <span class="portlet-font"> <c:out
	value='${category.name}' /> </span> </legend>
<div class="texteBasPage">[<a href="${ctxUrl}"
	class="liensmenudroit"><img src="<html:imagesPath/>audience.gif"
	alt="" align="absmiddle" border="0" /> <fmt:message
	key="news.label.new.audience" /></a>]</div>
<p />
<table class="cat">
	<thead>
		<tr>
			<th class="portlet-font"><fmt:message
				key="news.label.subscription.type" /></th>
			<th class="portlet-font"><fmt:message
				key="news.label.userAndGroup" /></th>
			<th align="right" class="portlet-font"><fmt:message
				key="news.label.action" /></th>
		</tr>
	</thead>
	<tr>
		<td colspan="3">
		<div class="line_c"></div>
		</td>
	</tr>
	<c:forEach items="${lists}" var="mapEntry" varStatus="j">
		<tr>
			<td class="portlet-form-label"><fmt:message
				key="${mapEntry.key}" /></td>
			<td colspan="2" class="portlet-font">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<c:choose>
					<c:when test="${fn:length(mapEntry.value) > 0}">
						<c:forEach items="${mapEntry.value}" var="listItem" varStatus="i">
							<tr>
								<td id="news_showhide" class="portlet-font"><c:choose>
									<c:when
										test="${listItem.isGroup==1 && not empty listItem.displayName}">
										<p onClick="slide('news_showhide${j.index}-${i.index}')"><img
											align="absmiddle"
											title='<fmt:message key="news.see.more.about.group"/>'
											src="<html:imagesPath/>personnes.gif" border=0 /> <c:out
											value="${listItem.displayName}" /></p>
										<span id="news_showhide${j.index}-${i.index}"
											class="news_showhide_hidden">
										<p><c:out value="${listItem.principal}" /></p>
										</span></c:when>
									<c:when test="${listItem.isGroup==1}">
										<img
											title="<fmt:message key="news.img.group"/>" align="absmiddle"
											src="<html:imagesPath/>personnes.gif" border=0 /> <fmt:message
											key="news.label.groupDetails.notFound">
											<fmt:param value="${listItem.principal}" />
										</fmt:message>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when
												test="${!empty userList[listItem.principal] && userList[listItem.principal].foundInLdap}">
												<p onClick="slide('news_showhide${j.index}-${i.index}')"><img
													title="<fmt:message key="news.see.more.about.individual"/>"
													align="absmiddle" src="<html:imagesPath/>personne.gif"
													border=0 /> <c:out
													value="${userList[listItem.principal].displayName}"/></p>
												<span id="news_showhide${j.index}-${i.index}" class="news_showhide_hidden">
												<p><c:forEach items="${attrDisplay}" var="displayAttr" varStatus="stat">
													<c:forEach
														items="${userList[listItem.principal].attributes[displayAttr]}"
														var="attrValue">
														<c:if test="${not stat.first && not empty attrValue}"> - </c:if>
														<c:out value="${attrValue}" />
													</c:forEach>
												</c:forEach></p>
												</span>
											</c:when>
											<c:otherwise>
												<img title="<fmt:message key="news.img.individual"/>"
													align="absmiddle" src="<html:imagesPath/>personne.gif"
													border=0 />
												<fmt:message key="news.label.userDetails.notFound">
													<fmt:param value="${listItem.principal}" />
												</fmt:message>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose></td>
								<td align="right"><a
									href="<portlet:actionURL>
                                   <portlet:param name="action" value="deleteSubscriber"/>
                                   <portlet:param name="subscriber" value="${listItem.id}"/>
                                   <portlet:param name="ctxId" value="${listItem.ctxId}"/>
                                   <portlet:param name="ctx" value="${listItem.ctxType}"/>
                                   </portlet:actionURL>"
									onClick="return confirm('<fmt:message key="news.alert.delete.subscriber"/>');"><img
									title="<fmt:message key="news.img.title.delSub"/>"
									src="<html:imagesPath/>delete.gif" border=0 /></a></td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="2">
							<div>
							<p class="portlet-msg-info"><fmt:message
								key="news.view.noTargetAudience" /></p>
							</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		<tr>
			<td colspan="3" class="pointilles"></td>
		</tr>
	</c:forEach>
	</td>
	</tr>
	<tr>
		<td colspan="3" class="line_c"></td>
	</tr>
</table>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>