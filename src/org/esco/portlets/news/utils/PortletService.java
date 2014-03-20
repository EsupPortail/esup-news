/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.utils;

import java.util.List;

/**
 * @author jgribonvald
 * 3 mai 2012
 */
public interface PortletService {

	/*
	 ************************** METHODS *************************************/
	/**
	 * @param name
	 * @return  String
	 */
	public String getPreference(final String name);

	/**
	 * @param attribute
	 * @return List<String>
	 */
	public List<String> getUserAttribute(final String attribute);

	/**
	 * @param group
	 * @return boolean
	 */
	public boolean isUserInGroup(final String group);

	/**
	 * @return the userId;
	 */
	public String getRemoteUser();

}