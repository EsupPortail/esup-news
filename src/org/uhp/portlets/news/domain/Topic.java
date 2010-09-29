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


public class Topic extends AbstractDataSrcEdit implements Serializable {

	private static final long serialVersionUID = -2469930009367936739L;
	private Long topicId;
	private Long categoryId;
	private int count;
	private int scheduleCount;
	private int archivedCount;

	public int getScheduleCount() {
		return this.scheduleCount;
	}

	public void setScheduleCount(int scheduleCount) {
		this.scheduleCount = scheduleCount;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getArchivedCount() {
		return this.archivedCount;
	}

	public void setArchivedCount(int archivedCount) {
		this.archivedCount = archivedCount;
	}

	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

    public String toString() {
	  StringBuffer result = new StringBuffer(200);
      result.append("Topic ( id = ");
      result.append(topicId);
      result.append(", CategoryId = ");
      result.append(categoryId);
      result.append(", Topic Name = ");
      result.append(name);
      result.append(", Topic Description = ");
      result.append(desc);
      result.append(" )");
      return result.toString();
     }

}
