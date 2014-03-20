/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.publisher;

import java.io.InputStream;
import java.net.SocketException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.DownloadException;
import org.springframework.context.ApplicationContext;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.publisher.BaseAppContext;
import org.uhp.portlets.news.publisher.Constants;
import org.uhp.portlets.news.service.FeedService;

/**
 * created by Anyware Services - Delphine Gavalda.
 *
 * 4 juin 2010
 */
public class DownloadServlet extends BaseAppContext {

    /**
     * The id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * logger.
     */
    private static final Log LOG = LogFactory.getLog(DownloadServlet.class);

    /**
     * The encoding.
     */
    private String encoding = "UTF-8";

    /**
     * Constructor.
     */
    public DownloadServlet() {
	super();
    }

    /**
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
	super.init(config);
	String configEncoding = config.getInitParameter("encoding");
	if (configEncoding != null) {
	    encoding = configEncoding;
	}
    }

    /**
     * @throws ServletException
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    @Override
    public void service(final ServletRequest servletRequest, final ServletResponse servletResponse)
	    throws ServletException {
	HttpServletRequest request = (HttpServletRequest) servletRequest;
	HttpServletResponse response = (HttpServletResponse) servletResponse;

	final ApplicationContext context = getApplicationContext();
	final FeedService feedService = (FeedService) context.getBean("feedService");

	if (feedService == null) {
	    throw new ServletException(new IllegalStateException("feedService == null"));
	}
	HttpSession session = request.getSession();
	if (LOG.isDebugEnabled()) {
	    LOG
		    .debug("doGet: sesion uid=" + session.getAttribute(NewsConstants.UID) + " remote user="
			    + request.getRemoteUser());
	}

	final boolean isProtected;
	if (request.getServletPath().contains(Constants.PRIVATE_ACCESS)) {
	    isProtected = true;
	} else {
	    isProtected = false;
	}

	String itemId = request.getParameter("itemID");
	String downloadId = request.getParameter("downloadID");
	AttachmentD attachmentToDownload = null;
	if (itemId != null && downloadId != null) {
	    attachmentToDownload = feedService.getAttachmentToDownload(downloadId, Long.parseLong(itemId), isProtected);

	} else {
	    Exception de = new DownloadException("Unable to get the file associated to id : " + downloadId
		    + ", can not download");
	    throw new ServletException(de);
	}

	try {
	    Document cmisDocument = attachmentToDownload.getCmisDocument();
	    String contentType = cmisDocument.getContentStreamMimeType();
	    if (contentType != null) {
		response.setContentType(contentType);
	    }
	    String filename = attachmentToDownload.getFileName();
	    response.setHeader("Content-disposition", "attachment; filename=\"" + filename);

	    ContentStream content = cmisDocument.getContentStream();
	    if (content == null) {
		throw new DownloadException("data is null, can not download it");
	    }
	    response.setContentLength((int) content.getLength());
	    response.setCharacterEncoding(encoding);
	    ServletOutputStream out = response.getOutputStream();
	    InputStream stream = content.getStream();
	    try {
		int data;
		while ((data = stream.read()) != -1) {
		    out.write(data);
		}
	    } catch (SocketException e) {
		LOG.warn("SocketException was raides while downloading, " + "probably because the client cancelled");
	    } finally {
		stream.close();
		out.close();
	    }
	} catch (Throwable t) {
	    Exception de = new DownloadException(t);
	    throw new ServletException(de);
	}
    }
}
