/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services.group;

import java.util.List;

import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;

/**
 * Interface for Group Services.
 * @author GIP RECIA - Gribonvald Julien
 * 31 mai 2010
 */
public interface GroupService {

    /**
     * Looking for groups.
     * @param token
     * @return List<PortalGroup>
     * @throws PortalErrorException
     */
    List<PortalGroup> searchPortalGroups(final String token) throws PortalErrorException;

    /**
     * Looking for groups, where the filter will be used to make de search on uPortal Groups and the token will be used to filter on returned groups that match the token
     * @param filter
     * @param token
     * @return List<PortalGroup>
     * @throws PortalErrorException
     */
    List<PortalGroup> searchPortalGroups(final String filter, final String token) throws PortalErrorException;

    /**
     * Looking for a group.
     * @param id
     * @return PortalGroup
     * @throws PortalErrorException
     */
    PortalGroup getPortalGroupById(final String id) throws PortalErrorException;

    /**
     * Tell if a user is a member of a group.
     * @param userId
     * @param groupId
     * @return boolean
     * @throws PortalErrorException
     */
    boolean isMemberOf(final String userId, final String groupId) throws PortalErrorException;

    /**
     * Tell if a group is containing the groupMember.
     * @param groupId
     * @param groupMemberId
     * @return boolean
     * @throws PortalErrorException
     */
    boolean isContainingGroup(final String groupId, final String groupMemberId) throws PortalErrorException;

}