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
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.uhp.portlets.news.service.FeedService;
import org.uhp.portlets.news.util.HostUtils;


/**
 * Servlet for feeds publication.
 * modified by GIP RECIA - Gribonvald Julien.
 * 7 mai 2010
 */
public class PublishServlet extends BaseAppContext {
    
    /** */
	private static final long serialVersionUID = 1L;
	
	/** */
	private static final String MIME_TYPE = "application/xml; charset=UTF-8";
	/** */
	private static final String DEFAULT_FORMAT = "rss_2.0";
	/** */
	private static final String FEED_CREATION_ERR = "Feed Creation Error";
	/** */
	private static final int DEFAULT_DAY_COUNT = 5;
	/** */
	private static final String[] SUPPORTED_TYPES = {"rss_0.92", "rss_1.0", "rss_2.0", "atom_0.3", "atom_1.0"};
	/** */
	private static final Log LOG = LogFactory.getLog(PublishServlet.class);

	/**
     * Constructeur de l'objet PublishServlet.java.
     */
    public PublishServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws ObjectRetrievalFailureException
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, 
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response)
	throws IOException, ServletException, ObjectRetrievalFailureException {		
		final ApplicationContext context = getApplicationContext();
		final FeedService feedService = (FeedService) context.getBean("feedService");

		if (feedService == null) {
		    throw new ServletException(new IllegalStateException("feedService == null"));
		}
		
		// Id of a category
		final String entityId = request.getParameter("entityID");
		// Id of a category
		final String cId = request.getParameter("cID");
		// Id of the topic
		final String topicId = request.getParameter("topicID");			
		// name of the context
        final String typeContext = request.getParameter("type");
		// Type of feeds
		final int t = Integer.parseInt(request.getParameter("t"));
		final String fType = getFeedType(request);

//TODO need to see if it usefull or not, it's when we watch on the category or topic 
//that we can know if it's a private or public access
		final boolean isProtected = request.getServletPath().contains(Constants.PRIVATE_ACCESS) ? true : false;
		// get the host
		final String host = HostUtils.getServletHostUrl(request);

		//check if the type is well known
		if (!Arrays.asList(SUPPORTED_TYPES).contains(fType)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
			        "Feed format not supported, supported formats are : " 
			        + "rss_0.92, rss_1.0, rss_2.0, atom_0.3, atom_1.0");
		}
		// set content type of the response
		response.setContentType(MIME_TYPE);
		// returns the feeds asked
		switch (t) {
		case Constants.EXPORT_TOPIC_FEED : 
		    if (isStringNullOrEmpty(topicId))  {       
                final String msg = "A 'topicID' value is needed.";
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
            }
			final String s = feedService.getTopicFeedNotAvailableMsg(Long.valueOf(topicId), isProtected);
			if (s == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "The topic with id " + topicId + " doesn't exist.");
				return;
			}
			if (!"".equals(s)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, s);
				return;
			}			
			outputSyndFeedResponse(response, feedService.getTopicFeed(Long.valueOf(topicId), fType, host), 
			        FEED_CREATION_ERR);
			break;
		case Constants.EXPORT_MOST_RECENT : 	
			final int nbLastDays = isStringNullOrEmpty(request.getParameter("dayCount")) 
			    ? DEFAULT_DAY_COUNT : Integer.parseInt(request.getParameter("dayCount"));
			if (isStringNullOrEmpty(cId)) {
	                final String msg = "A 'cID' value is needed.";
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
	                return;
			} 
			outputSyndFeedResponse(response, feedService.getMostRecentItemsFeedOfCategory(
			        Long.valueOf(cId), fType, Integer.valueOf(nbLastDays), host), FEED_CREATION_ERR);
			break;
		case Constants.EXPORT_CAT_FEED :
	        if (!isStringNullOrEmpty(cId))  {       
	            final String msg = feedService.getFeedNotAvailableMsg(Long.valueOf(cId), isProtected);
	            if (msg == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "The category with id " + cId + " doesn't exist.");
					return;
				}
	            if (!"".equals(msg)) {
	                response.sendError(HttpServletResponse.SC_FORBIDDEN, msg);
	                return;
	            }
	        } else {
	            final String msg = "A 'cID' value is needed.";
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
	        }
		    outputFeedResponse(response, feedService.getCategoryFeed(Long.valueOf(cId), fType, host), 
		            FEED_CREATION_ERR);
		    break;
		case Constants.EXPORT_ENTITY_FEED :
		    if (isStringNullOrEmpty(entityId))  {       
                final String msg = "An 'entityID' value is needed.";
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
            }
		    if (isStringNullOrEmpty(typeContext))  {       
                final String msg = "A 'type' value is needed.";
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
            }
            outputFeedResponse(response, feedService.getEntityTypeFeed(Long.valueOf(entityId), fType, 
                    host, typeContext), FEED_CREATION_ERR);
            break;
		case Constants.EXPORT_XML_OPML :
		    if (isStringNullOrEmpty(cId))  {       
                final String msg = "A 'cID' value is needed.";
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
            }
		    outputFeedResponse(response, feedService.generateOpml(Long.valueOf(cId), host, fType), 
		            FEED_CREATION_ERR);
		    break;
		case Constants.EXPORT_XML_LEC: 	
		    if (isStringNullOrEmpty(typeContext))  {       
                final String msg = "A 'type' value is needed.";
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
                return;
            }
			outputFeedResponse(response, feedService.getFeedsOfType(typeContext, fType, host), 
			        FEED_CREATION_ERR);
			break;
		default : response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Parameter not authorized");
		break;
		}
}
	/**
	 * @param str
	 * @return boolean
	 */
	private static boolean isStringNullOrEmpty(final String str) {
		return str == null || str.equals("");
	}	

	/**
	 * @param request
	 * @return String
	 */
	private String getFeedType(final HttpServletRequest request) {
		String fType = request.getParameter("feedType");  	
		return (fType != null) ? fType : DEFAULT_FORMAT;
	}

	/**
	 * @param response
	 * @param feed
	 * @param msgErr
	 * @throws IOException
	 */
	private void outputFeedResponse(final HttpServletResponse response, final String feed, final String msgErr)
	throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.println(feed);
		} catch (NumberFormatException e) {
			LOG.error(msgErr + " : " + e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msgErr);

		}	
	}

	/**
	 * @param response
	 * @param feed
	 * @param msgErr
	 * @throws IOException
	 */
	private void outputSyndFeedResponse(final HttpServletResponse response, final SyndFeed feed, 
	        final String msgErr) throws IOException {
		SyndFeedOutput sfo = new SyndFeedOutput();		
		try {
			sfo.output(feed, response.getWriter());
		} catch (FeedException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msgErr);
		}
	}
}
