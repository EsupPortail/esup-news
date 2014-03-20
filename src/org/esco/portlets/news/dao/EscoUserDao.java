/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.dao;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.domain.IEscoUser;
import org.esupportail.commons.services.ldap.LdapException;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.RolePerm;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;

/**
 * Modifications of the interface.
 * @author GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public interface EscoUserDao {

    /**
     * @param user
     * @throws DataAccessException
     */
    void insert(final User user) throws DataAccessException;

    /**
     * @param user
     * @throws DataAccessException
     */
    void update(final User user) throws DataAccessException;

    /**
     * @param userId
     * @throws DataAccessException
     */
    void deleteUser(final String userId) throws DataAccessException;

    /**
     * @param userId
     * @return <code>IEscoUser</code>
     * @throws DataAccessException
     */
    IEscoUser getUserById(final String userId) throws DataAccessException;

    /**
     * @param userId
     * @return <code>String</code>
     * @throws LdapException
     */
    String getUserNameById(final String userId) throws LdapException;
    /**
     * @param userId
     * @return <code>String</code>
     * @throws DataAccessException
     */
    boolean isUserInDB(final String userId) throws DataAccessException;

    /**
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getAllUsers() throws DataAccessException, LdapException;

    /**
     * @param userId
     * @param enabled
     * @throws DataAccessException
     */
    void updateUserStatus(final String userId, final String enabled) throws DataAccessException;

    /**
     * @param userId
     * @throws DataAccessException
     */
    void updateLastUserAccess(final String userId) throws DataAccessException;

    /**
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getAllSuperUsers() throws DataAccessException, LdapException;

    /**
     * Tell if a user is a super Admin from his userid.
     * @param userId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isUserSuperAdmin(final String userId) throws DataAccessException;

    /**
     * Tell if a user has roles.
     * @param userId
     * @return <code>boolean</code>
     */
    boolean isUserHasAnyRole(final String userId);

    /**
     * @param token Search value.
     * @return <code>List< IEscoUser ></code> A list of users corresponding to the search value.
     * @throws LdapException
     */
    List<IEscoUser> findPersonsByToken(final String token) throws LdapException;

    /**
     * @param token Search value.
     * @param filter
     * @return <code>List< IEscoUser ></code> A list of users corresponding to the search value.
     * @throws LdapException
     */
    List<IEscoUser> findPersonsByTokenAndFilter(final String token,
            final org.springframework.ldap.support.filter.Filter filter) throws LdapException;

    /**
     * Get user's details from a list of user's id.
     * @param usersUid a list of id/userId to retrieve in the LDAP.
     * @return <code>Map< String, IEscoUser ></code> A Map of IEscoUser (details of users) with id/userId as key.
     * @throws LdapException
     */
    Map<String, IEscoUser> findPersonsByListUid(final List<String> usersUid) throws LdapException;

    /**
     * Name of the LDAP attribute mapped with the displayName attribute of the ESCOUser object.
     * @return <code>String</code>
     */
    String getDisplayName();

    /**
     * Name of the LDAP attribute mapped with the mail attribute of the ESCOUser object.
     * @return <code>String</code>
     */
    String getMail();


    /**
     * @param enabled
     * @param userId
     * @throws DataAccessException
     */
//    void activateUser(int enabled, final String userId) throws DataAccessException;

    /**
     * @param userId
     * @param rar
     * @throws DataAccessException
     */
//    void deleteUser(final String userId, final boolean rar) throws DataAccessException;





    /**
     * @param userRole
     * @throws DataAccessException
     */
//    void addEntityRole(final EntityRole userRole) throws DataAccessException;

    /**
     * @param user
     * @param role
     * @param ctx
     * @param ctxId
     * @throws DataAccessException
     */
//    void addEntityRole(final User user, final String role, final String ctx, final Long ctxId) throws DataAccessException;

    /**
     * @param userId
     * @param role
     * @param ctx
     * @param ctxId
     * @param isGroup
     * @throws DataAccessException
     */
//    void addEntityRole(final String userId, final String role, final String ctx, final Long ctxId, final String isGroup) throws DataAccessException;

    /**
     * Update the role of the user in the context.
     * @param userId
     * @param role
     * @param ctx
     * @param ctxId
     * @throws DataAccessException
     */
//    void updateEntityRoleForCtx(final String userId, final String role, final String ctx, final Long ctxId) throws DataAccessException;

    /**
     * @param target
     * @param targetCtx
     * @param role
     * @return <code>List< EntityRole ></code>
     * @throws DataAccessException
     */
//    List<EntityRole> getUsersByRole(final Long target, final String targetCtx, final RoleEnum role) throws DataAccessException;

    /**
     * @param userId
     * @param target
     * @param targetCtx
     * @throws DataAccessException
     */
//    void removeEntityRoleForCtx(final String userId, final Long target, final String targetCtx) throws DataAccessException;

    /**
     * @param userId
     * @param cId
     * @throws DataAccessException
     */
//    void removeAllEntityRolesForTopics(final String userId, final Long cId) throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @return <code>List< EntityRole ></code>
     * @throws DataAccessException
     */
//    List<EntityRole> loadCtxUsersRolesMap(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param userId
     * @param ctxId
     * @param ctxType
     * @return <code>String</code>
     * @throws DataAccessException
     */
//    String getEntityRoleForCtx(final String userId, final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     *  WARNING doesn't works with groups.
     * @param categoryId
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    @Deprecated
    List<IEscoUser> getManagersForCategory(final Long categoryId) throws DataAccessException, LdapException;

    /**
     * @param topicId
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
//    List<IEscoUser> getManagersForTopic(final Long topicId) throws DataAccessException, LdapException;

    /**
     * @param topicIds
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
//    List<IEscoUser> getManagersForTopics(final Integer[] topicIds) throws DataAccessException, LdapException;

    /**
     * WARNING doesn't works with groups.
     * @param cId
     * @param topicIds
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    @Deprecated
    List<IEscoUser> getManagersForTopics(final Long cId, final Integer[] topicIds) throws DataAccessException, LdapException;

    /**
     * WARNING doesn't works with groups.
     * @param topic
     * @param user
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    @Deprecated
    boolean isTopicManager(final Topic topic, final User user) throws DataAccessException;
    /**
     * @param ctxId
     * @param ctxType
     * @param userId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
//    boolean isEntityRoleExistForContext(final Long ctxId, final String ctxType, final String userId) throws DataAccessException;

    /**
     * @return <code>List< Role ></code>
     * @throws DataAccessException
     */
    //List<Role> getAllRoles() throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @throws DataAccessException
     */
//    void removeUsersRoleForCtx(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @return <code>List< EntityRole ></code>
     * @throws DataAccessException
     */
//    List<EntityRole> getEntityRolesForCtx(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param userId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
//    boolean isPermitted(final String userId) throws DataAccessException;

    /**
     * @param userId
     * @param categoryId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
//    boolean userRolesExistInTopicsOfcategory(final String userId, final Long categoryId) throws DataAccessException;

    /**
     * @param role
     * @param itemId
     * @return <code>List< String ></code>
     * @throws DataAccessException
     */
    //boolean isUserHasRoleInTopicsByItem(final Long itemId, final RolePerm role) throws DataAccessException;

    /**
     * @param userId
     * @param entityId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
//    boolean userRolesExistInCategoriesOfEntity(final String userId, final Long entityId) throws DataAccessException;

    /**
     * @param userId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
//    boolean userRoleExist(final String userId) throws DataAccessException;



}
