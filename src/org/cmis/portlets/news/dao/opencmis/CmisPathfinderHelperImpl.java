/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.cmis.portlets.news.dao.opencmis;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.CmisPathFinderHelper;
import org.cmis.portlets.news.utils.StringUtils;
import org.springframework.stereotype.Service;

/**
 * created by Anyware Services - Delphine Gavalda.
 *
 * 20 mai 2010
 */
@Service("cmisPathfinderHelper")
public class CmisPathfinderHelperImpl implements CmisPathFinderHelper {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(CmisPathfinderHelperImpl.class);

	/** Constructeur.  */
	public CmisPathfinderHelperImpl() {
		super();
	}

	/**
	 * @see org.cmis.portlets.news.dao.CmisPathFinderHelper#getPathForAttachment(org.apache.chemistry.opencmis.client.api.Session, java.util.Map)
	 */
	public String getPathForAttachment(final Session openCmisSession, final Map<String, Object> properties) {

		String fileName = (String) properties.get(FILE_NAME);
		Date insertDate = (Date) properties.get(INSERT_DATE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(insertDate);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		try {
			Folder entityFolder = null;
			Folder monthFolder = null;
			Folder yearFolder = null;

			// Checks if the entity folder exists, create it if not
			// entity valid filename
			String entityId = ((Long) properties.get(CmisPathFinderHelper.ENTITY_ID)).toString();
			// use entityId as validName
			String entityValidName = entityId;
			String entityPath = "/" + entityValidName;

			try {
				entityFolder = (Folder) openCmisSession.getObjectByPath(entityPath);
			} catch (CmisObjectNotFoundException nfe0) {
				// nothing
			}
			if (entityFolder == null) {
				entityFolder = createFolder(openCmisSession, openCmisSession.getRootFolder(), "/", entityValidName);
			}

			// Checks if the year folder exists, create it if not
			String yearPath = entityPath + "/" + String.valueOf(year);
			try {
				yearFolder = (Folder) openCmisSession.getObjectByPath(yearPath);
			} catch (CmisObjectNotFoundException nfe0) {
				// nothing
			}
			if (yearFolder == null) {
				// create folder for the year if don't exits
				yearFolder = createFolder(openCmisSession, entityFolder, entityPath + "/", String
						.valueOf(year));

				// then create the month folder
				monthFolder = createFolder(openCmisSession, yearFolder, yearPath, String.valueOf(month));

				// return directly the file path
				String validName = StringUtils.getValidFileName(fileName);
				return yearPath + "/" + String.valueOf(month) + "/" + validName;

			}
			// Else search for the month folder
			String monthPath = yearPath + "/" + String.valueOf(month);
			try {
				monthFolder = (Folder) openCmisSession.getObjectByPath(monthPath);
			} catch (CmisObjectNotFoundException nfe) {
				// nothing
			}
			if (monthFolder == null) {
				// create folder for the month if don't exits
				monthFolder = createFolder(openCmisSession, yearFolder, yearPath, String.valueOf(month));

				// return directly the file path
				String validName = StringUtils.getValidFileName(fileName);
				return monthPath + "/" + validName;
			}
			// Else Search for unexisting filename
			String validName = StringUtils.getValidFileName(fileName);
			Document file = null;
			try {
				file = (Document) openCmisSession.getObjectByPath(monthPath + "/" + validName);
			} catch (CmisObjectNotFoundException nfe2) {
				// nothing
			}
			if (file == null) {
				return monthPath + "/" + validName;
			}
			// Else
			String name = findFileName(openCmisSession, monthPath, validName);
			return monthPath + "/" + name;

		} catch (Exception e) {
			LOG.error("An error occurs trying to get a path for a new file", e);
		}

		return null;
	}

	/**
	 * @param cmisSession
	 * @param path
	 * @param name
	 * @return <code> String </code>
	 */
	private String findFileName(final Session cmisSession, final String path, final String name) {
		String start;
		String end;
		if (name.indexOf(".") > 0) {
			start = name.substring(0, name.lastIndexOf("."));
			end = name.substring(name.lastIndexOf("."), name.length());
		} else {
			start = name;
			end = "";
		}

		boolean exists = true;
		int cpt = 2;
		String testName = start + "_" + cpt + end;
		while (exists) {
			Document file = null;
			try {
				file = (Document) cmisSession.getObjectByPath(path + "/" + testName);
			} catch (CmisObjectNotFoundException nfe2) {
				// nothing
			}
			if (file == null) {
				exists = false;
			} else {
				cpt += 1;
				testName = start + "_" + cpt + end;
			}
		}

		return testName;
	}

	/**
	 * @param cmisSession
	 * @param folder
	 * @param path
	 * @param name
	 * @return <code> Folder </code>
	 */
	private Folder createFolder(final Session cmisSession, final Folder folder, final String path, final String name) {
		Map<String, String> newFolderProps = new HashMap<String, String>();
		newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.value());
		newFolderProps.put(PropertyIds.NAME, name);

		Folder newFolder = null;
		try {
			newFolder = folder.createFolder(newFolderProps, null, null, null, cmisSession.getDefaultContext());
		} catch (Exception e) {
			LOG.error("Unable to create a new forlder.", e);
		}
		return newFolder;
	}
}
