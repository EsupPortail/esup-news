<%@ page contentType="text/html" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setBundle basename="messages" var="msg" />

<html>
<head>
<link rel='stylesheet' type='text/css' href='/newsPortlet/css/item.css' />
</head>
<link rel='stylesheet' type='text/css'
	href='/newsPortlet/css/portlet.css' />
</head>
<body>
<div id='container'>
<p />
<div id='content'>
<div class='entry'>
<p class="portlet-msg-error">
<div class="error">&nbsp; &nbsp; <fmt:message bundle="${msg}"
	key="news.alert.noAccessToSecureItems" /></div>
</p>
<div class='line_c'></div>
</div>
</div>
</div>
</body>
</html>