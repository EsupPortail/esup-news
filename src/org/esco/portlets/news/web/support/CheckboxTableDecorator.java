/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
* @since 1501
*/
package org.esco.portlets.news.web.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.ObjectUtils;
import org.displaytag.decorator.TableDecorator;
import org.displaytag.model.TableModel;
import org.uhp.portlets.news.web.SubForm;
/**
 * Decorator pour checkbox.
 * @author GIP RECIA - Gribonvald Julien
 * 23 nov. 09
 */
public class CheckboxTableDecorator extends TableDecorator {

    /** id of the input. */
    private String id = "id";

    /** List of input's id checked. */
    private List<String> checkedIds;

    /** input name. */
    private String fieldName = "_chk";
    
    /**
     * Constructeur de l'objet CheckboxTableDecorator.java.
     */
    public CheckboxTableDecorator() {
        super();
    }

    /**
     * Setter for <code>id</code>.
     * @param id The id to set.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Setter for <code>fieldName</code>.
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Getter du membre checkedIds.
     * @return <code>List<String></code> le membre checkedIds.
     */
    public List<String> getCheckedIds() {
        return checkedIds;
    }

    /**
     * Setter du membre checkedIds.
     * @param checkedIds la nouvelle valeur du membre checkedIds. 
     */
    public void setCheckedIds(final List<String> checkedIds) {
        this.checkedIds = checkedIds;
    }

    /**
     * @param pageContext 
     * @param decorated 
     * @param tableModel 
     */
    @Override
    public void init(final PageContext pageContext, final Object decorated, @SuppressWarnings("hiding")
    final TableModel tableModel) {
        super.init(pageContext, decorated, tableModel);
                
        String[] params = pageContext.getRequest().getParameterValues(fieldName);
        List<String> checked = null;
        if (params != null) {
            checked = new ArrayList<String>(Arrays.asList(params));
        } else {
            params = ((SubForm) pageContext.getRequest().getAttribute("subForm")).getSubKey();
            if (params != null) {
                checked = new ArrayList<String>(Arrays.asList(params));
                SubForm form = (SubForm) pageContext.getRequest().getAttribute("subForm");
                pageContext.getRequest().removeAttribute("subForm");
                form.setSubKey(null);
                pageContext.getRequest().setAttribute("subForm", form);
            }
        }
        if (checked != null) {
            checkedIds = checked;
        } else {
            checkedIds = new ArrayList<String>(0);
        }
        
    }

    /**
     * 
     */
    @Override
    public void finish() {
        if (!checkedIds.isEmpty()) {
            final JspWriter writer = getPageContext().getOut();
            for (final Iterator<String> it = checkedIds.iterator(); it.hasNext();)
            {
                final String name = it.next();
                final StringBuffer buffer = new StringBuffer();
                buffer.append("<input type=\"hidden\" name=\"");
                buffer.append(fieldName);
                buffer.append("\" value=\"");
                buffer.append(name);
                buffer.append("\">");
                try {
                    writer.write(buffer.toString());
                } catch (final IOException e) {
                    // should never happen
                }
            }
        }

        super.finish();

    }

    /**
     * @return <code>String</code>
     */
    public String getCheckbox() {

        String evaluatedId = ObjectUtils.toString(evaluate(id));

        boolean checked = checkedIds.contains(evaluatedId);

        StringBuffer buffer = new StringBuffer();
        buffer.append("<input type=\"checkbox\" name=\"");
        buffer.append(fieldName);
        buffer.append("\" value=\"");
        buffer.append(evaluatedId);
        buffer.append("\"");
        if (checked) {
            checkedIds.remove(evaluatedId);
            buffer.append(" checked=\"checked\"");
        }
        buffer.append("/>");

        return buffer.toString();
    }
    
    

}