<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<portlet:renderURL var="editTypeURL">
	<portlet:param name="action" value="editType" />
	<portlet:param name="id" value="${typeForm.type.typeId}" />
</portlet:renderURL>
<div id="news_headerA"> <IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"/> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="type" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>

<br/>
<fieldset>
    <legend> <fmt:message key="news.label.type.setting" /> <span class="portlet-font">
    <c:out value="${typeForm.type.name}" /></span> </legend>
	<div class="texteBasPage">[<a href="${editTypeURL}"
		class="liensmenudroit"><img src="<html:imagesPath/>edit.gif" alt=""
		align="absmiddle" border="0" /> <fmt:message key="menu.modify" /></a>]
	</div>
	<table border="0" cellpadding="4">
		<tr>
			<td class="portlet-font-dim"><fmt:message
				key="news.label.type.name" /> :</td>
			<td class="portlet-font"><c:out value="${typeForm.type.name}" /></td>
		</tr>
		<tr>
			<td class="portlet-font-dim"><fmt:message
				key="news.label.shortDesc" /> :</td>
			<td class="portlet-font"><c:out value="${typeForm.type.description}"
				escapeXml="false" /></td>
		</tr>
		<tr>
	<c:choose>	
	<c:when test="${fn:length(entityList) > 0}">
			<td class="portlet-font-dim" colspan="2"><fmt:message
				key="news.label.type.associations" /></td>
		</tr>
		</table>
		<table class="cat">
			<thead>
				<tr>
					<th class="portlet-font" colspan="2"><img src="<html:imagesPath/>icone_entity.gif" align="absMiddle" /> <fmt:message key="news.label.entity" /> / 
					<img src="<html:imagesPath/>icone_cat_p2.gif" align="absMiddle" /> <fmt:message key="news.label.category" /></th>					
				</tr>
			</thead>
			<tr>
				<td colspan="2">
				<div class="line_a"></div>
				</td>
			</tr>
			<c:forEach var="entity" items="${entityList}">
				<c:forEach var="catList" items="${catListOfEntity}">
					<c:if test="${catList.key eq entity.entityId}">
						<c:choose>
							<c:when test="${fn:length(catList.value) > 0}" >				
								<tr>
									<td class="portlet-font">
										<p class="news_E" style="margin-left: 5%;"> <c:out value="${entity.name}" /></p>
									</td>
									<c:forEach var="cat" items="${catList.value}">
										<tr><td class="portlet-font">
										<p class="news_C" style="margin-left: 10%;"> <c:out value="${cat.name}" /></p>
										</td></tr>
									</c:forEach>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td class="portlet-font">
										<p class="news_E" style="margin-left: 5%;"> <c:out value="${entity.name}" /></p>
									</td>
									<td class="portlet-font" />
								</tr>
							</c:otherwise>
						</c:choose>				 
						<tr>
							<td colspan="2" class="pointilles" />
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>
			<tr>
				<td colspan="2">
					<div class="line_a" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
			<td class="portlet-font-dim"><fmt:message
				key="news.label.type.associated.entities" /></td>
			<td class="portlet-font"><fmt:message
				key="news.label.type.no.associated.entities" /></td>
		</tr>
		</table>
	</c:otherwise>
	</c:choose>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>