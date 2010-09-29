/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.services.ldap;

import java.util.List;

/**
 * Interface.
 * @author GIP RECIA - Gribonvald Julien
 * 9 juil. 09
 */
public interface LdapUserService extends
        org.esupportail.commons.services.ldap.LdapUserService {
    
    /** Obtain the id attribute name of an user. 
     * @return <code>String</code> the id/uid attribute name.
     */
    String getIdAttribute();
    
    /**
     * @return <code>List<String></code> values of filter on LDAP search attributes.
     */
    List<String> getFilterSearchAttributes();

}
