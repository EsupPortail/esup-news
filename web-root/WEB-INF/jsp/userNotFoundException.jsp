<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend>
     <fmt:message key="exception.userNotFoundException.title"/>
    </legend>
<p class="portlet-msg-error ">${message}<br/>
<spring:message code="exception.contactAdmin"/></p>
<p/>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
