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
package org.uhp.portlets.news.domain;

/**
 *
 * Modifications : GIP RECIA - Gribonvald Julien
 * 4 févr. 2010
 */
public enum ItemStatus {
	PENDING(0, "PENDING", "view.topic.pendingItems.page.title"),
	PUBLISHED(1, "PUBLISHED", "view.topic.publishedItems.page.title"),
	ARCHIVED(2, "ARCHIVED", "view.topic.archivedItems.page.title"),	
	SCHEDULED(3, "SCHEDULED", "view.topic.scheduledItems.page.title");

	private final int id;
	private final String name;
	private final String label;

	ItemStatus(int id, String name, String label) {
		this.id = id;
		this.name = name;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static ItemStatus getItemStatusForId(int id) {
		ItemStatus[] values = values();
		for (ItemStatus v : values) {   	
			if (v.getId() == id) {
				return v;
			}
		}
		return null;
	}

}
