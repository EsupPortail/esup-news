/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * A basic editor String to List< String >.
 * @author GIP RECIA - Gribonvald Julien
 * 8 juil. 09
 */
public class ListEditor implements FactoryBean, InitializingBean {

    /** Logger.*/
    private static final Log LOG = LogFactory.getLog(ListEditor.class);
    
    /** */
    private String property;
    /** */
    private List<String> properties;
    
    /**
     * Constructor of ListEditor.java.
     */
    public ListEditor() {
        //block empty
    }
    
    /**
     * Constructor of ListEditor.java.
     * @param arg0 
     */
    public ListEditor(final String arg0) {
        this.setAsText(arg0);
    }

    /**
     * @param arg0
     * @throws IllegalArgumentException
     * @see org.springframework.beans.propertyeditors.PropertiesEditor#setAsText(java.lang.String)
     */
    private void setAsText(final String arg0) throws IllegalArgumentException {
        List<String> s = Arrays.asList(arg0.replaceAll("\\s", "").split(","));
        if (LOG.isDebugEnabled()) {
            LOG.debug("String in : " + arg0 + " List out : " + s.toString());
        }
        this.properties = s;
    }

    /**
     * @return <code>Object</code> Here returns a List of LDAP attributes names.
     * @throws Exception
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject() throws Exception {
        // TODO Auto-generated method stub
        return properties;
    }

    /**
     * @return <code>Class</code> The class name of the object returned.
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @SuppressWarnings("rawtypes")
	public Class getObjectType() {
        // TODO Auto-generated method stub
        return List.class;
    }

    /**
     * @return <code>boolean</code>
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(getProperty(), "The property property in class " 
                + this.getClass().getSimpleName() + " must not be null and not empty.");
        
        this.setAsText(getProperty());
    }

    /**
     * Getter du membre property.
     * @return <code>String</code> le membre property.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Setter du membre property.
     * @param property la nouvelle valeur du membre property. 
     */
    public void setProperty(final String property) {
        this.property = property;
    }
    
    

}
