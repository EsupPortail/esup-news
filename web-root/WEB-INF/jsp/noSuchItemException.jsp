<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
 <fieldset>
    <legend>
     <fmt:message key="exception.noSuchItemException.title"/>
    </legend>

<p class="portlet-msg-error"><c:out value="${message}"/></p>
<p class="portlet-msg-error"><fmt:message key="exception.contactAdmin"/></p>
</fieldset>
<div id="news_clear"></div>
 <%@ include file="/WEB-INF/jsp/footer.jsp" %>
