package org.uhp.portlets.news.notification;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.uhp.portlets.news.service.NotificationService;


public class Notification {

	private static final Log LOG = LogFactory.getLog(Notification.class);

	public void sendNotification() {
		LOG.debug("Begin of sendNotification");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("AppCtxN.xml");
		final NotificationService ns = (NotificationService)ctx.getBean("NotificationService");
		ns.sendDailyEmailForCategories(); 
		LOG.debug("End of sendNotification");
	}


}
