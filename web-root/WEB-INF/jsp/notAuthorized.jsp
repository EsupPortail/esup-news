<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend>
     <fmt:message key="exception.notAuthorized.title"/>
    </legend>
    
<p class="portlet-font"><fmt:message key="exception.notAuthorized.message"/><br>
<p class="portlet-msg-error"><c:out value="${msgError}"/></p>
<p class="portlet-msg-error"><fmt:message key="exception.contactAdmin"/></p>

<p/>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>