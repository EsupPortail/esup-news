<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="currentMainMenu" value="help"/>
<%@ include file="/WEB-INF/jsp/header1_adminView.jsp"%>
<div id="news_headerA"><IMG align="absmiddle" alt="" src="<html:imagesPath/>icon_help.gif" border="0"> 
<SPAN class="newsTitle"><fmt:message key="menu.help"/>  </SPAN>
</div>

<div id="news_clear"></div>
 <fieldset>
    <legend>
     <fmt:message key="menu.help"/> 
    </legend> 
<p><fmt:message key="news.help.onlineDoc" /> : <a
	href="http://sourcesup.cru.fr/newsportlet/" target="top">http://sourcesup.cru.fr/newsportlet/</a></p>
<p>&nbsp;</p>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>