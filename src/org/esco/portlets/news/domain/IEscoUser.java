/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

import org.esupportail.commons.services.ldap.LdapUser;
import org.uhp.portlets.news.domain.User;

/**
 * Interface of the User Object obtained only from the database.
 * @author GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public interface IEscoUser extends User, LdapUser {

    /** Tell if the user was found in the LDAP directory. 
     * @return true if was found.
     */
    boolean isFoundInLdap();
}
