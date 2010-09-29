/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

/**
 * Enum type for filter type (Group or LDAP).
 * @author GIP RECIA - Gribonvald Julien
 * 18 mai 2010
 */
public enum FilterType {
    /** For filter on groups. **/
    Group,
    /** For filter on LDAP (for user attributes). **/
    LDAP,

}
