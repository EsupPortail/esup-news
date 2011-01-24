<%@ page contentType="text/html" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctxPath" value="${pageContext['request'].contextPath}" />
<c:set var="portletName" value="Esup-News" />
<c:set var="portletVersion" value="@NEWS_VERSION@" />

<fmt:setBundle basename="messages" var="msg" />
<html>
<head>
<link rel="stylesheet" href="${ctxPath}/css/item.css" type="text/css"
	media="screen" />
<link rel="stylesheet" href="${ctxPath}/css/portlet.css"
	type="text/css" />
<style type="text/css" media="screen">
</style>
</head>
<body>
<div id="container">

<div id="header">
<h1>${itemsS.catName}</h1>
</div>

<div id="content">
<br/>
<div class="topicText"><fmt:message bundle="${msg}" key="news.label.topic"/> : ${itemsS.topic.name}</div><p class="Text"><fmt:message bundle="${msg}" key="${itemsS.itemStatus.label}"/></P>
<c:choose>
	<c:when test="${empty itemsS.items}">

		<fmt:message bundle="${msg}" key="news.msg.empty_list" />
	</c:when>
	<c:otherwise>
		<c:forEach items="${itemsS.items}" var="item">
			<div class='entry'>
			<p class="portlet-table-subheader"><img
				src="${ctxPath}/images/puce_ann.gif" alt="" /><c:out
				value="${item.title}" /></p>
			<p class="postmeta"><img src="${ctxPath}/images/rtf.png" alt="" />
			<fmt:message bundle="${msg}" key="item.label.created.by" /> <span
				class="portlet-font"><a class="lien"
				href="<portlet:renderURL>
     						<portlet:param name="action" value="userDetails"/>
     						<portlet:param name="userId" value="${item.postedBy}"/>
     						<portlet:param name="iId" value="${item.itemId}"/>
               				<portlet:param name="cId" value="${item.categoryId}"/>     
					</portlet:renderURL>">
			<c:choose>
				<c:when
					test="${!empty userList[item.postedBy] && userList[item.postedBy].foundInLdap}">
					<c:out value="${userList[item.postedBy].displayName}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="news.label.userDetails.notFound">
						<fmt:param value="${item.postedBy}" />
					</fmt:message>
				</c:otherwise>
			</c:choose></a></span> <fmt:message bundle="${msg}" key="item.label.start.Date" /> <span
				class="portlet-font"><fmt:formatDate value="${item.postDate}"
				type="both" dateStyle="long" /></span> <fmt:message bundle="${msg}"
				key="item.label.updated.time" /> <span class="portlet-font"><fmt:formatDate
				value="${item.lastUpdatedDate}" type="both" dateStyle="long" /></span> <br />
			<fmt:message bundle="${msg}" key="item.label.start.time" /> <span
				class="portlet-font"><fmt:formatDate
				value="${item.startDate}" type="date" dateStyle="long" /></span> <fmt:message
				bundle="${msg}" key="item.label.end.time" /> <span
				class="portlet-font"><fmt:formatDate value="${item.endDate}"
				type="date" dateStyle="long" /></span></p>
			<c:if test="${not empty item.summary}">
				<div class="meta"><c:out value="${item.summary}" /></div>
				</c:if> 
				<c:out value="${item.body}" />
				</div>
		</c:forEach>
	</c:otherwise>
</c:choose>
</div>
<div id="footer"> <p class="portlet-font">&nbsp; 
<IMG  alt="${portletName}" src="${ctxPath}/images/ESUP-News.gif"  align="absMiddle"> <c:out value="${portletVersion}"/> Copyright &copy; 2009 ESUP-Portail consortium </p> </div>

</div>
</body>
</html>