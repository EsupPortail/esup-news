<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend> <fmt:message
	key="exception.dataAccessFailure.title" /> </legend>
<p class="portlet-font"><fmt:message
	key="exception.dataAccessFailure.message" /></p>
<p class="portlet-msg-error">${exception.class}</p>
<p class="portlet-msg-error">${exception.localizedMessage == null ?
exception : exception.localizedMessage }</p>
<p class="portlet-msg-error"><fmt:message
	key="exception.contactAdmin" /></p>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>
