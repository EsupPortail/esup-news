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
import java.util.List;

import org.cmis.portlets.news.domain.Attachment;

public class ItemV implements Serializable {

    private static final long serialVersionUID = 1L;
    private Item item;
    private String catName;

    private List<Attachment> attachments;

    public String getCatName() {
	return catName;
    }

    public void setCatName(String catName) {
	this.catName = catName;
    }

    public Item getItem() {
	return item;
    }

    public void setItem(Item item) {
	this.item = item;
    }

    public void setAttachments(List<Attachment> attachments) {
	this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
	return attachments;
    }

}
