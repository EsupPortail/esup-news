<%@ page contentType="text/html" isELIgnored="false" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="fsc" uri="/WEB-INF/tags/fileSizeConverter.tld"%>

<%@ taglib prefix="rx" uri="http://jakarta.apache.org/taglibs/regexp-1.0"%>

<portlet:defineObjects />
<c:set var="datePattern">
	<fmt:message key="date.format" />
</c:set>
<c:set var="datePatternC">
	<fmt:message key="date.format2" />
</c:set>
<c:set var="slang">
	<fmt:message key="s.lang" />
</c:set>
<fmt:parseNumber var="rC" integerOnly="true" value="16" />
<fmt:parseNumber var="rE" integerOnly="true" value="32" />
<fmt:parseNumber var="rM" integerOnly="true" value="64" />
<fmt:parseNumber var="rS" integerOnly="true" value="128" />
<c:set var="ctxPath" value="${pageContext['request'].contextPath}" />
<c:set var="portletName" value="Esup-News" />
<c:set var="portletVersion" value="@NEWS_VERSION@" />
<c:set var="namespace"><portlet:namespace /></c:set>
<c:set var="attActivate" value="@NEWS_ATTACHMENTS_ACTIVATION@" />
<c:set var="portal_compatibility" value="@NEWS_COMPATIBILTY@" />
<c:set var="portletParamPrefixe" value="" />
<c:set var="pltc_target" value="" />

<c:choose>
	<c:when test="portal_compatibility == '3_2'" >
		<rx:text id="queryString"> <%=request.getQueryString()%>
		</rx:text> <rx:regexp id="pltc_target_regexp">s/(.*)pltc_target=([^&]*)(.*)/$2_/gmi</rx:regexp>

		<c:set var="pltc_target">
		<rx:substitute regexp="pltc_target_regexp" text="queryString"/>
		</c:set>
		<c:set var="portletParamPrefixe" value="pltp_" />
	</c:when>
	<c:when test="not (portal_compatibility == '3_2') and not (portal_compatibility == '2_6')" >
	<!-- uPortal 4 Compatibility -->
		<c:set var="portletParamPrefixe" value="pP_" />
	</c:when>
</c:choose>

<link rel="stylesheet" href="${ctxPath}/css/news.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="${ctxPath}/css/portlet.css" type="text/css"/>
<script type=text/javascript>

	function slide(id) {
		var slidingElt = document.getElementById(id);
		if (slidingElt.style.display == "none" || !slidingElt.style.display) {
			 slidingElt.style.display="block";
		} else {
			slidingElt.style.display="none";
		}
	}

	function disableFilterSelect(id, val) {
		var ldapElt = document.getElementById(id+"LDAP");
		var groupElt = document.getElementById(id+"Group");
		if (val == 'LDAP'){
			groupElt.disabled=true;
			groupElt.style.display='none';
			ldapElt.disabled=false;
			ldapElt.style.display='block';
		} else {
			groupElt.disabled=false;
			groupElt.style.display='block';
			ldapElt.disabled=true;
			ldapElt.style.display='none';
		}
	}

	function checkAll(theForm) { // check all the checkboxes in the list
  		for (var i=0;i<theForm.elements.length;i++) {
    		var e = theForm.elements[i];
			var eName = e.name;
    		if (eName != 'allbox' && (e.type.indexOf("checkbox") == 0)) {
        		e.checked = theForm.allbox.checked;
			}
		}
	}

/* Function to clear a form of all it's values */
	function clearForm(frmObj) {
		for (var i = 0; i < frmObj.length; i++) {
        	var element = frmObj.elements[i];
			if(element.type.indexOf("text") == 0 ||
				element.type.indexOf("password") == 0) {
					element.value="";
			} else if (element.type.indexOf("radio") == 0) {
				element.checked=false;
			} else if (element.type.indexOf("checkbox") == 0) {
				element.checked = false;
			} else if (element.type.indexOf("select") == 0) {
				for(var j = 0; j < element.length ; j++) {
					element.options[j].selected=false;
				}
            	element.options[0].selected=true;
			}
		}
	}

	function removeInput(name) {
		var inputs = document.getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++){
			var x = inputs[i];
			if (x.getAttribute("type") == "checkbox" && x.getAttribute("name") == name) {
				x.checked = false;
			} else if (x.getAttribute("type") == "hidden" && x.getAttribute("name") == name){
				x.parentNode.removeChild(x);
			}
		}
	}

</script>
<portlet:renderURL var="homeUrl" portletMode="view">
	<portlet:param name="action" value="newsStore" />
</portlet:renderURL>
<portlet:renderURL var="helpUrl" portletMode="view">
	<portlet:param name="action" value="newsHelp" />
</portlet:renderURL>
<portlet:renderURL var="viewEntityRenderUrl">
	<portlet:param name="action" value="viewEntity" />
	<portlet:param name="eId" value="${entity.entityId}" />
</portlet:renderURL>
<portlet:renderURL var="viewCatRenderUrl">
	<portlet:param name="action" value="viewCategory" />
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty categoryForm}"><portlet:param name="cId" value="${categoryForm.category.categoryId}" /></c:if>
</portlet:renderURL>
<portlet:renderURL var="viewTopicRenderUrl">
	<portlet:param name="action" value="viewTopic" />
	<portlet:param name="tId" value="${topic.topicId}" />
	<portlet:param name="status" value="1" />
</portlet:renderURL>
<portlet:renderURL var="viewItemRenderUrl">
	<portlet:param name="action" value="viewItem" />
	<c:if test="${not empty item}"><portlet:param name="iId" value="${item.itemId}" /></c:if>
	<c:if test="${not empty itemForm}"><portlet:param name="iId" value="${itemForm.item.itemId}" /></c:if>
	<c:if test="${not empty category}"><portlet:param name="cId" value="${category.categoryId}" /></c:if>
	<c:if test="${not empty topic}"><portlet:param name="tId" value="${topic.topicId}" /></c:if>
</portlet:renderURL>

<div class="esup-news">
