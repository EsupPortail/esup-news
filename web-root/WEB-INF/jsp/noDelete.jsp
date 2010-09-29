<%@ include file="/WEB-INF/jsp/include.jsp"%>

<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>

<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
<div id="header">
<ul>
	<li><IMG height=1 alt="" src="<html:imagesPath/>spacer.gif"
		width="100"></li>
	<li id="current"><a class="sousmenu" href="${viewCatRenderUrl}"><fmt:message
		key="menu.topics" /></a></li>
	<li><a href="${newItemRenderUrl}" class="sousmenu" /> <fmt:message
		key="menu.newItem" /> |</a></li>
	<c:if test='${pMask ge rM}'>
		<li><a href="${newTopicRenderUrl}" class="sousmenu"><fmt:message
			key="menu.newTopic" /> |</A></li>

		<li><a class="sousmenu" href="${viewCatSettingRenderUrl}"><fmt:message
			key="menu.setting" /> |</a></li>
		<li><a class="sousmenu" href="${viewTopicsRenderUrl}"><fmt:message
			key="menu.topicsView" /> |</A></li>
		<li><A class=sousmenu href="${catPermissionRenderUrl}"><fmt:message
			key="menu.permission" /> |</A></li>
		<li><a href="${catAudienceRenderUrl}" class="sousmenu"><fmt:message
			key="menu.audience" /> |</A></li>
	</c:if>
	<li><a class="sousmenu" href="${feedRSSUrl}"><fmt:message
		key="menu.rss" /> </a></li>

</ul>
</div>
<div id="news_clear"></div>
<fieldset>
<legend> 
Suppression non autorisée 
</legend> <br />

<TABLE width="98%" cellSpacing="2" cellPadding="0" border="0">
	<TBODY>
		<TR>

			<TD>
			<p class="portlet-msg-alert"><c:out value="${message}" /></p>
			</td>
		</tr>
</table>
<br />
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>