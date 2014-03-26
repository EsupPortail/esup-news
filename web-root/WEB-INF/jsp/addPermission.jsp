<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="d"%>
<link rel="stylesheet" type="text/css"
	href="${ctxPath}/css/displaytag.css">
<c:set var="aN" value="viewPerm" />

<c:choose>
	<c:when test="${permForm.ctxType eq 'E'}">
		<%@ include file="/WEB-INF/jsp/submenu_e.jsp"%>
		<%@ include file="/WEB-INF/jsp/header2E.jsp"%>
		<c:set var="r_cl" value="news_EntTitle" />
	</c:when>
	<c:when test="${permForm.ctxType eq 'C'}">
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
<fieldset>
<legend> <fmt:message
	key="add.permission.page.title" /> : <span class="portlet-font">
<c:choose>
	<c:when test="${permForm.ctxType eq 'E'}">
		<c:out value='${entity.name}' />
	</c:when>
	<c:when test="${permForm.ctxType eq 'C'}">
		<c:out value='${category.name}' />
	</c:when>
	<c:otherwise>
		<c:out value='${topic.name}' />
	</c:otherwise>
</c:choose> </span> </legend> <c:set var="page"
	value="${empty page ? 0 : page}" /> <c:set var="nextPage"
	value="${page == 2 ? null : page + 1}" /> <c:set var="prevPage"
	value="${page == 0 ? null : page - 1}" /> <c:set var="tit">
	<c:choose>
		<c:when test="${permForm.isGroup==0}">
			<fmt:message key="news.label.user" />
		</c:when>
		<c:otherwise>
			<fmt:message key="news.label.group" />
		</c:otherwise>
	</c:choose>
</c:set> <html:errors path="permForm" fields="true" /> <spring:nestedPath
	path="permForm">
	<portlet:actionURL var="submitPermission">
		<portlet:param name="action" value="addPermission"/>
		<portlet:param name="_page" value="${page}"/>
	</portlet:actionURL>
	<form name="${namespace}AddPerm" method="post"
		action="${submitPermission}" id="${namespace}AddPerm">
	<table border="0" cellspacing="4" cellpadding="4" width="90%">

		<c:choose>
			<c:when test="${page == 0}">
				<tr>
					<td colspan="4">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.permission.step1" /></p>
					</td>

				</tr>
				<tr>
					<c:choose>
						<c:when test="${permForm.ctxType eq 'E'}">
							<td class="portlet-form-label"><fmt:message
								key="news.label.entity" /> :</td>
							<td colspan="3" class="portlet-font"><c:out
								value="${entity.name}" /></td>
						</c:when>
						<c:when test="${permForm.ctxType eq 'C'}">
							<td class="portlet-form-label"><fmt:message
								key="news.label.category" /> :</td>
							<td colspan="3" class="portlet-font"><c:out
								value="${category.name}" /></td>
						</c:when>
						<c:otherwise>
							<td class="portlet-form-label"><fmt:message
								key="news.label.topic" /> :</td>
							<td colspan="3" class="portlet-font"><c:out
								value="${topic.name}" /></td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.research" /></td>
					<td class="portlet-font"><spring:bind path="isGroup">
						<select name="${status.expression}" size="1">
							<option value="0"
								<c:if test="${status.value == 0}">selected</c:if>><fmt:message
								key="news.label.person" /></option>
							<option value="1"
								<c:if test="${status.value == 1}">selected</c:if>><fmt:message
								key="news.label.group" /></option>
						</select>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind></td>
					<td align="right" class="portlet-form-label"><fmt:message
						key="news.label.contains" /></td>
					<td><html:input path="token" size="15" maxlength="30" /></td>
				</tr>
				<tr>
					<td class="portlet-form-label"><fmt:message
						key="news.label.role" /></td>
					<td class="portlet-font"><spring:bind path="role">
						<select name="${status.expression}" size="1">
							<c:forEach items="${roleList}" var="roleEntry">
								<option value="${roleEntry}" <c:if test="${status.value == roleEntry}">selected</c:if>><fmt:message
									key="${roleEntry}" /></option>
							</c:forEach>
						</select>
						<c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>
					</spring:bind></td>
					<td><input type="hidden" name="ctxId" value="${ctxId}" /></td>
					<td></td>
				</tr>

			</c:when>
			<c:when test="${page == 1}">
				<tr>
					<td colspan="4">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.permission.step2" /> :
					</td>
					</p>
					</td>
				</tr>
				<tr>
					<td colspan="4"><c:choose>
						<c:when test="${permForm.isGroup==1}">
							<c:choose>
								<c:when test="${fn:length(grps) > 0}">
									<spring:bind path="group">
										<table border="0" cellpadding="5" width="100%" align="center">
											<tr>
												<td>													
													<d:table name="${grps}" id="grp" export="false"
													class="dataTable" pagesize="${nbItemsToShow}" cellspacing="1" 
													sort="list" defaultsort="2" defaultorder="ascending">
													<d:column title="Select">

														<input type="radio" name="group.key"
													value="${grp.id}" />

													</d:column>

													<d:column property="id" titleKey="news.label.group.key" sortable="true" headerClass="sortable">
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

													</d:table>
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
										key="news.permission.noGrpFound" /> <br />
									<fmt:message key="news.permission.msg.goPrevious" /></div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${fn:length(userList) > 0}">
									<spring:bind path="user">
										<table border=0 cellpadding=5 width="100%">
											<tr>
												<td><d:table name="${userList}" id="user" export="false" sort="list" cellspacing="1"
													class="dataTable" pagesize="${nbItemsToShow}" defaultsort="2" defaultorder="ascending">
													<d:column title="Select">
															<input type="radio" name="user.userId"
																value="${user.userId}" />
														</d:column>
														<c:forEach items="${attrDisplay}" var="displayAttr">
															<d:column titleKey="news.label.${displayAttr}" sortable="true" headerClass="sortable">
																<c:forEach items="${user.attributes[displayAttr]}"
																	var="attrValue">
																	<c:out value="${attrValue}" />
																</c:forEach>
															</d:column>
														</c:forEach>
														<d:setProperty name="paging.banner.item_name" value="${tit}" />
														<d:setProperty name="paging.banner.items_name" value="${tit}s" />
													</d:table>
												</td>
											</tr>
											<tr>
												<td><c:if test="${fn:length(status.errorMessage) > 0}"><span class="portlet-msg-error">${status.errorMessage}</span></c:if>			</td>
											</tr>
										</table>
									</spring:bind>
								</c:when>
								<c:otherwise>
									<div>
									<p class="portlet-msg-info"><fmt:message
										key="news.permission.noUserFound" /> <br />
									<fmt:message key="news.permission.msg.goPrevious" /></p>
									</div>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose></td>
				</tr>
			</c:when>
			<c:when test="${page == 2}">
				<tr>

					<td colspan="2">
					<p class="${r_cl} portlet-font"><fmt:message
						key="news.label.permission.step3" /></p>
					</td>
				</tr>
				<c:choose>
				<c:when
					test="${(oldRole != null) && (!empty oldRole) }">
					<tr>
						<td colspan="2">
						<p class="portlet-font">
							<fmt:message key="news.label.permission.role.update">
								<fmt:param>
									<fmt:message key="${oldRole}" />
								</fmt:param>
							</fmt:message>
						</p>
						</td>
					</tr>
				</c:when>
				</c:choose>
				<tr>
					<td colspan="2">
					<table class="news_TabWb">
						<thead>
							<tr>
								<c:choose>
									<c:when test="${permForm.isGroup==1}">
										<th class="portlet-font"><fmt:message key="news.label.role" /></th>
										</th>
										<th class="portlet-font"><fmt:message
											key="news.label.group" /></th>
									</c:when>
									<c:otherwise>
										<th class="portlet-font" rowspan="2"><fmt:message
											key="news.label.role" /></th>
										<th class="portlet-font" colspan="${fn:length(attrDisplay)}"><fmt:message
											key="news.label.user" /></th>
									</c:otherwise>
								</c:choose>
							</tr>
							<c:choose>
								<c:when test="${permForm.isGroup==1}">
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
								<c:when test="${permForm.ctxType eq 'E'}">
									<td colspan="${fn:length(attrDisplay) +1}" class="line_e" />
								</c:when>
								<c:when test="${permForm.ctxType eq 'C'}">
									<td colspan="${fn:length(attrDisplay) +1}" class="line_c" />
								</c:when>
								<c:otherwise>
									<td colspan="${fn:length(attrDisplay) +1}" class="line_t" />
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="portlet-font"><fmt:message key="${permForm.role}" /></td>
							<c:choose>
								<c:when test="${permForm.isGroup==1}">
									<td class="portlet-font"><img
										title="<fmt:message key="news.img.group"/>"
										src="<html:imagesPath/>personnes.gif" border=0 /> <c:out
										value="${permForm.group.key}" /></td>
								</c:when>
								<c:otherwise>
									<c:forEach items="${attrDisplay}" var="displayAttr" varStatus="attrind">
										<td class="portlet-font"><c:choose>
											<c:when test="${attrind.first}">
												<img title='<fmt:message key="news.img.individual"/>'
													src="<html:imagesPath/>personne.gif" border=0 />
											</c:when>
										</c:choose> <c:forEach items="${user.attributes[displayAttr]}" var="attrValue">
											<c:out value="${attrValue}" />
										</c:forEach></td>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tr>
					</table>
			</c:when>
		</c:choose>
		<tr>
			<td colspan="4" align="center"><c:if test="${page !=2}"><input 
				type="submit" name="_target${nextPage}"	${empty nextPage ? "disabled" : ""} value="<fmt:message key="button.next" />" /></c:if>
				<c:if test="${page ==2}"><input type="submit" name="_finish" ${page !=2 ? "disabled" : ""} value=" <fmt:message key="button.finish" />" /></c:if>
				<c:if test="${page !=0}"><input type="submit" name="_target${prevPage}" ${empty prevPage ? "disabled" : ""} value="<fmt:message key="button.previous" />" /></c:if>
				<input type="submit" name="_cancel" value="<fmt:message key="button.cancel" />" /></td>
		</tr>
	</table>
	</form>
</spring:nestedPath></fieldset>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>