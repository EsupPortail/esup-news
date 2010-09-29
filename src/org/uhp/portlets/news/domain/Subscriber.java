package org.uhp.portlets.news.domain;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.Serializable;

/**
 * modified by GIP RECIA - Gribonvald Julien.
 * 14 avr. 2010
 */
public class Subscriber implements Serializable {
    
    /** */
	private static final long serialVersionUID = 5385522494826915515L;
	/** */
	private static final String DEFAULT_SUB_TYPE = SubscribeType.SUB_FREE.toString();		
	/** */
	private static final SubscribeType DST = SubscribeType.SUB_FREE;

	/** */
	private Long id;
	/** */
	private Long ctxId;
	/** */
	private String ctxType;
	/** */
	private String principal;
	/** */
	private String displayName;
	/** */
	private int isGroup;
	/** */
	private SubscribeType st;
	/** */
	private String subType;

	/**
	 * Constructeur de l'objet Subscriber.java.
	 */
	public Subscriber() {
	    super();
	    
	}

	/**
     * Getter du membre id.
     * @return <code>Long</code> le membre id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter du membre id.
     * @param id la nouvelle valeur du membre id. 
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Getter du membre ctxId.
     * @return <code>Long</code> le membre ctxId.
     */
    public Long getCtxId() {
        return ctxId;
    }

    /**
     * Setter du membre ctxId.
     * @param ctxId la nouvelle valeur du membre ctxId. 
     */
    public void setCtxId(final Long ctxId) {
        this.ctxId = ctxId;
    }

    /**
     * Getter du membre ctxType.
     * @return <code>String</code> le membre ctxType.
     */
    public String getCtxType() {
        return ctxType;
    }

    /**
     * Setter du membre ctxType.
     * @param ctxType la nouvelle valeur du membre ctxType. 
     */
    public void setCtxType(final String ctxType) {
        this.ctxType = ctxType;
    }

    /**
     * Getter du membre principal.
     * @return <code>String</code> le membre principal.
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * Setter du membre principal.
     * @param principal la nouvelle valeur du membre principal. 
     */
    public void setPrincipal(final String principal) {
        this.principal = principal;
    }



    /**
     * Getter du membre displayName.
     * @return <code>String</code> le membre displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter du membre displayName.
     * @param displayName la nouvelle valeur du membre displayName. 
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter du membre isGroup.
     * @return <code>int</code> le membre isGroup.
     */
    public int getIsGroup() {
        return isGroup;
    }

    /**
     * Setter du membre isGroup.
     * @param isGroup la nouvelle valeur du membre isGroup. 
     */
    public void setIsGroup(final int isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * Getter du membre st.
     * @return <code>SubscribeType</code> le membre st.
     */
    public SubscribeType getSt() {
        return st;
    }

    /**
     * Setter du membre st.
     * @param st la nouvelle valeur du membre st. 
     */
    public void setSt(final SubscribeType st) {
        if (st == null) {
            this.st = DST;
        } else {
            this.st = st;
        }
    }

    /**
     * Getter du membre subType.
     * @return <code>String</code> le membre subType.
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Setter du membre subType.
     * @param subType la nouvelle valeur du membre subType. 
     */
    public void setSubType(final String subType) {
        if (subType == null) {
            this.subType = DEFAULT_SUB_TYPE;
        } else {
            this.subType = subType;
        }
    }

    /**
	 * @return <code>String</code>
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Subscriber ( id = ");
		result.append(id);
		result.append(", ctxId = ");
		result.append(ctxId);
		result.append(", CtxType = ");
		result.append(ctxType);
		result.append(", principal = ");
		result.append(principal);
		result.append(", subType = ");
		result.append(subType);
		result.append(" )");
		return result.toString();
	}		
}

