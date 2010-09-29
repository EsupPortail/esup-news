package org.cmis.portlets.news.services.exceptions;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 4 juin 2010
 */
public class DownloadException extends Exception {

    private static final long serialVersionUID = 1L;

    public DownloadException() {
	super();
    }

    public DownloadException(String message) {
	super(message);
    }

    public DownloadException(final Throwable cause) {
	super(cause);
    }

    public DownloadException(final String message, Throwable cause) {
	super(message, cause);
    }
}
