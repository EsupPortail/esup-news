/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.cmis.portlets.news.dao.opencmis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.CmisAttachmentDao;
import org.cmis.portlets.news.dao.CmisPathFinderHelper;
import org.cmis.portlets.news.domain.Attachment;
import org.cmis.portlets.news.domain.AttachmentD;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * created by Anyware Services - Delphine Gavalda.
 *
 * 19 mai 2010
 */
@Repository("cmisAttachmentDao")
public class OpenCmisClientAttachmentDaoImpl implements CmisAttachmentDao {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(OpenCmisClientAttachmentDaoImpl.class);

	/** SessionFactory. */
	@Autowired
	private BasicSessionFactory sessionFactory;
	/** CmisPathFinderHelper. */
	@Autowired
	private CmisPathFinderHelper pathHelper;

	/** Constructeur.  */
	public OpenCmisClientAttachmentDaoImpl() {
		super();
	}

	/**
	 * @see org.cmis.portlets.news.dao.CmisAttachmentDao#deleteAttachment(java.lang.String, java.lang.Long)
	 */
	public void deleteAttachment(final String attachmentId, final Long entityId) throws CmisException {

		Session session = sessionFactory.getSession(entityId);

		ObjectId id = new ObjectIdImpl(attachmentId);
		Document object = (Document) session.getObject(id);

		object.delete(true);
	}

	/**
	 * @see org.cmis.portlets.news.dao.CmisAttachmentDao#getAttachmentById(java.lang.String, java.lang.Long)
	 */
	public AttachmentD getAttachmentById(final String id, final Long entityId) throws CmisException {

		Session session = sessionFactory.getSession(entityId);

		ObjectId oid = new ObjectIdImpl(id);

		Document doc = (Document) session.getObject(oid);

		AttachmentD ad = new AttachmentD();
		ad.setCmisUid(id);
		ad.setCmisDocument(doc);

		return ad;
	}

	/**
	 * @see org.cmis.portlets.news.dao.CmisAttachmentDao#insertAttachment(org.uhp.portlets.news.web.ItemForm.Attachment, java.lang.Long, java.util.Map)
	 */
	public Attachment insertAttachment(final org.uhp.portlets.news.web.ItemForm.Attachment attachment,
			final Long entityId, final Map<String, Object> prop) throws CmisException {
		Session session;
		try {
			session = sessionFactory.getSession(entityId);
		} catch (CmisException e) {
			// Delete the temporary file
			File file = attachment.getTempDiskStoredFile();
			file.delete();

			throw new CmisException(e.getMessage(), e.fillInStackTrace());
		}

		Date insertDate = attachment.getInsertDate();

		// Find a valid path and filename:
		String validPath = pathHelper.getPathForAttachment(session, prop);
		if(validPath == null)
		{
			// if the path is null, an error occurs, delete the temporary file
			File file = attachment.getTempDiskStoredFile();
			file.delete();

		} else {
			// Save the file
			String folderPath = validPath.substring(0, validPath.lastIndexOf("/"));
			String filename = validPath.substring(validPath.lastIndexOf("/") + 1, validPath.length());

			CmisObject folder = session.getObjectByPath(folderPath);

			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
			properties.put(PropertyIds.NAME, filename);
			properties.put(PropertyIds.CREATION_DATE, insertDate);

			File file = attachment.getTempDiskStoredFile();

			long size = file.length();

			ContentStream stream = null;
			ObjectId oid = null;
			FileInputStream fis = null;
			try {
				// Store the file
				fis = new FileInputStream(file);
				stream = new ContentStreamImpl(filename, BigInteger.valueOf(file.length()), attachment.getMimeType(), fis);
				oid = session.createDocument(properties, new ObjectIdImpl(folder.getId()), stream, VersioningState.NONE,
						null, null, null);

			} catch (Exception e) {
				LOG.error(e, e.fillInStackTrace());
				throw new CmisException("Impossible de lire le contenu du fichier.", e.fillInStackTrace());

			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					LOG.error(e, e.fillInStackTrace());
					throw new CmisException(e.getMessage(), e.fillInStackTrace());
				}
				// Delete the temporary file
				file.delete();
			}

			Attachment sqlAtt = null;
			if (oid != null) {
				// build the sqlMap object
				sqlAtt = new Attachment();
				sqlAtt.setCmisUid(oid.getId());
				sqlAtt.setTitle(attachment.getTitle());
				sqlAtt.setDescription(attachment.getDesc());
				sqlAtt.setFileName(filename);
				sqlAtt.setPath(folderPath);
				sqlAtt.setInsertDate(insertDate);
				sqlAtt.setSize(size);
			}
			return sqlAtt;
		}
		return null;
	}

}
