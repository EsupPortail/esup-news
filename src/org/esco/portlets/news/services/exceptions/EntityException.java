/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.services.exceptions;

import org.esupportail.commons.exceptions.EsupException;

/**
 * Exception on entity.
 * @author GIP RECIA - Gribonvald Julien
 * 10 déc. 2009
 */
public class EntityException extends EsupException {

    /** Identifiant de sérialisation. */
    private static final long serialVersionUID = 31294391483408130L;

    /**
     * Constructeur de l'objet EntityException.java.
     */
    public EntityException() {
        super();
    }

    /**
     * Constructeur de l'objet EntityException.java.
     * @param message
     */
    public EntityException(final String message) {
        super(message);
    }

    /**
     * Constructeur de l'objet EntityException.java.
     * @param cause
     */
    public EntityException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructeur de l'objet EntityException.java.
     * @param message
     * @param cause
     */
    public EntityException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
