package org.cmis.portlets.news.dao.opencmis;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmis.portlets.news.dao.CmisSessionFactory;
import org.cmis.portlets.news.domain.CmisServer;
import org.cmis.portlets.news.services.AttachmentManager;
import org.cmis.portlets.news.services.exceptions.CmisException;
import org.cmis.portlets.news.utils.SimpleXOREncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 19 mai 2010
 */
@Service("sessionFactory")
public class BasicSessionFactory implements CmisSessionFactory {

    private static final Log LOG = LogFactory.getLog(BasicSessionFactory.class);

    @Autowired
    private AttachmentManager am;

    private Map<Long, Session> sessionsMap;

    public Session getSession(final Long entityId) throws CmisException {

	if (sessionsMap == null)
	{
	    sessionsMap = new HashMap<Long, Session>();
	}
	if (sessionsMap.get(entityId) == null)
	{
	    return createSession(entityId);
	}

	return sessionsMap.get(entityId);
    }

    private Session createSession(final Long entityId) throws CmisException {

	CmisServer server = am.getEntityServer(entityId);
	boolean useDefault = false;
	if (server == null)
	{
	    server = am.getApplicationServer();
	    useDefault = true;
	}
	if (server == null)
	{
	    LOG.error("BasicSessionFactory : No CMIS server has been defined.");
	    throw new CmisException("Aucun serveur CMIS n'a été défini.");
	}

	if (useDefault && sessionsMap.get((long) -1) != null)
	{
	    return sessionsMap.get((long) -1);
	}

	// default factory implementation of client runtime
	SessionFactory f = SessionFactoryImpl.newInstance();
	Map<String, String> parameter = new HashMap<String, String>();

	// user credentials
	String serverLogin = server.getServerLogin();
	parameter.put(SessionParameter.USER, serverLogin);

	// decrypt pwd
	String serverPwd = server.getServerPwd();
	String decrypted = SimpleXOREncryption.encryptDecrypt(serverPwd);
	String pwd = decrypted.substring(serverLogin.length(), decrypted.length());
	parameter.put(SessionParameter.PASSWORD, pwd);

	// connection settings
	String serverUrl = server.getServerUrl();
	parameter.put(SessionParameter.ATOMPUB_URL, serverUrl);
	parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
	parameter.put(SessionParameter.REPOSITORY_ID, server.getRepositoryId());

	// session locale
	parameter.put(SessionParameter.LOCALE_ISO3166_COUNTRY, "");
	parameter.put(SessionParameter.LOCALE_ISO639_LANGUAGE, "fr");
	parameter.put(SessionParameter.LOCALE_VARIANT, "");

	// create session
	try
	{
	    Session session = (f).createSession(parameter);
	    if (session == null)
	    {
		LOG.error("BasicSessionFactory : unable to connect to the cmis server at : " + serverUrl);
		throw new CmisException("Impossible de se connecter au serveur avec l'URL suivante : " + serverUrl);
	    }

	    if (useDefault)
	    {
		sessionsMap.put((long) -1, session);
		return sessionsMap.get((long) -1);

	    } else
	    {
		sessionsMap.put(entityId, session);
		return sessionsMap.get(entityId);
	    }

	} catch (Exception e)
	{
	    LOG.error("BasicSessionFactory : An error occurs trying to connect to the cmis server at : " + serverUrl);
	    LOG.error(e, e.fillInStackTrace());
	    throw new CmisException("Une erreur est survenue durant la connection au serveur avec l'URL suivante : " + serverUrl, e.fillInStackTrace());
	}
    }

    public void removeSession(final Long entityId) throws CmisException {
	if (sessionsMap != null)
	{
	    if (sessionsMap.get(entityId) != null)
	    {
		sessionsMap.remove(entityId);
	    }
	}
    }

    public void removeDefaultSession() throws CmisException {
	if (sessionsMap != null)
	{
	    if (sessionsMap.get((long) -1) != null)
	    {
		sessionsMap.remove((long) -1);
	    }
	}
    }
}
