/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

/**
 * Enum Type for operator values.
 * @author GIP RECIA - Gribonvald Julien
 * 19 mai 2010
 */
public enum FilterOperator {

    /** */
    EQUAL("="),
    /** */
    GE(">="),
    /** */
    LE("<="),
    /** */
    NOT_EQUAL("<>"),
    /** */
    APPROX("~=");
    
    
    /** */
    private String code;
        
    /**
     * Constructeur de l'objet FilterOperator.java.
     * @param code
     */
    private FilterOperator(final String code) {
        this.code = code;
    }

    /**
     * Getter du membre code.
     * @return <code>String</code> le membre code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter du membre code.
     * @param code la nouvelle valeur du membre code. 
     */
    public void setCode(final String code) {
        this.code = code;
    }

}
