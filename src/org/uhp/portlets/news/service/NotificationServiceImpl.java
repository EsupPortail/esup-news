package org.uhp.portlets.news.service;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.domain.IEscoUser;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.ItemDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Topic;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    private static final Log LOG = LogFactory.getLog(NotificationServiceImpl.class);
    
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    private TopicDao topicDao;	
    private ItemDao itemDao;
    private EscoUserDao userDao;
    private CategoryDao categoryDao;

    private boolean enableNotification;

    public boolean isEnableNotification() {
        return enableNotification;
    }

    public void setEnableNotification(final boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public void setMailSender(final MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(final SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void setTopicDao(final TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    public void setItemDao(final ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public void setUserDao(final EscoUserDao userDao) {
        this.userDao = userDao;
    }



    public void setCategoryDao(final CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    private void sendDailyEmailForTopics(final Category category) {

        final List<Topic> topicsForToday = this.getPendingTopics(category.getCategoryId());

        if (topicsForToday.size() < 1) {		
            return;
        }

        final List<IEscoUser> managers = 
            this.getOnlyTopicManagersForTopics(category.getCategoryId(), topicsForToday);

        if (managers.isEmpty()) {
            return;
        } 
        for (IEscoUser user : managers) {
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                SimpleMailMessage message = new SimpleMailMessage(templateMessage);
                message.setTo(user.getEmail());
                String text = message.getText();
                List<Topic> userTopics = filterUserTopics(user, topicsForToday);
                String[] tIds = new String[userTopics.size()];
                StringBuilder sb = new StringBuilder();
                int i = 0;
                for (Topic t : userTopics) {
                    tIds[i++] = String.valueOf(t.getTopicId());
                    sb.append(t.getName());
                    sb.append(" [");
                    int k = itemDao.getPendingItemsCountByTopic(t.getTopicId());
                    sb.append(k + "]\n");

                }

                text = StringUtils.replace(text, "%NB%", 
                        Integer.toString(this.itemDao.getPendingItemsCountByTopics(tIds)));
                text = StringUtils.replace(text, "%CATEGORY%", category.getName());	
                text = StringUtils.replace(text, "%TOPICS%", sb.toString());
                message.setText(text);

                try {
                    LOG.debug(message);
                    this.mailSender.send(message);	        	
                } catch (MailException e) {
                    LOG.error("Notification Service:: An exception occured when sending mail, " 
                            + "have you correctly configured your mail engine ?" + e);

                }  	
            }
        }

    }

    private void sendDailyEmailForCategory(final Category category) {
        Long cId = category.getCategoryId();
        String n = "";
        LOG.debug("sendDailyEmailForCategory " + category.getName());
        List<Topic> topicsForToday = this.getPendingTopics(cId);
        if (topicsForToday.size() < 1) {
            LOG.debug("send Daily Email For Category [" + category.getName() 
                    + "] : nothing new, no notification sent");
            return;
        }
        Set<IEscoUser> managers = this.getManagersForCategory(category);		
        
        if (managers.isEmpty()) {
            return;
        }


        try {
            n = Integer.toString(
                    this.itemDao.getPendingItemsCountByCategory(category.getCategoryId()));			
        } catch (DataAccessException e1) {
            LOG.error("Notification error : " + e1.getMessage());

        }
        SimpleMailMessage message = new SimpleMailMessage(templateMessage);
        LOG.debug("send Daily Email For Category [" + category.getName() + "] : status OK");
        String[] recip = new String[managers.size()];
        int nb = 0;
        for (IEscoUser user : managers) {
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                recip[nb++] = user.getEmail();
            }
        }

        message.setTo(recip);

        String text = message.getText();
        text = StringUtils.replace(text, "%NB%", n);
        text = StringUtils.replace(text, "%CATEGORY%", category.getName());
        text = StringUtils.replace(text, "%TOPICS%", this.getPendingTopicsForCategory(cId));
        message.setText(text);

        try {
            LOG.info(message);
            mailSender.send(message);			
        } catch (MailException e) {
            LOG.error("Notification Service:: An exception occured when sending mail," 
                    + " have you correctly configured your mail engine ?" + e);
        }  

    }


    private List<Topic> filterUserTopics(final IEscoUser user, final List<Topic> topics) {
        List<Topic> filterdTopics = new ArrayList<Topic>();
        for (Topic t : topics) {
            if (this.userDao.isTopicManager(t, user)) {
                filterdTopics.add(t);
            }
        }
        return filterdTopics;
    }


    private String getPendingTopicsForCategory(final Long categoryId) {
        List<Topic> topicsForToday = getPendingTopics(categoryId);
        StringBuilder topics = new StringBuilder();
        for (Topic t : topicsForToday) {
            topics.append(t.getName() + "[" + this.itemDao.getPendingItemsCountByTopic(t.getTopicId()) + "]\n");
        }
        return topics.toString();
    }


    private List<IEscoUser> getOnlyTopicManagersForTopics(final Long cId, final  List<Topic> topics) {
        List<IEscoUser> managers = null;

        String[] tIds = new String[topics.size()];
        int i = 0;
        for (final Topic t : topics) {
            tIds[i++] = String.valueOf(t.getTopicId());			
        }
        try {
            managers = this.userDao.getManagersForTopics(cId, toInteger(tIds));
        } catch (DataAccessException e) {
            LOG.error("Erreur : pb de connexion de la base de donnees " + e.getMessage());
        }
        return managers;

    }

    private Set<IEscoUser> getManagersForCategory(final Category category) {
        Set<IEscoUser> managers = new HashSet<IEscoUser>();
        managers.addAll(this.userDao.getManagersForCategory(category.getCategoryId()));
        return managers;
    }

    private List<Topic> getPendingTopics(final Long categoryId) {
        return   this.topicDao.getPendingTopicsForCategory(categoryId);
    }

    private Integer[] toInteger(final String[] arr) {
        if (arr == null) {
            return null;
        } 

        final Integer[] res = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = Integer.valueOf(arr[i]);
        }
        return res;
    }

    public void sendDailyEmailForCategories() {
        for (Category c : this.categoryDao.getAllCategory()) {			
            this.sendDailyEmailForCategory(c);
            this.sendDailyEmailForTopics(c);
        }		
    }				
}
