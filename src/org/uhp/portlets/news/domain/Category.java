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
 * modification : GIP RECIA - Gribonvald Julien
 * 9 déc. 2009
 */
public class Category extends AbstractDataSrcEdit implements Serializable {
	private static final long serialVersionUID = 7905702046965710427L;
	private Long categoryId;
	private Long entityId;
	 
	public Category() {
		super();
	}

	public Category(String name, String desc, String lang, String rssAllowed, String refreshPeriod, Integer refreshFrequency, String publicView, Long entityId) {
		super();
		setName(name);
		setDesc(desc);
		setLangue(lang);
		setRssAllowed(rssAllowed);
		setRefreshPeriod(refreshPeriod);     
		setRefreshFrequency(refreshFrequency);
		setPublicView(publicView);
		setEntityId(entityId);
	}


	public Category(String name, String desc, String lang, String rssAllowed, String refreshPeriod, int refreshFrequency, String publicView, Long entityId) {
		this(name, desc, lang, rssAllowed,refreshPeriod, Integer.valueOf(refreshFrequency), publicView, entityId);
	}


	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String toString() {
	  StringBuffer result = new StringBuffer(200);
      result.append("Category ( id = ");
      result.append(categoryId);
      result.append(", Category Name = ");
      result.append(name);
      result.append(", Category Description = ");
      result.append(desc);
      result.append(" )");
      return result.toString();

     }

}