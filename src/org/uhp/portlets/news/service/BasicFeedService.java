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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.AttachmentDao;
import org.cmis.portlets.news.dao.CmisAttachmentDao;
import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.esco.portlets.news.dao.EntityDAO;
import org.esco.portlets.news.dao.TypeDAO;
import org.esco.portlets.news.domain.Entity;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.domain.Type;
import org.esco.portlets.news.services.RoleManager;
import org.esco.portlets.news.services.UserManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.uhp.portlets.news.NewsConstants;
import org.uhp.portlets.news.dao.CategoryDao;
import org.uhp.portlets.news.dao.ItemDao;
import org.uhp.portlets.news.dao.SubscriberDao;
import org.uhp.portlets.news.dao.TopicDao;
import org.uhp.portlets.news.domain.Category;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.ItemStatus;
import org.uhp.portlets.news.domain.ItemV;
import org.uhp.portlets.news.domain.ItemsView;
import org.uhp.portlets.news.domain.SubscribeType;
import org.uhp.portlets.news.domain.Subscriber;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.publisher.Constants;
import org.uhp.portlets.news.service.exception.NoSuchItemException;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;




/**
 * Implementation of FeedService.
 * modified by GIP RECIA - Gribonvald Julien
 * 10 mai 2010
 */
public class BasicFeedService implements FeedService, InitializingBean {
	/** */
	private static final String PUBLIC_ACCESS = "public";
	/** */
	private static final String PRIVATE_ACCESS = "cas";
	/** */
	private static final String PER_HOUR = "hour";
	/** */
	private static final String PER_DAY = "day";
	/** */
	private static final String DESC_CONTENT_TYPE = "text/plain";
	/** */
	private static final String RSS_NOT_ALLOWED = "RSS feed is not allowed for this ";
	/** */
	private static final String ACC_BY_AUTH = "A secure feed is only  accessible with authentication";
	/** */
	private static final Log LOG = LogFactory.getLog(BasicFeedService.class);
	/** */
	private static final int DEFAULT_TIMEOUT = 3600;

	/** Entity Dao. **/
	@Autowired
	private EntityDAO entityDao;
	/** type Dao. **/
	@Autowired
	private TypeDAO typeDao;
	/** Topic Dao. **/
	@Autowired
	private TopicDao topicDao;
	/** Category Dao. **/
	@Autowired
	private CategoryDao categoryDao;
	/** Item Dao. */
	@Autowired
	private ItemDao itemDao;
	/** Subscriber Dao. **/
	@Autowired
	private SubscriberDao subDao;
	/** User Manager. */
	@Autowired
	private UserManager um;
	/** Role Manager. */
	@Autowired
	private RoleManager rm;
	/** Attachment Dao. */
	@Autowired
	private AttachmentDao attachmentDao;
	/** CMIS dao. */
	@Autowired
	private CmisAttachmentDao cmisDao;

	/** Timeout in paramters */
	private Integer timeout;

	/** Group Store key to remove from resource generation when used with user attributes.*/
	private String groupStoreName = "smartldap.";

	/**
	 * Constructor of BasicFeedService.java.
	 */
	public BasicFeedService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param feedUrl
	 * @param fType
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService#
	 * generateOpml(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public String generateOpml(final Long id, final String feedUrl, final String fType) {
		try {
			final Category c = this.categoryDao.getCategoryById(id);
			final List<Topic> topics = topicDao.getTopicListByCategory(id);
			final StringBuilder sbuf = new StringBuilder();
			sbuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<opml version=\"1.0\">\n");
			sbuf.append("\n<head><title>" + StringEscapeUtils.escapeXml(c.getName()) + "</title></head>\n\n<body>");
			sbuf.append("<outline text=\"" + StringEscapeUtils.escapeXml(c.getName()) + "\">\n");
			for (Topic t : topics) {
				if (NewsConstants.S_Y.equals(t.getRssAllowed())) {
					final String path = (NewsConstants.S_Y.equals(t.getPublicView()))
					? NewsConstants.PUBLIC_PATH : NewsConstants.PRIVATE_PATH;
					sbuf.append("<outline type=\"rss\" text=\""
							+ StringEscapeUtils.escapeXml(t.getName())
							+ "\"  title=\"" + StringEscapeUtils.escapeXml(t.getName())
							+ "\" description=\"" + StringEscapeUtils.escapeXml(t.getDesc())
							+ "\" xmlUrl=\""  + feedUrl + path
							+ "rss?t=" + Constants.EXPORT_TOPIC_FEED
							+ "&amp;topicID=" + t.getTopicId()
							+ "&amp;feedType=" + fType + "\" /> \n");
				}
			}
			sbuf.append("\n</outline>\n</body>\n</opml>");
			return sbuf.toString();
		} catch (DataAccessException e) {
			LOG.error("Error while generating opml feed : " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @param id
	 * @param fType
	 * @param feedUrl
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService#
	 * getCategoryFeed(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public String getCategoryFeed(final Long id, final String fType, final String feedUrl) {
		StringBuilder sbuf = new StringBuilder();
		sbuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		try {
			sbuf.append(getSimpleCategoryFeed(id, fType, feedUrl));
			return sbuf.toString();
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 * @param id
	 * @param fType
	 * @param feedUrl
	 * @return String
	 * @throws DataAccessException
	 */
	private StringBuilder getSimpleCategoryFeed(final Long id, final String fType, final String feedUrl)
	throws DataAccessException {
		StringBuilder categoryBuffer = new StringBuilder();
		try {
			final Category c = this.categoryDao.getCategoryById(id);
			final List<Topic> topics = this.topicDao.getTopicListByCategory(c.getCategoryId());
			Integer ttl = getTTL(c.getRefreshPeriod(), c.getRefreshFrequency());
			categoryBuffer.append("<category edit=\"all\" name=\"" + StringEscapeUtils.escapeXml(c.getName())
					+ "\" ttl=\"" + ttl.toString() + "\">\n");
			categoryBuffer.append("<description>" + StringEscapeUtils.escapeXml(c.getDesc()) + "</description>\n");
			final boolean hasEntitySubs = this.subDao.hasSubscribers(c.getEntityId(), NewsConstants.CTX_E);
			final boolean hasCatSubs = this.subDao.hasSubscribers(c.getCategoryId(), NewsConstants.CTX_C);
			String path;
			// Buffer of the list of categoryProfile from topics
			StringBuilder topicsBuffer = new StringBuilder();
			// Buffer of the list of the visibility of categoryProfile from topics
			StringBuilder topicsVisibilityBuffer = new StringBuilder();
			final String visibilityEntity = this.getCtxVisibility(c.getEntityId(), NewsConstants.CTX_E);
			for (final Topic t : topics) {
				if (NewsConstants.S_Y.equals(t.getRssAllowed())) {
					final boolean hasTopSubs = this.subDao.hasSubscribers(t.getTopicId(), NewsConstants.CTX_T);
					ttl = getTTL(t.getRefreshPeriod(), t.getRefreshFrequency());
					path = (NewsConstants.S_Y.equals(t.getPublicView()))
					? NewsConstants.PUBLIC_PATH : NewsConstants.PRIVATE_PATH;
					final String acc = (NewsConstants.S_Y.equals(t.getPublicView()))
					? PUBLIC_ACCESS : PRIVATE_ACCESS;
					topicsBuffer.append("<sourceProfile id=\"" + t.getTopicId() + "\" access=\"" + acc
							+ "\" name=\"" + StringEscapeUtils.escapeXml(t.getName())
							+ "\" specificUserContent=\"no\" ttl=\""
							+ ttl.toString() + "\" url=\"" + feedUrl + path
							+ "rss?t=" + Constants.EXPORT_TOPIC_FEED
							+ "&amp;topicID=" + t.getTopicId()
							+ "&amp;feedType=" + fType + "\"> \n");
					if (!hasCatSubs && !hasTopSubs) {
						topicsBuffer.append(visibilityEntity);
					} else {
						String vis = this.getCtxVisibility(t.getTopicId(), NewsConstants.CTX_T);
						topicsBuffer.append(vis);
						topicsVisibilityBuffer.append(vis);
					}
					topicsBuffer.append("\n</sourceProfile>");
				}
			}
			if (hasCatSubs) {
				categoryBuffer.append(getCtxVisibility(c.getCategoryId(), NewsConstants.CTX_C));
			} else if (hasEntitySubs) {
				categoryBuffer.append(this.getCtxVisibility(c.getEntityId(), NewsConstants.CTX_E));
			} else {
				categoryBuffer.append(topicsVisibilityBuffer);
			}

			categoryBuffer.append("<sourceProfiles>\n");
			categoryBuffer.append(topicsBuffer);
			categoryBuffer.append("</sourceProfiles>\n</category>\n");
			return categoryBuffer;
		} catch (DataAccessException e) {
			LOG.error("Error while generating category feed : " + e.getLocalizedMessage());
			throw e;
		}
	}



	/**
	 * @param id of the Category
	 * @param feedType
	 * @param nbLastDays
	 * @param urlEntry
	 * @return SyndFeed
	 * @see org.uhp.portlets.news.service.FeedService#
	 * getMostRecentItemsFeedOfCategory(Long, String, Integer, String)
	 */
	public SyndFeed getMostRecentItemsFeedOfCategory(final Long id, final String feedType,
			final Integer nbLastDays, final String urlEntry) {
		try {
			final Category c = this.categoryDao.getCategoryById(id);
			final List<Item> items =
				this.itemDao.getMostRecentItemsByCategory(c.getCategoryId(), nbLastDays);
			SyndFeed feed = new SyndFeedImpl();
			feed.setFeedType(feedType);
			try {
				feed.setTitle(c.getName());
				final String path = (NewsConstants.S_Y.equals(c.getPublicView()))
				? NewsConstants.PUBLIC_PATH : NewsConstants.PRIVATE_PATH;
				feed.setLink(urlEntry + path + "rss?t=" + Constants.EXPORT_MOST_RECENT
						+ "&cID=" + c.getCategoryId()
						+ "&feedType=" + feedType + "&dayCount=" + nbLastDays.intValue());
				feed.setDescription(c.getDesc());
				feed.setLanguage(c.getLangue());
				feed.setEntries(addEntry(items, urlEntry, path));
			} catch (final Exception ex) {
				LOG.error("Error : " + ex.getLocalizedMessage());
			}
			return feed;
		} catch (DataAccessException e) {
			LOG.error("Error while generating most recent items feed : " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @param id of the Entity
	 * @param feedType
	 * @param nbLastDays
	 * @param urlEntry
	 * @return SyndFeed
	 * @see org.uhp.portlets.news.service.FeedService#
	 * getMostRecentItemsFeedOfCategory(Long, String, Integer, String)
	 */
	public SyndFeed getMostRecentItemsFeedOfEntity(final Long id, final String feedType,
			final Integer nbLastDays, final String urlEntry) {
		/*try {
			final List<Category> cats = this.categoryDao.getAllCategoryOfEntity(id);
			//TODO ici
			return entityFeed;
		} catch (DataAccessException e) {
			LOG.error("Error while generating most recent items feed : " + e.getLocalizedMessage());
		}*/
		return null;
	}

	/**
	 * @param id
	 * @param feedType
	 * @param urlEntry
	 * @return SyndFeed
	 * @see org.uhp.portlets.news.service.FeedService
	 * #getTopicFeed(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public SyndFeed getTopicFeed(final Long  id,  final String feedType, final String urlEntry) {
		try {
			final Topic t = this.topicDao.getTopicById(id);
			final Category c = this.categoryDao.getCategoryById(t.getCategoryId());
			final List<Item> items = this.itemDao.getItemListByTopic(t.getTopicId());
			String path;
			final SyndFeed feed = new SyndFeedImpl();
			feed.setFeedType(feedType);
			feed.setTitle(t.getName());
			if (NewsConstants.S_N.equals(c.getPublicView())) {
				path = NewsConstants.PRIVATE_PATH;
			} else {
				path = (NewsConstants.S_Y.equals(t.getPublicView()))
				? NewsConstants.PUBLIC_PATH : NewsConstants.PRIVATE_PATH;
			}
			feed.setLink(urlEntry + path + "rss?t=" + Constants.EXPORT_TOPIC_FEED
					+ "&topicID=" + t.getTopicId() + "&feedType=" + feedType);
			feed.setDescription(t.getDesc());
			feed.setLanguage(t.getLangue());
			feed.setEntries(addEntry(items, urlEntry, path));
			return feed;
		} catch (DataAccessException e) {
			LOG.error("Error while generating topic RSS feed : " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * @param items
	 * @param urlEntry
	 * @param path
	 * @return List<SyndEntry>
	 */
	private List<SyndEntry> addEntry(final List<Item> items, final String urlEntry, final String path) {
		final List<SyndEntry> entries = new ArrayList<SyndEntry>();

		for (Item item : items) {
			SyndEntry entry = new SyndEntryImpl();
			entry.setTitle(item.getTitle());
			entry.setLink(urlEntry + getItemPath(item.getItemId(), path)
					+ "item?c=1&itemID=" + item.getItemId());
			entry.setAuthor(um.getUserNameByUid(item.getPostedBy()));
			entry.setPublishedDate(item.getPostDate());
			entry.setUpdatedDate(item.getLastUpdatedDate());
			SyndContent description = new SyndContentImpl();
			description.setType(DESC_CONTENT_TYPE);
			description.setValue(item.getSummary());
			entry.setDescription(description);
			entries.add(entry);
		}
		return entries;
	}

	/**
	 * @param refreshP
	 * @param refreshF
	 * @return Integer
	 */
	private Integer getTTL(final String refreshP, final Integer refreshF) {
		Integer ttl;
		//In second
		final Integer cst = 3600;
		final Integer cstDay = 86400;
		if ((refreshP == null) || (refreshP.trim().equals("")) || (refreshF == null) || (refreshF == 0)) {
			ttl = cst;
		} else if (refreshP.equalsIgnoreCase(PER_HOUR)) {
			ttl = cst / refreshF.intValue();
		} else if (refreshP.equalsIgnoreCase(PER_DAY)) {
			ttl = cstDay / refreshF.intValue();
		} else    {
			ttl = cst;
		}
		return ttl;
	}

	/**
	 * @param slist
	 * @return String
	 */
	private String getSub(final List<Subscriber> slist) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < slist.size(); i++) {
			if (slist.get(i).getIsGroup() == 1 && slist.get(i).getPrincipal().contains(groupStoreName)) {
				String value = StringEscapeUtils.escapeXml(slist.get(i).getPrincipal().replace(groupStoreName, ""));
				sb.append("<regex attribute=\"isMemberOf\" pattern=\"" + Pattern.quote(value) + "(:.*)?" + "\" />\n");
			} else if  (slist.get(i).getIsGroup() == 1) {
				sb.append("<group name=\"" + StringEscapeUtils.escapeXml(slist.get(i).getPrincipal()) + "\" />\n");
			} else {
				sb.append("<regular attribute=\"uid\" value=\""
						+ StringEscapeUtils.escapeXml(slist.get(i).getPrincipal()) + "\" />\n");
			}
		}
		return sb.toString();
	}



	/**
	 * @param ctxId
	 * @param ctx
	 * @return String
	 */
	private String getCtxVisibility(final Long ctxId, final String ctx) {
		final StringBuilder sbuf = new StringBuilder();
		if (this.subDao.hasSubscribers(ctxId, ctx)) {
			sbuf.append("<visibility>\n");
			sbuf.append("<allowed>\n");
			List<Subscriber> sbs =
				this.subDao.getSubscribers(ctxId, ctx, SubscribeType.SUB_FREE.toString());
			sbuf.append(getSub(sbs));
			sbuf.append("</allowed>\n");
			sbs.clear();
			sbuf.append("<autoSubscribed>\n");
			sbs = this.subDao.getSubscribers(ctxId, ctx, SubscribeType.SUB_PRE.toString());
			sbuf.append(getSub(sbs));
			sbuf.append("</autoSubscribed>\n");
			sbs.clear();
			sbuf.append("<obliged>\n");
			sbs = this.subDao.getSubscribers(ctxId, ctx, SubscribeType.SUB_FORCED.toString());
			sbuf.append(getSub(sbs));
			sbuf.append("</obliged>\n");
			sbuf.append("</visibility>");
		}
		return sbuf.toString();
	}

	/**
	 * @param id
	 * @return boolean
	 * @see org.uhp.portlets.news.service.FeedService#allowTopicPublicView(java.lang.Long)
	 */
	public boolean allowTopicPublicView(final Long id) {
		final Topic t = this.topicDao.getTopicById(id);
		return (t.getPublicView().equals(NewsConstants.S_Y)) ? true :  false;
	}

	/**
	 * @param id
	 * @return boolean
	 * @see org.uhp.portlets.news.service.FeedService#allowTopicRSS(java.lang.Long)
	 */
	public boolean allowTopicRSS(final Long id) {
		final Topic t = this.topicDao.getTopicById(id);
		return (t.getRssAllowed().equals(NewsConstants.S_Y)) ? true :  false;
	}

	/**
	 * @param id
	 * @param isProtected
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService#getFeedNotAvailableMsg(java.lang.Long, boolean)
	 */
	public String getFeedNotAvailableMsg(final Long id, final boolean isProtected) {
		String msg = "";
		final Category c = this.categoryDao.getCategoryById(id);
		if (c != null) {
			if (c.getPublicView().equals(NewsConstants.S_N) &&  !isProtected) {
				msg = ACC_BY_AUTH;
			}
			if (c.getRssAllowed().equals(NewsConstants.S_N)) {
				msg = RSS_NOT_ALLOWED + " category";
			}
		} else {
			msg = null;
		}
		return msg;
	}

	/**
	 * @param id
	 * @param isProtected
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService#getTopicFeedNotAvailableMsg(java.lang.Long, boolean)
	 */
	public String getTopicFeedNotAvailableMsg(final Long id, final boolean isProtected) {
		String msg = "";
		final Topic t = this.topicDao.getTopicById(id);
		if (t != null) {
			if (t.getRssAllowed().equals(NewsConstants.S_N)) {
				msg = RSS_NOT_ALLOWED + " topic";
			}
			if (t.getPublicView().equals(NewsConstants.S_N) &&  !isProtected) {
				msg = ACC_BY_AUTH;
			}
		} else {
			msg = null;
		}
		return msg;
	}


	/**
	 * @param id
	 * @param status
	 * @param uid
	 * @return ItemsView
	 * @see org.uhp.portlets.news.service.FeedService#getItems(java.lang.Long, int, java.lang.String)
	 */
	public ItemsView getItems(final Long id,  final int status, final String uid) {
		try {
			if (rm.isRoleExistForContext(id, NewsConstants.CTX_T, uid, false)) {
				final Topic t = topicDao.getTopicById(id);
				return getItems(categoryDao.getCategoryById(t.getCategoryId()), t, status);
			}
			LOG.warn("user " + uid + " has no permission to view the topic list with id=" + id);
		} catch (DataAccessException e) {
			LOG.error("DAE " + e.getMessage());
		}
		return null;
	}

	/**
	 * @param c
	 * @param t
	 * @param status
	 * @return ItemsView
	 */
	public ItemsView getItems(final Category c, final Topic t,  final int status) {
		ItemsView itemsS = new ItemsView();

		try {
			final Long id = t.getTopicId();
			switch (status) {
			case 0 :
				itemsS.setItems(itemDao.getPendingItemListByTopic(id));
				itemsS.setItemStatus(ItemStatus.PENDING);

				break;
			case 1 :
				itemsS.setItems(itemDao.getValidatedItemListByTopic(id));
				itemsS.setItemStatus(ItemStatus.PUBLISHED);
				break;
			case 2 :
				itemsS.setItems(itemDao.getExpiredItemListByTopic(id));
				itemsS.setItemStatus(ItemStatus.ARCHIVED);
				break;
			case 3 :
				itemsS.setItems(itemDao.getScheduledItemListByTopic(id));
				itemsS.setItemStatus(ItemStatus.SCHEDULED);
				break;
			default :
				itemsS.setItems(itemDao.getAllItemListByTopic(id));
			}
		} catch (DataAccessException e) {
			LOG.warn("DAE : " + e.getLocalizedMessage());
		}
		itemsS.setTopic(t);
		itemsS.setCatName(c.getName());
		return itemsS;
	}

	/**
	 * @param id
	 * @param isProtected
	 * @return ItemV
	 * @see org.uhp.portlets.news.service.FeedService#getItem(java.lang.Long, boolean)
	 */
	public ItemV getItem(final Long id, final boolean isProtected) {
		try {
			final Item item = itemDao.getItemById(id);
			Category c = categoryDao.getCategoryById(item.getCategoryId());

			ItemV iv = new ItemV();
			iv.setItem(item);
			if (NewsConstants.S_N.equals(c.getRssAllowed())
					|| (NewsConstants.S_N.equals(c.getPublicView()) &&  !isProtected)) {
				return null;
			}
			iv.setCatName(c.getName());

			List<Attachment> attachments = attachmentDao.getAttachmentsListByItem(id);
			iv.setAttachments(attachments);

			return iv;
		} catch (DataAccessException e) {
			LOG.error("DAE " + e.getMessage());
		} catch (NoSuchItemException nsie) {
			LOG.error("No Item found with id " + id);
		}
		return null;
	}

	/**
	 * @param id
	 * @param path
	 * @return String
	 */
	private String getItemPath(final Long id, final String path) {
		if (path.equals(NewsConstants.PUBLIC_PATH) && this.itemDao.hasProtectedTopics(id)) {
			return NewsConstants.PRIVATE_PATH;
		}
		return path;
	}


	/**
	 * Get the attachment file to download.
	 *
	 * @param fileUid
	 * @param itemId
	 * @param isProtected
	 * @return AttachmentD
	 */
	public AttachmentD getAttachmentToDownload(final String fileUid, final Long itemId, final boolean isProtected) {
		try {
			List<Attachment> attachments = attachmentDao.getAttachmentsListByItem(itemId);
			boolean found = false;
			Attachment attachement = null;
			for (Attachment att : attachments) {
				String cmisUid = att.getCmisUid();
				if (cmisUid.equalsIgnoreCase(fileUid)) {
					found = true;
					attachement = att;
					break;
				}
			}
			if (found) {
				// retrieve entity
				Item item = itemDao.getItemById(itemId);
				Category c = categoryDao.getCategoryById(item.getCategoryId());
				if (NewsConstants.S_N.equals(c.getRssAllowed())
						|| (NewsConstants.S_N.equals(c.getPublicView()) && !isProtected)) {
					return null;
				}
				Long entityId = c.getEntityId();

				AttachmentD toDownload = cmisDao.getAttachmentById(fileUid, entityId);
				toDownload.setFileName(attachement.getFileName());
				toDownload.setAttachmentId(attachement.getAttachmentId());
				return toDownload;
			}
			return null;
		} catch (DataAccessException e) {
			LOG.error("DAE " + e.getMessage(), e.fillInStackTrace());
		} catch (CmisException ce) {
			LOG.error(ce, ce.fillInStackTrace());
		}
		return null;
	}

	/**
	 * Returns the feed for all entities of a type.
	 * @param type
	 * @param fType
	 * @param feedUrl
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService#
	 * getFeedsOfType(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getFeedsOfType(final String type, final String fType, final String feedUrl) {
		StringBuilder  sbuf = new StringBuilder();
		try {
			sbuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			final Type atype = this.typeDao.getTypeByName(type);
			if (atype == null) {
				sbuf.append("<error>The 'type' value given in parameters is unknown from this context.</error>");
				return sbuf.toString();
			}
			final List<Entity> entities = this.entityDao.getEntitiesByType(atype.getTypeId());
			sbuf.append("<categoryProfilesUrl>\n");
			for (Entity entity : entities) {
				sbuf.append(this.getCategoryProfileOfEntityOfType(entity, atype, feedUrl, fType));
			}
			sbuf.append("</categoryProfilesUrl>");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Returned feed of type : " + sbuf);
			}
			return sbuf.toString();
		} catch (DataAccessException e) {
			LOG.error("Error while generating category profile : " + e.getLocalizedMessage());
			sbuf.append("<error>An error in database access occured.</error>");
			return sbuf.toString();
		}
	}


	/**
	 * @param id
	 * @param fType
	 * @param feedUrl
	 * @param type
	 * @return String
	 * @see org.uhp.portlets.news.service.FeedService
	 * #getEntityTypeFeed(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getEntityTypeFeed(final Long id, final String fType, final String feedUrl, final String type) {
		StringBuilder  sbuf = new StringBuilder();

		try {
			sbuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			final Type atype = this.typeDao.getTypeByName(type);
			if (atype == null) {
				sbuf.append("<error>The 'type' value given in parameters is unknown from this context.</error>");
				return sbuf.toString();
			}
			sbuf.append("<categoryProfilesUrl>\n");
			sbuf.append(this.getCategoryProfileOfEntityOfType(this.entityDao.getEntityById(id), atype, feedUrl, fType));
			sbuf.append("</categoryProfilesUrl>");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Returned feed of type on entity " + id + " : \n" + sbuf);
			}
			return sbuf.toString();
		} catch (DataAccessException e) {
			LOG.error("Error while generating category profile : " + e.getLocalizedMessage());

		}
		return null;
	}

	/**
	 * @param entity
	 * @param type
	 * @param feedUrl
	 * @param fType
	 * @return String
	 */
	private String getCategoryProfileOfEntityOfType(final Entity entity, final Type type, final String feedUrl,
			final String fType) {
		StringBuilder  sbuf = new StringBuilder();
		final List<Category> categories =
			this.categoryDao.getCategoryByTypeOfEntityInDisplayOrder(type.getTypeId(), entity.getEntityId());
		for (Category cat : categories) {
			String access;
			String path;
			if (NewsConstants.S_Y.equals(cat.getPublicView())) {
				access = PUBLIC_ACCESS;
				path = NewsConstants.PUBLIC_PATH;
			} else {
				access = PRIVATE_ACCESS;
				path = NewsConstants.PRIVATE_PATH;
			}
			Integer ttl = getTTL(cat.getRefreshPeriod(), cat.getRefreshFrequency());
			sbuf.append("<categoryProfile name=\"" + StringEscapeUtils.escapeXml(entity.getName()) + " - " + StringEscapeUtils.escapeXml(cat.getName())
					+ "\" id=\"" + entity.getEntityId() + cat.getCategoryId() + "\""
					+ " urlCategory=\"" + feedUrl + path
					+ "rss?t=" + Constants.EXPORT_CAT_FEED
					+ "&amp;cID=" + cat.getCategoryId()
					+ "\" trustCategory=\"yes\" access=\"" + access + "\" ttl=\"" + ttl + "\" timeout=\"" + this.timeout + "\" >\n");
			//sbuf.append(this.getCtxVisibility(entity.getCategoryId(), NewsConstants.CTX_C));
			sbuf.append("</categoryProfile>\n");
		}
		return sbuf.toString();
	}



	/**
	 * Obtains user's details from a list of user's id.
	 * @param usersUid a list of id/uid to retrieve in the LDAP.
	 * @return <code>Map< String, LdapUser ></code> A Map of LdapUser (details of users) with id/uid as key.
	 * @see org.esco.portlets.news.services.UserManager#getUsersByListUid(java.util.List)
	 */
	public Map<String, IEscoUser> getUsersByListUid(final List<String> usersUid) {
		return um.getUsersByListUid(usersUid);
	}

	/**
	 * Getter of attribute timeout
	 * @return <code>Integer</code> the attribute timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * Setter of attribute timeout
	 * @param timeout <code>Integer</code> the attribute timeout to set
	 */
	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}

	/**
	 * Getter of member groupStoreName.
	 * @return <code>String</code> the attribute groupStoreName
	 */
	public String getGroupStoreName() {
		return groupStoreName;
	}

	/**
	 * Setter of attribute groupStoreName.
	 * @param groupStoreName the attribute groupStoreName to set
	 */
	public void setGroupStoreName(final String groupStoreName) {
		this.groupStoreName = groupStoreName;
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		final String notNull = " must not be null.";
		Assert.notNull(this.typeDao, "The property TypeDAO typeDao in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.categoryDao, "The property CategoryDao categoryDao in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.um, "The property UserManager um in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.entityDao, "The property EntityDAO entityDao in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.topicDao, "The property TopicDao topicDao in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.itemDao, "The property ItemDao itemDao in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.subDao, "The property SubscriberDao subDao in class "
				+ getClass().getSimpleName() + notNull);
		if (this.timeout == null || this.timeout < 0) {
			LOG.warn("The timeout property isn't set or used, so default timeout " + DEFAULT_TIMEOUT + " will be used.");
			this.timeout = DEFAULT_TIMEOUT;
		}
		Assert.notNull(this.groupStoreName, "The property groupStoreName in class "
				+ getClass().getSimpleName() + notNull);
		Assert.notNull(this.rm, "The property RoleManager rm in class "
				+ getClass().getSimpleName() + notNull);
	}
}
