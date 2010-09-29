<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="admin"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"><IMG align="absmiddle" alt="" src="<html:imagesPath/>icone_admin.gif" border="0"> 
<SPAN class="newsTitle"><fmt:message key="menu.admin"/> </SPAN>
</div>
<c:set var="currentHeaderSM" value="role" />
<%@ include file="/WEB-INF/jsp/headerSM_adminView.jsp"%>
<div id="news_clear"></div>
  <fieldset>
    <legend> <fmt:message
	key="view.roles.page.title" /> </legend> <br />

<p class="portlet-section-text-bold"><img src="<html:imagesPath/>puce_adm.gif"
	alt="" /> <fmt:message key="news.label.roles.view.header" /></p>
<c:choose>
	<c:when test="${fn:length(roleList) > 0}">
		<DL id="news_showhide">
			<c:forEach var="role" items="${roleList}" varStatus="status">
				<c:if test='${role.roleName != "ROLE_USER"}'>
					<DT onClick="slide('news_showhide_roles${status.index}')"><img
						src="<html:imagesPath/>fa.gif" alt="" /> <b><fmt:message
						key="${role.roleName}" /> <img src="<html:imagesPath/>help_a.gif"
						align="absmiddle" alt='<fmt:message key="news.label.perms.role"/>' />:
					</b><fmt:message key="${role.descKey}" /></DT>
					<p />
					<DD id="news_showhide_roles${status.index}"><fmt:message
						key="news.role.${role.roleName}.perm" /></DD>
				</c:if>
			</c:forEach>
		</DL>
	</c:when>
	<c:otherwise>
		<div>
		<p class="portlet-msg-info"><fmt:message key="news.view.noRole" />
		</p>
		</div>

	</c:otherwise>
</c:choose></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>
