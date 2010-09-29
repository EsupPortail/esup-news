package org.uhp.portlets.news.publisher;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.uhp.portlets.news.domain.ItemV;
import org.uhp.portlets.news.service.FeedService;

public class ItemServlet extends BaseAppContext {

	private static final long serialVersionUID = 1L;

	private static final String VIEW_ITEM = "/WEB-INF/jsp/item.jsp";
	private static final String VIEW_ITEMS = "/WEB-INF/jsp/items.jsp";
	private static final String NO_ITEM_VIEW = "/WEB-INF/jsp/noitem.jsp";
	private static final String UNAUTORIZED = "/WEB-INF/jsp/unauthorized.jsp";

	private static final Log log = LogFactory.getLog(ItemServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{			
		final ApplicationContext context = getApplicationContext();
		final FeedService feedService = (FeedService) context.getBean("feedService");

		if (feedService == null)
			throw new ServletException(new IllegalStateException("feedService == null"));
		HttpSession session = request.getSession();
        if (log.isDebugEnabled()) {
            log.debug("doGet: sesion uid="+session.getAttribute("uid") + " remote user="+request.getRemoteUser());
        }
		final boolean isProtected = request.getServletPath().contains(Constants.PRIVATE_ACCESS) ? true : false;
		String jspView=NO_ITEM_VIEW;
		switch (Integer.parseInt(request.getParameter("c"))) {
		case Constants.VIEW_ITEM : 
			String itemId = request.getParameter("itemID");
			if(itemId != null) {
				final ItemV iv = feedService.getItem(Long.valueOf(itemId), isProtected);
				
				if(iv != null) {		
					request.setAttribute("itemV", iv);
					List<String> usersUid = new ArrayList<String>();
					usersUid.add(iv.getItem().getPostedBy());
					usersUid.add(iv.getItem().getLastUpdatedBy());
					request.setAttribute(
					        org.uhp.portlets.news.web.support.Constants.ATT_USER_LIST, 
					        feedService.getUsersByListUid(usersUid));
					jspView = VIEW_ITEM;
				}			     			      
			}
			break;
		case Constants.VIEW_ITEMS :
			final String uid = request.getRemoteUser();
			if(log.isDebugEnabled()) {
				log.debug("View list items: remote user="+request.getRemoteUser());
			}
			if(!isProtected || uid == null) {
				jspView = UNAUTORIZED;				
			}
			else {		
				request.setAttribute("itemsS", feedService.getItems(Long.valueOf(request.getParameter("tID")), Integer.parseInt(request.getParameter("s")), uid));
				jspView = VIEW_ITEMS;
			}
			break;
		default : response.sendError(HttpServletResponse.SC_FORBIDDEN, "Parameter not authorized");
		break;
		}    
		final ServletContext app = getServletContext();
		final RequestDispatcher dispatcher = app.getRequestDispatcher(jspView); 
		dispatcher.forward(request,response);
	}
}
