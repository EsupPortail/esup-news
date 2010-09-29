<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend>
     <fmt:message key="exception.timeLimitExceeded.title"/> 
    </legend>

<p class="portlet-msg-error">${exception.localizedMessage == null ? exception : exception.localizedMessage }<br/>
<spring:message code="exception.contactAdmin"/></p>

<p class="portlet-msg-error">ldap timeout error</p>
<p class="portlet-msg-error">${exception.class}</p>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>