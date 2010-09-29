/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;
 
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.exceptions.ExceptionHandlingException;

/**
 * A safe implementation of ExceptionService, that just logs the throwable.
 */
public class SafeExceptionServiceImpl implements ExceptionService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7162199100621969582L;
	/**
	 * A logger.
	 */
	private static final Log LOG = LogFactory.getLog(SafeExceptionServiceImpl.class);

	/**
	 * The throwable caught.
	 */
	private Throwable throwable;
	
	
	/**
	 * Constructor.
	 */
	public SafeExceptionServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#setParameters(
	 * java.lang.Throwable)
	 */
	public void setParameters(
			final Throwable t) {
		throwable = t;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#handleException()
	 */
	public void handleException() throws ExceptionHandlingException {
		LOG.error(throwable);
		throw new ExceptionHandlingException(throwable);
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getApplicationName()
	 */
	public String getApplicationName() {
		return null;
	}

	

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getClient()
	 */
	public String getClient() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getCookies()
	 */
	public Set<String> getCookies() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getDate()
	 */
	public Long getDate() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getThrowable()
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getPortal()
	 */
	public String getPortal() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getQueryString()
	 */
	public String getQueryString() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getQuickStart()
	 */
	public Boolean getQuickStart() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRecipientEmail()
	 */
	public String getRecipientEmail() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestHeaders()
	 */
	public Set<String> getRequestHeaders() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestParameters()
	 */
	public Set<String> getRequestParameters() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getServer()
	 */
	public String getServer() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getSessionAttributes()
	 */
	public Set<String> getSessionAttributes() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getGlobalSessionAttributes()
	 */
	public Set<String> getGlobalSessionAttributes() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestAttributes()
	 */
	public Set<String> getRequestAttributes() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getSystemProperties()
	 */
	public Set<String> getSystemProperties() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getUserAgent()
	 */
	public String getUserAgent() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getUserId()
	 */
	public String getUserId() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getExceptionView()
	 */
	public String getExceptionView() {
		// never called since handleException() always throws an throwable
		return null;
	}

}
