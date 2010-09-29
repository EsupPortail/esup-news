/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

import java.io.Serializable;

/**
 * Mapping object to Filter.
 * @author GIP RECIA - Gribonvald Julien
 * 18 mai 2010
 */
public class Filter implements Serializable {

    /** Serialisation identifier. */
    private static final long serialVersionUID = -6434431166792848329L;
    
    /** Identifier. */
    private Long filterId;
    /** Attribute name. */
    private String attribute;
    /** Comparison operator. */
    private FilterOperator operator;
    /** The Search value. */
    private String value;
    /** The type of the filter. */
    private FilterType type;
    /** The entity to apply the filter on searches. */
    private Long entityId;

    /**
     * Constructeur de l'objet filter.java.
     */
    public Filter() {
        super();
    }

    /**
     * Getter du membre filterId.
     * @return <code>Long</code> le membre filterId.
     */
    public Long getFilterId() {
        return filterId;
    }

    /**
     * Setter du membre filterId.
     * @param filterId la nouvelle valeur du membre filterId. 
     */
    public void setFilterId(final Long filterId) {
        this.filterId = filterId;
    }

    /**
     * Getter du membre attribute.
     * @return <code>String</code> le membre attribute.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Setter du membre attribute.
     * @param attribute la nouvelle valeur du membre attribute. 
     */
    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    /**
     * Getter du membre operator.
     * @return <code>FilterOperator</code> le membre operator.
     */
    public FilterOperator getOperator() {
        return operator;
    }

    /**
     * Setter du membre operator.
     * @param operator la nouvelle valeur du membre operator. 
     */
    public void setOperator(final FilterOperator operator) {
        this.operator = operator;
    }

    /**
     * Getter du membre value.
     * @return <code>String</code> le membre value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter du membre value.
     * @param value la nouvelle valeur du membre value. 
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Getter du membre type.
     * @return <code>TypeFilter</code> le membre type.
     */
    public FilterType getType() {
        return type;
    }

    /**
     * Setter du membre type.
     * @param type la nouvelle valeur du membre type. 
     */
    public void setType(final FilterType type) {
        this.type = type;
    }

    /**
     * Getter du membre entityId.
     * @return <code>Long</code> le membre entityId.
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * Setter du membre entityId.
     * @param entityId la nouvelle valeur du membre entityId. 
     */
    public void setEntityId(final Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return int
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (attribute != null) {
            result += attribute.hashCode();
        }
        result = prime * result;
        if (entityId != null) {
            result += entityId.hashCode();
        }
        result = prime * result;
        if (operator != null) {
            result += operator.hashCode();
        }
        result = prime * result;
        if (type != null) {
            result += type.hashCode();
        }
        result = prime * result;
        if (value != null) {
            result += value.hashCode();
        }
        return result;
    }

    /**
     * @param obj
     * @return boolean
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Filter)) {
            return false;
        }
        Filter other = (Filter) obj;
        if (attribute == null) {
            if (other.attribute != null) {
                return false;
            }
        } else if (!attribute.equals(other.attribute)) {
            return false;
        }
        if (entityId == null) {
            if (other.entityId != null) {
                return false;
            }
        } else if (!entityId.equals(other.entityId)) {
            return false;
        }
        if (operator == null) {
            if (other.operator != null) {
                return false;
            }
        } else if (!operator.equals(other.operator)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * @return String
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Filter [filterId=");
        sb.append(filterId);
        sb.append(", attribute=");
        sb.append(attribute);
        sb.append(", operator=");
        sb.append(operator);
        sb.append(", type=");
        sb.append(type);            
        sb.append(", value=");
        sb.append(value);
        sb.append(", entityId=");
        sb.append(entityId);
        sb.append("] ");
        return sb.toString();
    }
    
    

}
