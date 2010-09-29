<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="home"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerACC">
<span class="newsTitle"><fmt:message key="news.label.entities"/>  </span>
</div>
<div id="news_clear"></div>
<c:if test="${not empty msg}">
	<p class="portlet-msg-info">${msg}</p>
</c:if>
<br />
<fieldset>
    <legend> <fmt:message
	key="news.label.entities" /> </legend>
<p />
	<c:choose>
		<c:when test="${fn:length(entityList) > 0}">
			<table class="adm">
			<c:forEach items="${entityList}" var="entity"
				varStatus="status">
				<tr>
					<td class="news_listA"><a
						href="<portlet:renderURL>
								   <portlet:param name="action" value="viewEntity" />
								   <portlet:param name="eId" value="${entity.entityId}" />
							  </portlet:renderURL>">${entity.name}</a></td>
					<c:if test='${pMask eq rS}'>
					<td><a
						href="<portlet:renderURL>
									<portlet:param name="action" value="editEntity" />
									<portlet:param name="eId" value="${entity.entityId}" />
							  </portlet:renderURL>"><img
						title="<fmt:message key="news.img.title.edit.entity"/>"
						src="<html:imagesPath/>edit.gif" border=0 /></a></td>
					<td><a href="<portlet:actionURL>
									<portlet:param name="action" value="deleteEntity" />
									<portlet:param name="eId" value="${entity.entityId}" />
							  </portlet:actionURL>"
						onClick="return confirm('<fmt:message key="news.alert.delete.entity"/>');"><img
						title="<fmt:message key="news.img.title.del.entity"/>"
						src="<html:imagesPath/>delete.gif" border=0 /></a></td>
					
					</c:if>
				</tr>

				<tr>
					<td colspan="3" class="pointilles"></td>
				</tr>

			</c:forEach>
			<tr>
				<td colspan="3"><span class="portlet-msg-alert">${status.errorMessage}</span>
			</tr>
			</table>
			<br/>
			<c:if test='${pMask eq rS}'>
			<div id="new_add_link"><a href="${addEntityRenderUrl}"> <img
					title="<fmt:message key="news.img.title.add.newEntity"/>"
					src="<html:imagesPath/>add.gif" border=0 /> <fmt:message
					key="menu.newEntity" /> </a></div>
				<br/>
			</c:if>
		</c:when>
		<c:otherwise>
				<div>
				<p class="portlet-msg-info"><fmt:message
					key="news.view.noEntity" /></p>
				</div>
				<br/>
				<c:if test='${pMask eq rS}'>
				<div id="new_add_link"><a href="${addEntityRenderUrl}"> <img
					title="<fmt:message key="news.img.title.add.newEntity"/>"
					src="<html:imagesPath/>add.gif" border=0 /> <fmt:message
					key="menu.newEntity" /> </a></div>
				<br/>
				</c:if>
		</c:otherwise>
	</c:choose>
</fieldset>
<br />
<br/>
                                                        
<%@ include file="/WEB-INF/jsp/footer.jsp" %>