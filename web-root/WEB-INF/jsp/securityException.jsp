<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend>
     <fmt:message key="exception.notAuthorized.title"/> 
    </legend>
    
<p class="portlet-font"><fmt:message key="exception.notAuthorized.message"/></p>
<p class="portlet-msg-error"><c:out value="${msgError}"/></p>

</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>