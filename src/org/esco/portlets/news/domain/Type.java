/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

import java.io.Serializable;

/**
 * Objet de mapping Type.
 * @author GIP RECIA - Gribonvald Julien
 * 2 déc. 2009
 */
public class Type implements Serializable {
    
    /** Identifiant de sérialisation.*/
    private static final long serialVersionUID = -7424895753382351343L;
    
    /** Identifiant.  */
    private Long typeId;
    /** Nom. */
    private String name;
    /** Description. */
    private String description;
    
    /**
     * Constructeur de l'objet Type.java.
     */
    public Type() {
        super();
    }
    
    /**
     * Getter du membre typeId.
     * @return <code>Long</code> le membre typeId.
     */
    public Long getTypeId() {
        return typeId;
    }
    /**
     * Setter du membre typeId.
     * @param typeId la nouvelle valeur du membre typeId. 
     */
    public void setTypeId(final Long typeId) {
        this.typeId = typeId;
    }
    /**
     * Getter du membre name.
     * @return <code>String</code> le membre name.
     */
    public String getName() {
        return name;
    }
    /**
     * Setter du membre name.
     * @param name la nouvelle valeur du membre name. 
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter du membre description.
     * @return <code>String</code> le membre description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter du membre description.
     * @param description la nouvelle valeur du membre description. 
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return The details of the type.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder();
            sb.append(" Type [typeId=");
            sb.append(typeId);
            sb.append(", description=");
            sb.append(description);
            sb.append(", name=");
            sb.append(name);
            sb.append("] ");
        return sb.toString();
    }

    /**
     * @return <code>int</code> Get the hashCode value of the instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (name == null) {
            result += name.hashCode();
        }
        return result;
    }

    /**
     * @param obj the instance of object to compare
     * @return <code>boolean</code> Check if the instance of obj is equals to this object.
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
        if (!(obj instanceof Type)) {
            return false;
        }
        Type other = (Type) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    
}