package org.uhp.portlets.news.domain;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 */

import java.util.Date;

public abstract class AbstractDataSrcEdit implements DataSrcEdit {

	protected String name;

    protected String desc;
    protected String createdBy;
    protected Date creationDate ;
    protected String  refreshPeriod;
    protected Integer refreshFrequency;
                  
    protected String langue;
    protected String rssAllowed;

    protected Date lastUpdateDate;
    protected int displayOrder;
    protected String publicView;
    
    
    protected int pendingCount;
    protected int totalCount;
    
	public String getName() {	
		return name;
	}

	public void setName(String name) {
		this.name = name;
		

	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
         this.desc = desc;
	}

	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;		
	}
	

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(String refreshPeriod) {
         this.refreshPeriod = refreshPeriod;
	}

	public String getRssAllowed() {
		return this.rssAllowed;
	}

	public String isRssAllowed() {
		return this.rssAllowed;
	}

	public void setRssAllowed(String rssAllowed) {
		this.rssAllowed = rssAllowed;
	}
	

	public int getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getLangue() {
		return this.langue;
	}

	public void setLangue(String langue) {
        this.langue = langue;
	}
	
	public String isPublicView() { 
		return publicView;
	}

	public String getPublicView() {
		return this.publicView;
	}
	
	public void setPublicView(String publicView) {
		this.publicView = publicView;
	}
	
   

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Integer getRefreshFrequency() {
		return this.refreshFrequency;
	}

	public void setRefreshFrequency(Integer refreshFrequency) {
		this.refreshFrequency = refreshFrequency;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
