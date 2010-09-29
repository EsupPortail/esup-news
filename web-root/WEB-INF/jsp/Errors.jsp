<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header_err.jsp" %>
<fieldset>
    <legend>
     <fmt:message key="error.page.title"/>
    </legend>


<p class="portlet-msg-error"><c:out value="${message}" default="No further information was provided."/></p>
<p/>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
