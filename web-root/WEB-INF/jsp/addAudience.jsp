<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<c:set var="aN" value="viewAudience" />

<c:choose>
<%-- //Add audience for entity--%>
	<c:when test="${subForm.subscriber.ctxType eq 'E'}">
		<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
		<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
		<c:set var="r_cl" value="news_EntTitle" />
	</c:when>
<%--  //end add --%>
	<c:when test="${subForm.subscriber.ctxType eq 'C'}">
		<%@ include file="/WEB-INF/jsp/submenu_c.jsp"%>
		<%@ include file="/WEB-INF/jsp/header2C.jsp"%>
		<c:set var="r_cl" value="news_CatTitle" />
	</c:when>
	<c:otherwise>
		<%@ include file="/WEB-INF/jsp/submenu_t.jsp"%>
		<%@ include file="/WEB-INF/jsp/header2T.jsp"%>
		<c:set var="r_cl" value="news_TopTitle" />
	</c:otherwise>
</c:choose>

<div id="news_clear"></div>
<fieldset><legend> <fmt:message
	key="news.audience.page.title" /> : <span class="portlet-font">

<c:choose>
<%-- //Add audience for entity--%>
	<c:when test="${subForm.subscriber.ctxType eq 'E'}">
		<c:out value='${entity.name}' />
	</c:when>
<%--  //end add --%>
	<c:when test="${subForm.subscriber.ctxType eq 'C'}">
		<c:out value='${category.name}' />
	</c:when>
	<c:otherwise>
		<c:out value='${topic.name}' />
	</c:otherwise>
</c:choose> </span> </legend> <c:set var="page" value="${empty page ? 0 : page}" /> <c:set
	var="nextPage" value="${page == 2 ? null : page + 1}" /> <c:set
	var="prevPage" value="${page == 0 ? null : page - 1}" /> <c:set
	var="tit">
	<c:choose>
		<c:when test="${subForm.subscriber.isGroup==0}">
			<fmt:message key="news.label.user" />
		</c:when>
		<c:otherwise>
			<fmt:message key="news.label.group" />
		</c:otherwise>
	</c:choose>
</c:set> <html:errors path="subForm" fields="true" /> <spring:nestedPath
	path="subForm">
	<portlet:actionURL var="submitAudience">
		<portlet:param name="action" value="addAudience" />
		<portlet:param name="_page" value="${page}" />
	</portlet:actionURL>
	<form name="${namespace}AddAud" method="post" <%--method="get"--%>
		action="${submitAudience }" id="${namespace}AddAud">
	<table width="90%" cellpadding="5">

		<c:choose>
			<c:when test="${page == 0}">
				<tr>
					<td colspan="4">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.audience.step1" /></p>
					</td>
				</tr>
				<tr>
					<c:choose>
						<%-- //Add audience for entity--%>
						<c:when test="${subForm.subscriber.ctxType eq 'E'}">
							<td class="portlet-form-label" align="right"><fmt:message
								key="news.label.entity" /> :</td>
							<td class="portlet-font" colspan="3" align="left"><c:out
								value="${entity.name}" /></td>
						</c:when>
						<%-- //end add --%>
						<c:when test="${subForm.subscriber.ctxType eq 'C'}">
							<td class="portlet-form-label" align="right"><fmt:message
								key="news.label.category" /> :</td>
							<td class="portlet-font" colspan="3" align="left"><c:out
								value="${category.name}" /></td>
						</c:when>

						<c:otherwise>
							<td class="portlet-form-label" align="right"><fmt:message
								key="news.label.topic" /> :</td>
							<td colspan="3" class="portlet-font" align="left"><c:out
								value="${topic.name}" /></td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td class="portlet-form-label" align="right"><fmt:message
						key="news.lable.research" /></td>
					<td align="left"><spring:bind path="subForm.subscriber.isGroup">
						<select name="${status.expression}" size="1">
							<option value="1"
								<c:if test="${status.value == 1}">selected</c:if>><fmt:message
								key="news.label.group" /></option>
							<option value="0"
								<c:if test="${status.value == 0}">selected</c:if>><fmt:message
								key="news.label.person" /></option>
						</select>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind></td>
					<td class="portlet-form-label" align="right"><fmt:message
						key="news.lable.contains" /></td>
					<td align="left"><html:input path="token" size="15"
						maxlength="30" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label" align="right"><fmt:message
						key="news.label.subscription.type" /></td>
					<td class="portlet-font" align="left" colspan="3"><spring:bind
						path="subForm.subscriber.subType">
						<c:forEach items="${subTypeList}" var="subTypeEntry"
							varStatus="stat">
							<c:choose>
								<c:when
									test="${(status.value == null && stat.index == 0) || status.value == subTypeEntry}">
									<input type="radio"
										name="<c:out value='${status.expression}'/>"
										value="${subTypeEntry}" checked>
								</c:when>
								<c:otherwise>
									<input type="radio"
										name="<c:out value='${status.expression}'/>"
										value="${subTypeEntry}">
								</c:otherwise>
							</c:choose>
							<fmt:message key="${subTypeEntry}" /> &nbsp; &nbsp;
                                   </c:forEach>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind> <input type="hidden" name="subscriber.ctxId"
						value="${subForm.subscriber.ctxId}" /></td>
				</tr>

			</c:when>
			<c:when test="${page == 1}">
				<tr>
					<td colspan="4">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.audience.step2" /> <c:choose>
						<c:when test="${subForm.subscriber.isGroup==1}">
							<fmt:message key="news.label.audience.step2.g" />
						</c:when>
						<c:otherwise>
							<fmt:message key="news.label.audience.step2.p" />
						</c:otherwise>
					</c:choose> :
					</td>
					</p>
					</td>

				</tr>
				<tr>
					<td colspan="4"><%--<c:set var="checkAll">
						<input type="checkbox" name="allbox"
							onclick="checkAll(document.forms['${namespace}AddAud'])" />
					</c:set> <jsp:scriptlet>
									    <![CDATA[
									        org.esco.portlets.news.web.support.CheckboxTableDecorator decorator = new org.esco.portlets.news.web.support.CheckboxTableDecorator();
									        decorator.setFieldName("subKey");
									        pageContext.setAttribute("checkboxDecorator", decorator);
									            ]]>
									</jsp:scriptlet>--%> <c:choose>
						<c:when test="${subForm.subscriber.isGroup==1}">
							<c:choose>
								<c:when test="${fn:length(grps) > 0}">
									<spring:bind path="subKey">
										<table border="0" cellpadding="5" width="100%" align="center">
											<tr>
												<td><%--<d:table name="${grps}" id="grp" sort="list"
													requestURI="${submitAudience}" keepStatus="true"
													export="false" class="dataTable" defaultsort="2"
													defaultorder="ascending" cellspacing="1"
													pagesize="${nbItemsToShow}" form="${namespace}AddAud"
													decorator="checkboxDecorator">

													<d:column title="${checkAll}" property="checkbox"
														value="${grp.id}" />
													<d:column property="id" titleKey="news.label.group.key" sortable="true"
														headerClass="sortable" />
														--%>
													
													<d:table name="${grps}" id="grp" export="false"
													class="dataTable" pagesize="${nbItemsToShow}"
													cellspacing="1" sort="list" requestURI="${submitAudience}"
													defaultsort="2" defaultorder="ascending"
													decorator="org.displaytag.decorator.TotalTableDecorator">
													<d:column title="Select">

														<input type="checkbox" name="subKey" value="${grp.id}" />

													</d:column>

													<d:column property="id" title="GrpKey" sortable="true" headerClass="sortable">
														<input type="checkbox" name="${status.expression}"
															value="<c:out value='${grp.id}'/>" />
														<input type="hidden" name="_${status.expression}"
															value="${name}" />
													</d:column>
													<d:column property="name" titleKey="news.label.group.name"
														sortable="true" headerClass="sortable" />

													<d:setProperty name="paging.banner.item_name"
														value="${tit}" />
													<d:setProperty name="paging.banner.items_name"
														value="${tit}s" />

												</d:table><%--<span id="news_displaytable_checkboxReset"><a
													href="javascript:document.forms['${namespace}AddAud'].submit();"
													onclick="removeInput('subKey');" 
													title="<fmt:message key='link.remove.all.checked.title' />">
													<fmt:message key="link.remove.all.checked" /></a></span> --%>
													</td>
											</tr>
											<tr>
												<td><c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
												</spring:bind></td>
											</tr>
										</table>
								</c:when>
								<c:otherwise>
									<div class="portlet-font"><fmt:message
										key="news.audience.noGrpFound" /> <br />
									<fmt:message key="news.audience.msg.goPrevious" /></div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${fn:length(userList) > 0}">
									<spring:bind path="subKey">
										<table border=0 cellpadding=5 width="90%" align="center">
											<tr>
												<td><%--<d:table name="${userList}" id="user" sort="list"
													requestURI="${submitAudience}" keepStatus="true"
													export="false" class="dataTable" defaultsort="2"
													defaultorder="ascending" cellspacing="1"
													pagesize="${nbItemsToShow}" form="${namespace}AddAud"
													decorator="checkboxDecorator">

													<d:column title="${checkAll}" property="checkbox"
														value="${user.userId}" />--%>
													
													<d:table name="${userList}" id="user" sort="list" requestURI="${submitAudience}"
													export="false" class="dataTable" defaultsort="2" defaultorder="ascending" cellspacing="1"
													pagesize="${nbItemsToShow}"	decorator="org.displaytag.decorator.TotalTableDecorator">

													<d:column title="Select">
														<input type="checkbox" name="subKey"
															value="${user.userId}" />
													</d:column>
													
													<c:forEach items="${attrDisplay}" var="displayAttr">
														<d:column titleKey="news.label.${displayAttr}"
															sortable="true" headerClass="sortable">
															<c:forEach items="${user.attributes[displayAttr]}"
																var="attrValue">
																<c:out value="${attrValue}" />
															</c:forEach>
														</d:column>
													</c:forEach>

													<d:setProperty name="paging.banner.item_name"
														value="${tit}" />
													<d:setProperty name="paging.banner.items_name"
														value="${tit}s" />

												</d:table><%--<span id="news_displaytable_checkboxReset"><a
													href="javascript:document.forms['${namespace}AddAud'].submit();"
													onclick="removeInput('subKey');" 
													title="<fmt:message key='link.remove.all.checked.title' />">
													<fmt:message key="link.remove.all.checked" /></a></span>--%>
													</td>
											</tr>
											<tr>
												<td><c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if></td>
											</tr>
										</table>
									</spring:bind>
								</c:when>
								<c:otherwise>
									<div>
									<p class="portlet-msg-info"><fmt:message
										key="news.audience.noUserFound" /> <br />
									<fmt:message key="news.audience.msg.goPrevious" /></p>
									</div>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</c:when>
			<c:when test="${page == 2}">
				<tr>
					<td colspan="4">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.audience.step3" /></p>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					<table class="news_TabWb">
						<thead>
							<tr>
								<c:choose>
									<c:when test="${subForm.subscriber.isGroup==1}">
										<th class="portlet-font"><fmt:message
											key="news.label.subscription.type" /></th>
										<th class="portlet-font"><fmt:message
											key="news.label.subscribers" /></th>
									</c:when>
									<c:otherwise>
										<th class="portlet-font" rowspan="2"><fmt:message
											key="news.label.subscription.type" /></th>
										<th class="portlet-font" colspan="${fn:length(attrDisplay)}"><fmt:message
											key="news.label.subscribers" /></th>
									</c:otherwise>
								</c:choose>
							</tr>
							<c:choose>
								<c:when test="${subForm.subscriber.isGroup==1}">
								</c:when>
								<c:otherwise>
									<tr>
										<c:forEach items="${attrDisplay}" var="displayAttr">
											<th class="portlet-font"><fmt:message
												key="news.label.${displayAttr}" /></th>
										</c:forEach>
									</tr>
								</c:otherwise>
							</c:choose>
						</thead>
						<tr>
							<c:choose>
							<%-- //Add audience for entity--%>
								<c:when test="${subForm.subscriber.ctxType eq 'E'}">
									<td colspan="${fn:length(attrDisplay) +1}" class="line_e" />
								</c:when>
							<%-- //end add --%>								
								<c:when test="${subForm.subscriber.ctxType eq 'C'}">
									<td colspan="${fn:length(attrDisplay) +1}" class="line_c" />
								</c:when>
								<c:otherwise>
									<td colspan="${fn:length(attrDisplay) +1}" class="line_t" />
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="portlet-font" rowspan="${fn:length(subForm.subKey)}"><fmt:message
								key="${subForm.subscriber.subType}" /></td>
							<c:choose>
								<c:when test="${subForm.subscriber.isGroup==1}">
									<td class="portlet-font"><img
										title="<fmt:message key="news.img.group"/>"
										src="<html:imagesPath/>personnes.gif" border=0 /> <c:out
										value="${subForm.subKey[0]}" /></td>
								</c:when>
								<c:otherwise>
									<c:forEach items="${attrDisplay}" var="displayAttr"
										varStatus="attrind">
										<td class="portlet-font"><c:choose>
											<c:when test="${attrind.first}">
												<img title='<fmt:message key="news.img.individual"/>'
													src="<html:imagesPath/>personne.gif" border=0 />
											</c:when>
										</c:choose> <c:forEach items="${userList[0].attributes[displayAttr]}"
											var="attrValue">
											<c:out value="${attrValue}" />
										</c:forEach></td>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tr>
						<c:choose>
							<c:when test="${subForm.subscriber.isGroup==1}">
								<c:forEach items="${subForm.subKey}" var="sk" varStatus="skind">
									<c:choose>
										<c:when test="${! skind.first}">
											<tr>
												<td class="portlet-font"><img
													title="<fmt:message key="news.img.group"/>"
													src="<html:imagesPath/>personnes.gif" border=0 /> <c:out
													value="${sk}" /></td>
											</tr>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach items="${userList}" var="user" varStatus="userind">
									<c:choose>
										<c:when test="${! userind.first}">
											<tr>
												<c:forEach items="${attrDisplay}" var="displayAttr"
													varStatus="attrDisplayind">
													<c:choose>
														<c:when test="${attrDisplayind.first}">
															<td class="portlet-font"><img
																title='<fmt:message key="news.img.individual"/>'
																src="<html:imagesPath/>personne.gif" border=0 />
														</c:when>
														<c:otherwise>
															<td>
														</c:otherwise>
													</c:choose>
													<c:forEach items="${user.attributes[displayAttr]}"
														var="attrValue">
														<c:out value="${attrValue}" />
													</c:forEach>
													</td>
												</c:forEach>
											</tr>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</table>
					</td>
				</tr>
			</c:when>

		</c:choose>
		<tr>
			<td colspan="4" align="center"><c:if test="${page !=2}"><input 
				type="submit" name="_target${nextPage}" ${empty nextPage ? "disabled" : ""} value="<fmt:message key="button.next" />"
				<%--onclick="document.forms['${namespace}AddAud'].method='post';"--%> /></c:if> <c:if test="${page ==2}"><input
				type="submit" name="_finish"  ${page !=2 ? 'disabled' : ''} value="<fmt:message key="button.finish" />"
				<%--onclick="document.forms['${namespace}AddAud'].method='post';" --%> /></c:if> <c:if test="${page !=0}"><input
				type="submit" name="_target${prevPage}" ${empty	prevPage ? "disabled" : ""} value="<fmt:message key="button.previous" />"
				<%--onclick="document.forms['${namespace}AddAud'].method='post';" --%>/></c:if> <input
				type="submit" name="_cancel" value="<fmt:message key="button.cancel" />"
				<%--onclick="document.forms['${namespace}AddAud'].method='post';" --%>/></td>
		</tr>
	</table>
	</form>
</spring:nestedPath></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>