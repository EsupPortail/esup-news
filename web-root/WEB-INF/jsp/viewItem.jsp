<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/submenu_i.jsp"%>
<c:set var="aN" value="view" />
<%@ include file="/WEB-INF/jsp/header2I.jsp"%>

<div id="news_clear"></div>
	<div class="texteBasPage">
<c:if test='${pMask ge rM}'>
	[<a href='<portlet:actionURL>
                                                <portlet:param name="action" value="apprItem"/>
                                                <portlet:param name="id" value="${item.itemId}"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                        </portlet:actionURL>'
		class="liensmenudroit"><c:choose>
		<c:when test="${item.status eq 1}">
			<img title='<fmt:message key="button.invalidate"/>' align="absMiddle"
				src="<html:imagesPath/>inval.gif" border="0" />
			<fmt:message key="button.invalidate" />
		</c:when>
		<c:otherwise>
			<img title='<fmt:message key="button.validate"/>'
				src="<html:imagesPath/>val.gif" border="0" align="absMiddle" />
			<fmt:message key="button.validate" />
		</c:otherwise>
	</c:choose> </a>]
</c:if>

<c:if test='${(pMask ge rM) or (pMask ge rC && userId eq item.postedBy)}'>
	[<a
		href='<portlet:actionURL>
                                                <portlet:param name="action" value="deleteItem"/>
                                                <portlet:param name="id" value="${item.itemId}"/>
                                                <portlet:param name="tId" value="${topic.topicId}"/>
                                                <portlet:param name="status" value="1"/>  
                                                <portlet:param name="all" value="1"/>                                                 
                                        </portlet:actionURL>'
        onClick="return confirm('<fmt:message key="news.img.title.delete.item"/>');"
		class="liensmenudroit"> <img
		title='<fmt:message key="news.img.title.delete.item"/>'
		align="absMiddle" src="<html:imagesPath/>delete.gif" border="0" /><fmt:message
		key="button.delete" /> </a>]
</c:if>
</div>
<div id="news_clear"></div>
<fieldset>
<legend> <fmt:message
	key="view.item.page.title" /> </legend>

<div id="news_content">
<div class="news_entry">
<h2><c:out value="${item.title}" /></h2>
<p class="postmeta"><img src="<html:imagesPath/>rtf.png" alt="" />
<fmt:message key="item.label.postedBy" /> : <span class="portlet-font"><a
	class="lien"
	href="<portlet:renderURL>
     						<portlet:param name="action" value="userDetails"/>
     						<portlet:param name="uid" value="${item.postedBy}"/>
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
</c:choose></a></span> 
	<fmt:message key="item.label.created.date" /> : <span class="portlet-font">
	
	<fmt:formatDate value="${item.postDate}" type="both" timeStyle="long" dateStyle="long" /></span> 
	
	<fmt:message key="item.label.start.time" /> : <span class="portlet-font">
	
	<fmt:formatDate	value="${item.startDate}" type="date" dateStyle="long" /></span> 
	
	<fmt:message key="item.label.end.time" /> : <span class="portlet-font">
	
	<fmt:formatDate	value="${item.endDate}" type="date" dateStyle="long" /></span> <br />
	[ <img src="<html:imagesPath/>icone_entity.gif" align="absMiddle" /> <a href='${viewEntityRenderUrl}'>
	
	<span class="entity"><c:out value="${entity.name}" /></span></a> ] [ 
	
	<img src="<html:imagesPath/>icone_cat_p2.gif" align="absMiddle" /> 
	
	<a href='${viewCatRenderUrl}'><span class="cat"><c:out value="${category.name}" /></span></a> ] [ 
	
	<c:forEach items="${topicList}" var="topic" varStatus="topicStatus">
		<img src="<html:imagesPath/>icone_theme_p2.gif" align="absMiddle" border="0" />
		<a href='<portlet:renderURL>
				<portlet:param name="action" value="viewTopic" />
				<portlet:param name="tId" value="${topic.topicId}" />
				<portlet:param name="status" value="1" />
			</portlet:renderURL>'><span class="topic"><c:out value="${topic.name}" /></span></a>
			
		<c:if test="${not topicStatus.last}">  ||</c:if> 
	</c:forEach>
	 ] </span></p>
		
		<div class="news_meta"><c:out value="${item.summary}" /></div>
		
		<p class="portlet-font"><c:out value="${item.body}" escapeXml="false" /></p>
		
			<c:if test="${attActivate eq 'true'}">
			<c:if test="${not empty attachmentList}">
			<div>
				 <c:forEach items="${attachmentList}" var="attachment" varStatus="attStatus">
					<div>
					<!-- ${pageContext['request'].contextPath}/internal/download/${attachment.fileName}?itemID=${item.itemId}&downloadID=${attachment.cmisUid} -->
					           		
					 	<img style="padding:4px 5px 0 0; float:left" alt="${attachment.fileName}" title="${attachment.fileName}" 
					 		src="<html:imagesPath/>types/${attachment.type}.png"/>
						<p style="float:left; margin-top:0px;">
							<a href="${pageContext['request'].contextPath}/internal/download/${attachment.fileName}?itemID=${item.itemId}&downloadID=${attachment.cmisUid}">
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
			</c:if>
		</div>
	</div>
</div>
</fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>