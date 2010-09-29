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
		<link rel="stylesheet" href="${ctxPath}/css/item.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="${ctxPath}/css/portlet.css" type="text/css" />
		<style type="text/css" media="screen"></style>
	</head>

<body>

<div id="container">
	<div id="header">
		<h1><c:out value="${itemV.catName}" /></h1>
	</div>
	
	<div id="content">
		<p />
		<div class='entry'>
			<h2>${itemV.item.title}</h2>
			<div class='postmeta'>
				<img src='${ctxPath}/images/rtf.png' /> <fmt:message bundle="${msg}" key="item.label.postedBy" /> 
					<span class='portlet-font'>
						<c:choose>
						<c:when test="${!empty userList[itemV.item.postedBy] && userList[itemV.item.postedBy].foundInLdap}">
							<c:out value="${userList[itemV.item.postedBy].displayName}" />
						</c:when>
						<c:otherwise>
							<fmt:message bundle="${msg}" key="news.label.userDetails.notFound">
								<fmt:param value="${itemV.item.postedBy}" />
							</fmt:message>
						</c:otherwise>
						</c:choose>
						<!--</a>-->
					</span> 
					<fmt:message bundle="${msg}" key="item.label.updated.time" /> : <span class='portlet-font'><fmt:formatDate value="${itemV.item.lastUpdatedDate}" type="both" dateStyle="long" /></span>
					<br />
			</div>
			<br />
			<c:if test="${not empty itemV.item.summary}">
				<span class='meta'>${itemV.item.summary}</span>
			</c:if>
			
			<p class='portlet-font'>${itemV.item.body}</p>
			
			<c:if test="${not empty itemV.attachments}">
				<div>
					 <c:forEach items="${itemV.attachments}" var="attachment" varStatus="attStatus">
					 	<div>
						 	<img style="padding:4px 5px 0 0; float:left" alt="${attachment.fileName}" title="${attachment.fileName}" 
						 		src="${ctxPath}/images/types/${fn:substring(attachment.fileName, fn:indexOf(attachment.fileName, '.')+1, fn:length(attachment.fileName))}.png"/>
							<p style="float:left; margin-top:0px;">
								<a href="${ctxPath}/feeds/download/${attachment.fileName}?itemID=${itemV.item.itemId}&downloadID=${attachment.cmisUid}" style="text-decoration:underline;">
									<c:out value="${attachment.title}"/>
								</a><br/> 
								<fmt:formatDate type="date" dateStyle="long" value="${attachment.insertDate}"/>
								<c:if test="${attachment.size != null}">
									- <c:out value="${attachment.size}"/> Ko
								</c:if><br/> 
								<span style="font-style:italic;"><c:out value="${attachment.description}"/></span>
							</p>
							<br style="clear:both;"/> 
						</div>
					 </c:forEach> 
				</div>
			</c:if>
		</div>
	</div>
	
	<div id="footer"> 
		<p class="portlet-font">&nbsp; 
			<IMG  alt="${portletName}" src="${ctxPath}/images/ESUP-News.gif"  align="absMiddle"> <c:out value="${portletVersion}"/> Copyright &copy; 2009 ESUP-Portail consortium 
		</p> 
	</div>
</div>
</body>
</html>