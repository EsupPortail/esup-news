/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Objet de mapping Entity.
 * @author GIP RECIA - Gribonvald Julien
 * 2 déc. 2009
 */
public class Entity implements Serializable {

    /** Identifiant de sérialisation. */
    private static final long serialVersionUID = 3179838796914652052L;
    
    /**
     * Identifiant.
     */
    private Long entityId;
    /**
     * Nom.
     */
    private String name;
    /**
     * Uid de l'utilisateur ayant créé l'entité.
     */
    private String createdBy;
    /**
     * Date de création.
     */
    private Date creationDate;
    /**
     * Description.
     */
    private String description;
    
    /**
     * Constructeur de l'objet Entity.java.
     */
    public Entity() {
        super();
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
     * Getter du membre createdBy.
     * @return <code>String</code> le membre createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * Setter du membre createdBy.
     * @param createdBy la nouvelle valeur du membre createdBy. 
     */
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Getter du membre creationDate.
     * @return <code>Date</code> le membre creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * Setter du membre creationDate.
     * @param creationDate la nouvelle valeur du membre creationDate. 
     */
    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
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
     * @return A String of the descrition of the object.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb.append(" Entity [entityId=");
            sb.append(entityId);
            sb.append(", createdBy=");
            sb.append(createdBy);
            sb.append(", creationDate=");
            sb.append(creationDate);
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
        if (name != null) {
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
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) obj;
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