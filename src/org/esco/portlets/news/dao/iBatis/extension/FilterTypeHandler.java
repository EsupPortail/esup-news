/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao.iBatis.extension;

import org.esco.portlets.news.domain.FilterType;

/**
 * Handler for FilterType enum.
 * @author GIP RECIA - Gribonvald Julien
 * 18 mai 2010
 */

public class FilterTypeHandler extends EnumTypeHandler<FilterType> {

    /**
     * Constructor of FilterTypeHandler.java.
     */
    public FilterTypeHandler() {
        super(FilterType.class);
    }
}