package org.cmis.portlets.news.services.exceptions;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 4 juin 2010
 */
public class CmisException extends Exception {

    private static final long serialVersionUID = 1L;

    public CmisException() {
	super();
    }

    public CmisException(String message) {
	super(message);
    }

    public CmisException(final Throwable cause) {
	super(cause);
    }

    public CmisException(final String message, Throwable cause) {
	super(message, cause);
    }
}
