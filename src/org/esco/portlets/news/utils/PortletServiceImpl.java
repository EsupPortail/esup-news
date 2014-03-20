/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletRequestAttributes;

/**
 * Access to portlet services.
 * @author jgribonvald
 * 3 mai 2012
 */
public class PortletServiceImpl implements InitializingBean, PortletService {


	/*
	 ************************** PROPERTIES ******************************** */
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(PortletServiceImpl.class);

	//private AssertionAccessor assertionHandler;


	/*
	 ************************** INIT ****************************************/
	/**
	 * Default constructor.
	 */
	public PortletServiceImpl() {
		super();
	}

	/*
	 ************************** METHODS *************************************/

	/**
	 * @see org.esco.portlets.news.utils.PortletService#getPreference(java.lang.String)
	 */
	public String getPreference(final String name){
		if (LOG.isDebugEnabled()) {
			LOG.debug("getPreference(" + name + ")");
		}
		String value;
		final PortletRequestAttributes requestAttributes = (PortletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		final PortletRequest request = requestAttributes.getRequest();
		final PortletPreferences portletPreferences = request.getPreferences();
		value = portletPreferences.getValue(name, "default");

		if (value == null) {
			LOG.warn("No value for portlet preference '"
					+ name + "' returned by external service");
			/*throw new NoExternalValueException("No value for portlet preference '"
					+ name + "' returned by external service");*/
		}
		return value;

	}


	/**
	 * @see org.esco.portlets.news.utils.PortletService#getUserAttribute(java.lang.String)
	 */
	public List<String> getUserAttribute(final String attribute) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserAttribute(" + attribute + ")");
		}
		List<String> values;
			final PortletRequestAttributes requestAttributes = (PortletRequestAttributes)RequestContextHolder.currentRequestAttributes();
			final PortletRequest request = requestAttributes.getRequest();
			@SuppressWarnings("unchecked")
			Map<String, ArrayList<String>> userInfo =
					(Map<String, ArrayList<String>>) request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
			values = userInfo.get(attribute);
		if (values == null) {
			LOG.warn("User Attribute "
					+ attribute + " not found ! See your portlet.xml file for user-attribute definition.");
			/*throw new NoExternalValueException("User Attribute "
					+ attribute + " not found ! See your portlet.xml file for user-attribute definition.");*/
		}
		return values;
	}

	/**
	 * @see org.esco.portlets.news.utils.PortletService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("isUserInGroup(" + group + ")");
		}
		boolean value = Boolean.FALSE;
		final PortletRequestAttributes requestAttributes = (PortletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		final PortletRequest request = requestAttributes.getRequest();
		if (request.isUserInRole(group)) {
			value = Boolean.TRUE;
		}

		return value;
	}



	/*public String getUserProxyTicketCAS(String casTargetService) {
		return assertionHandler.getAssertion().getPrincipal().getProxyTicketFor(casTargetService);
	}*/


	/**
	 * @see org.esco.portlets.news.utils.PortletService#getRemoteUser()
	 */
	public String getRemoteUser() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getRemoteUser()");
		}
		final PortletRequestAttributes requestAttributes = (PortletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		final PortletRequest request = requestAttributes.getRequest();
		return request.getRemoteUser();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		/*Assert.notNull(TicketValidator, "property ticketValidator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(service, "property service of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(assertionHandler, "property assertionHandler of class "
				+ this.getClass().getName() + " can not be null");*/
	}

	/*
	 ************************** Accessors ************************************/


}
