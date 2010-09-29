/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.dao.iBatis.extension;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;

/**
 * Generic Handler for enum type.
 * @author GIP RECIA - Gribonvald Julien
 * 18 mai 2010
 * @param <E>
 */
@SuppressWarnings("unchecked")
public abstract class EnumTypeHandler<E extends Enum> implements TypeHandlerCallback {
    /** Class. */
    private Class<E> enumClass;

    /**
     * Constructor of EnumTypeHandler.java.
     * @param enumClass
     */
    public EnumTypeHandler(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * @param setter
     * @param parameter
     * @throws SQLException
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#
     * setParameter(com.ibatis.sqlmap.client.extensions.ParameterSetter, java.lang.Object)
     */
    public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
        setter.setString(((E) parameter).toString());
    }

    /**
     * @param getter
     * @return Object
     * @throws SQLException
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#
     * getResult(com.ibatis.sqlmap.client.extensions.ResultGetter)
     */
    public Object getResult(final ResultGetter getter) throws SQLException {
        return valueOf(getter.getString());
    }

    /**
     * @param s
     * @return Object
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#valueOf(java.lang.String)
     */
    public Object valueOf(final String s) {
        return Enum.valueOf(this.enumClass, s);
    }
}

