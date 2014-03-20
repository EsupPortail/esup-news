/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.cmis.portlets.news.services.exceptions;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 4 juin 2010
 */
public class DownloadException extends Exception {
	/** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public DownloadException() {
	super();
    }

    /**
     * @param message
     */
    public DownloadException(final String message) {
	super(message);
    }

    /**
     * @param cause
     */
    public DownloadException(final Throwable cause) {
	super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DownloadException(final String message, final Throwable cause) {
	super(message, cause);
    }
}
