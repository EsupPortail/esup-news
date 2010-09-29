/**
 * 
 */
package org.cmis.portlets.news.publisher;

import java.io.InputStream;
import java.net.SocketException;

import javax.portlet.PortletSecurityException;
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
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.CmisAttachmentDao;
import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.DownloadException;
import org.esco.portlets.news.services.UserManager;
import org.springframework.context.ApplicationContext;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.publisher.BaseAppContext;
import org.uhp.portlets.news.service.ItemManager;

/**
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 4 juin 2010
 */
public class InternalDownloadServlet extends BaseAppContext {

    /**
     * The id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * logger.
     */
    private static final Log LOG = LogFactory.getLog(InternalDownloadServlet.class);

    /**
     * The encoding.
     */
    private String encoding = "UTF-8";

    /**
     * Constructor.
     */
    public InternalDownloadServlet() {
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
	final CmisAttachmentDao dao = (CmisAttachmentDao) context.getBean("cmisAttachmentDao");
	final CategoryDao categoryDao = (CategoryDao) context.getBean("categoryDao");
	final UserManager um = (UserManager) context.getBean("escoUserManager");
	final ItemManager im = (ItemManager) context.getBean("itemManager");

	if (dao == null) {
	    throw new ServletException(new IllegalStateException("cmisAttachmentDao == null"));
	}
	if (categoryDao == null) {
	    throw new ServletException(new IllegalStateException("categoryDao == null"));
	}
	if (um == null) {
	    throw new ServletException(new IllegalStateException("escoUserManager == null"));
	}
	if (im == null) {
	    throw new ServletException(new IllegalStateException("itemManager == null"));
	}

	HttpSession session = request.getSession();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("doGet: sesion uid=" + session.getAttribute("uid"));
	}

	String userUid = (String) session.getAttribute("uid");
	if (userUid == null) {
	    throw new ServletException(new PortletSecurityException("you are not authorized for this action"));
	}
	String itemId = request.getParameter("itemID");
	String downloadId = request.getParameter("downloadID");

	Item item = im.getItemById(Long.valueOf(itemId));
	if (item == null) {
	    throw new ServletException(new IllegalStateException("Item with id : " + itemId + " does not exist"));
	}
	AttachmentD attachmentToDownload = null;
	if (um.canValidate(userUid, item)) {
	    try {
		if (itemId != null && downloadId != null) {
		    Category c = categoryDao.getCategoryById(item.getCategoryId());
		    try {
			attachmentToDownload = dao.getAttachmentById(downloadId, c.getEntityId());
		    } catch (CmisObjectNotFoundException e) {
			Exception de = new DownloadException("Unable to get the file associated to id : " + downloadId
				+ ", can not download it");
			throw new ServletException(de);
		    }

		} else {
		    Exception de = new DownloadException("Unable to get the file associated to id : " + downloadId
			    + ", can not download it");
		    throw new ServletException(de);
		}

		Document cmisDocument = attachmentToDownload.getCmisDocument();
		String contentType = cmisDocument.getContentStreamMimeType();
		if (contentType != null) {
		    response.setContentType(contentType);
		}
		String filename = cmisDocument.getContentStreamFileName();
		response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");

		ContentStream content = cmisDocument.getContentStream();
		if (content == null) {
		    throw new DownloadException("data is null, can not download");
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
		    LOG
			    .warn("SocketException was raides while downloading, "
				    + "probably because the client cancelled");
		} finally {
		    stream.close();
		    out.close();
		}
	    } catch (Throwable t) {
		Exception de = new DownloadException(t);
		throw new ServletException(de);
	    }
	} else {
	    throw new ServletException(new PortletSecurityException("you are not authorized for this action"));
	}
    }

}
