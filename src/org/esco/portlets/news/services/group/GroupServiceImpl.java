/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalService;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.esupportail.portal.ws.client.exceptions.PortalGroupNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * General Services for groups.
 * @author GIP RECIA - Gribonvald Julien
 * 31 mai 2010
 */
@Service("groupService")
public class GroupServiceImpl implements InitializingBean, GroupService {


	/** */
	private static final Log LOG = LogFactory.getLog(GroupServiceImpl.class);

	/** */
	private PortalService portalService;

	/**
	 * Constructor of AGroupServiceImpl.java.
	 */
	public GroupServiceImpl() {
		super();
	}

	/**
	 * Looking for groups.
	 * @param token
	 * @return List<PortalGroup>
	 * @throws PortalErrorException
	 */
	public List<PortalGroup> searchPortalGroups(final String token) throws PortalErrorException {
		List<PortalGroup> grps;

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Searching groups with token : " + token);
			}
			grps = portalService.searchGroupsByName(token);
		} catch (PortalErrorException e) {
			LOG.error(e.getLocalizedMessage());
			throw e;
		} catch (PortalGroupNotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return null;
		}
		return grps;
	}

	/**
     * Looking for groups, where the filter will be used to make de search on uPortal Groups and the token will be used to filter on returned groups that match the token
     * @param filter
     * @param token
     * @return List<PortalGroup>
     * @throws PortalErrorException
     */
    public List<PortalGroup> searchPortalGroups(final String filter, final String token) throws PortalErrorException {
        List<PortalGroup> grps = new ArrayList<PortalGroup>();

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Searching groups with filter : " + filter + " and token : " + token);
            }
            List<PortalGroup> tmp_grps = portalService.searchGroupsByName(filter);
            for (PortalGroup pg : tmp_grps){
            	if (pg.getName().toLowerCase().matches(".*" + token.toLowerCase().replaceAll("\\*+", ".*") + ".*")){
            		grps.add(pg);
            	}
            }
        } catch (PortalErrorException e) {
            LOG.error(e.getLocalizedMessage());
            throw e;
        } catch (PortalGroupNotFoundException e) {
            LOG.warn(e.getLocalizedMessage());
            return null;
        }
        return grps;
    }

    /**
	 * Looking for a group.
	 * @param id
	 * @return PortalGroup
	 * @throws PortalErrorException
	 */
	public PortalGroup getPortalGroupById(final String id) throws PortalErrorException {
		try {
			return portalService.getGroupById(id);
		} catch (PortalErrorException e) {
			LOG.error("GroupService::getPortalGroupById::principal=" + id + " PortalErrorException " + e.getMessage());
			throw e;
		} catch (PortalGroupNotFoundException e) {
			LOG.warn("GroupService::getPortalGroupById:: Group " + id + " not found" + e.getMessage());
			return null;
		}
	}

	/**
	 * @see org.esco.portlets.news.services.group.GroupService#isMemberOf(java.lang.String, java.lang.String)
	 */
	public boolean isMemberOf(final String userId, final String groupId) throws PortalErrorException {
		try {
			return portalService.isUserMemberOfGroup(portalService.getUser(userId), portalService.getGroupById(groupId));
		} catch (PortalErrorException e) {
			LOG.error(e.getLocalizedMessage());
			throw e;
		} catch (PortalGroupNotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return false;
		} catch (PortalUserNotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * @see org.esco.portlets.news.services.group.GroupService#isContainingGroup(java.lang.String, java.lang.String)
	 */
	public boolean isContainingGroup(final String groupId,final String groupMemberId) throws PortalErrorException {
		try {
			if (groupMemberId.startsWith(groupId)){
				return true;
			}
			List<PortalGroup> grps = portalService.getSubGroupsById(groupId);
			Iterator<PortalGroup> it = grps.iterator();
			boolean found = false;
			while (!found && it.hasNext()) {
				PortalGroup pg = it.next();
				if (pg.getId().equals(groupMemberId) || groupMemberId.startsWith(pg.getId())){
					found = true;
				}
			}
			return found;
		} catch (PortalErrorException e) {
			LOG.error(e.getLocalizedMessage());
			throw e;
		} catch (PortalGroupNotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return false;
		} catch (PortalUserNotFoundException e) {
			LOG.warn(e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Setter of portalService.
	 * @param portalService
	 */
	public void setPortalService(final PortalService portalService) {
		this.portalService = portalService;
	}

	/**
	 * Getter of portalService.
	 * @return <code>PortalService</code> the member portalService.
	 */
	public PortalService getPortalService() {
		return portalService;
	}

	/**
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getPortalService(), "The property subscriberDao in class "
				+ this.getClass().getSimpleName() + " must not be null.");
	}

}
