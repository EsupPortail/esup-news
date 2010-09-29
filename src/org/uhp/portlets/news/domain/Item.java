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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Item implements Serializable {

	private static final long serialVersionUID = 3979390351818832198L;

	private Long itemId;
	private Long categoryId;
	private String title;
	private String summary;
	private String body;
	private Date postDate;
	private Date startDate;
	private Date endDate;
	private Date lastUpdatedDate;
	private String postedBy;
	private String lastUpdatedBy;
	private String status;

	public  String getTitle() { 
		return this.title; 
	}

	public  void setTitle(String title) { 
		this.title = title; 
	}

	public  String getSummary() {
		return this.summary; 
	}

	public void setSummary(String summary) { 
		this.summary = summary;
	}

	public  String getBody() { 
		return this.body;
	}

	public  void setBody(String body) { 
		this.body = body; 
	}

	public  Date getPostDate() {
		return this.postDate;
	}

	public  void setPostDate(Date postDate) {
		this.postDate = postDate;
	}


	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public  String getPostedBy() {
		return this.postedBy; 
	}

	public  void setPostedBy(String postedBy) { 
		this.postedBy = postedBy; 
	}

	public  String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public  void setLastUpdatedBy(String lastUpdateBy) { 
		this.lastUpdatedBy = lastUpdateBy; 
	}

	public  Date getEndDate() {
		return this.endDate;
	}

	public  void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public  void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public  String getStatus() {
		return this.status;
	}

	public  void setStatus(String status) {
		this.status = status;
	}

	 public String toString() {
		 return new ToStringBuilder(this).appendSuper(super.toString())
		 .append("Item Id ", this.itemId)
		 .append("title ", this.title)  
		 .toString();
	 }

	 public Long getItemId() {
		 return itemId;
	 }

	 public void setItemId(Long itemId) {
		 this.itemId = itemId;
	 }

	 public Long getCategoryId() {
		 return categoryId;
	 }

	 public void setCategoryId(Long categoryId) {
		 this.categoryId = categoryId;
	 }

}
