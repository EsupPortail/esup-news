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

	import java.util.Date;

	public interface DataSrcEdit {
	 
		public String getName();
	    public void setName(String name);
	    
	    public String getDesc();
	    public void setDesc(String desc);
	    
	    public String getCreatedBy();
	    public void setCreatedBy(String createdBy);
	    
	    public Date getCreationDate();
	    public void setCreationDate(Date creationDate);
	    
	    public String getRefreshPeriod();
	    public void setRefreshPeriod(String refreshPeriod);
	    
	    public Integer getRefreshFrequency();
	    public void setRefreshFrequency(Integer refreshFrequency);
	    
	    public String isRssAllowed();
	    public String getRssAllowed();
	    public void setRssAllowed(String allow);
	    
	    public int getDisplayOrder();
	    public void setDisplayOrder(int display_order);
	    
	    public String getLangue();
	    public void setLangue(String langue);
	    
	    public Date getLastUpdateDate();
		public void setLastUpdateDate(Date lastUpdateDate);
		
	    public String isPublicView();
	    public String getPublicView();
	    public void setPublicView(String pv);
	    	    
	}
