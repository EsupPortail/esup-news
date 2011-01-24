<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="aN" value="viewEntityFilter" />
<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
<div id="news_clear"></div>
<br />
<fieldset>
<legend> <fmt:message
	key="news.filter.page.title" /> <span class="portlet-font"> <c:out
	value='${entity.name}' /> </span> </legend>
<div class="texteBasPage">[<a href="${addEntityFilterRenderUrl}"
	class="liensmenudroit"><fmt:message
	key="news.label.new.filter" /></a>]</div>
<p />
<table class="entity">
	<thead>
		<tr>
			<th class="portlet-font"><fmt:message
				key="news.label.filter.type" /></th>
			<th class="portlet-font"><fmt:message
				key="news.label.filter.criteria" /></th>
			<th align="right" class="portlet-font"><fmt:message
				key="news.label.action" /></th>
		</tr>
	</thead>
	<tr>
		<td colspan="3">
		<div class="line_e"></div>
		</td>
	</tr>
	<c:forEach items="${filterMap}" var="mapEntry">
		<tr>
			<td class="portlet-form-label">
			<c:choose>
				<c:when test="${mapEntry.key eq 'LDAP'}">
					<img align="absmiddle" src="<html:imagesPath/>personne.gif"
						border=0 />
				</c:when>
				<c:otherwise>
					<img align="absmiddle" src="<html:imagesPath/>personnes.gif"
						border=0 />
				</c:otherwise>
			</c:choose>				
				<fmt:message
				key="news.label.filter.type.${mapEntry.key}" /></td>
			<td colspan="2" class="portlet-font">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<c:choose>
					<c:when test="${fn:length(mapEntry.value) > 0}">
						<c:forEach items="${mapEntry.value}" var="listItem" varStatus="i">
							<tr>
								<td>
								<p><c:choose>
									<c:when test="${mapEntry.key eq 'LDAP'}">
										<c:out value="${listItem.attribute}" />
									</c:when>
									<c:when test="${mapEntry.key eq 'Group'}">
										<fmt:message key="news.label.filter.attribute.${listItem.attribute}" />
									</c:when>
								</c:choose>
								<c:out value="${listItem.operator.code}" />
								<c:out value="${listItem.value}" /></p>
								</td>
								<td align="right">
								<a href="<portlet:renderURL>
                                   <portlet:param name="action" value="editFilter"/>
                                   <portlet:param name="fId" value="${listItem.filterId}"/>
                                   <portlet:param name="eId" value="${entity.entityId}"/>
                                   </portlet:renderURL>"><img
									title="<fmt:message key="news.img.title.edit.filter"/>"
									src="<html:imagesPath/>edit.gif" border=0 /></a>	
								<a href="<portlet:actionURL>
                                   <portlet:param name="action" value="deleteFilter"/>
                                   <portlet:param name="fId" value="${listItem.filterId}"/>
                                   <portlet:param name="eId" value="${entity.entityId}"/>
                                   </portlet:actionURL>"
									onClick="return confirm('<fmt:message key="news.alert.delete.filter"/>');"><img
									title="<fmt:message key="news.img.title.del.filter"/>"
									src="<html:imagesPath/>delete.gif" border=0 /></a>
								
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="2">
							<div>
							<p class="portlet-msg-info"><fmt:message
								key="news.view.noFilter" /></p>
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
		<td colspan="3" class="line_e"></td>
	</tr>
</table>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>