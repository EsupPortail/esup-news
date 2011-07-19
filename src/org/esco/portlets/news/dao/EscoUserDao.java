/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.dao;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.domain.IEscoUser;
import org.esupportail.commons.services.ldap.LdapException;
import org.springframework.dao.DataAccessException;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.RoleEnum;
import org.uhp.portlets.news.domain.Topic;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;

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
     * @param enabled
     * @param uid
     * @throws DataAccessException
     */
    void activateUser(int enabled, final String uid) throws DataAccessException;

    /**
     * @param uid
     * @param rar
     * @throws DataAccessException
     */
    void deleteUser(final String uid, final boolean rar) throws DataAccessException;

    /**
     * @param user
     * @throws DataAccessException
     */
    void deleteUser(final User user) throws DataAccessException;

    /**
     * @param uid
     * @return <code>IEscoUser</code>
     * @throws DataAccessException
     */
    IEscoUser getUserById(final String uid) throws DataAccessException;

    /**
     * @param uid
     * @return <code>String</code>
     * @throws DataAccessException
     */
    String getUserNameById(final String uid) throws DataAccessException;

    /**
     * @param uid
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isSuperAdmin(final String uid) throws DataAccessException;

    /**
     * @param uid
     * @return <code>String</code>
     * @throws DataAccessException
     */
    boolean isUserInDB(final String uid) throws DataAccessException;

    /**
     * @param uid
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isUserAccountEnabled(final String uid) throws DataAccessException;

    /**
     * @param userRole
     * @throws DataAccessException
     */
    void addUserRole(final UserRole userRole) throws DataAccessException;

    /**
     * @param user
     * @param role
     * @param ctx
     * @param ctxId
     * @throws DataAccessException
     */
    void addUserRole(final User user, final String role, final String ctx, final Long ctxId) throws DataAccessException;

    /**
     * @param uid
     * @param role
     * @param ctx
     * @param ctxId
     * @param isGroup
     * @throws DataAccessException
     */
    void addUserRole(final String uid, final String role, final String ctx,
            final Long ctxId, final String isGroup) throws DataAccessException;

    /**
     * Update the role of the user in the context.
     * @param uid
     * @param role
     * @param ctx
     * @param ctxId
     * @throws DataAccessException
     */
    void updateUserRoleForCtx(final String uid, final String role, final String ctx, final Long ctxId)
    throws DataAccessException;

    /**
     * @param target
     * @param targetCtx
     * @param role
     * @return <code>List< UserRole ></code>
     * @throws DataAccessException
     */
    List<UserRole> getUsersByRole(final Long target, final String targetCtx,
            final RoleEnum role) throws DataAccessException;

    /**
     * @param uid
     * @param target
     * @param targetCtx
     * @throws DataAccessException
     */
    void removeUserRoleForCtx(final String uid, final Long target, final String targetCtx) throws DataAccessException;

    /**
     * @param uid
     * @param cId
     * @throws DataAccessException
     */
    void removeAllUserRolesForTopics(final String uid, final Long cId) throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @return <code>List< UserRole ></code>
     * @throws DataAccessException
     */
    List<UserRole> loadCtxUsersRolesMap(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param uid
     * @param ctxId
     * @param ctxType
     * @return <code>String</code>
     * @throws DataAccessException
     */
    String getUserRoleForCtx(final String uid, final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param categoryId
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getManagersForCategory(final Long categoryId) throws DataAccessException, LdapException;

    /**
     * @param topicId
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getManagersForTopic(final Long topicId) throws DataAccessException, LdapException;

    /**
     * @param topicIds
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getManagersForTopics(final Integer[] topicIds) throws DataAccessException, LdapException;

    /**
     * @param cId
     * @param topicIds
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getManagersForTopics(final Long cId,
            final Integer[] topicIds) throws DataAccessException, LdapException;

    /**
     * @param topic
     * @param user
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isTopicManager(final Topic topic, final User user) throws DataAccessException;
    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isUserRoleExistForContext(final Long ctxId, final String ctxType, final String uid)
        throws DataAccessException;

    /**
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getAllUsers() throws DataAccessException, LdapException;

    /**
     * @return <code>List< Role ></code>
     * @throws DataAccessException
     */
    List<Role> getAllRoles() throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @throws DataAccessException
     */
    void removeUsersRoleForCtx(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param uid
     * @param enabled
     * @throws DataAccessException
     */
    void updateUserStatus(final String uid, final String enabled) throws DataAccessException;

    /**
     * @param uid
     * @throws DataAccessException
     */
    void updateUserLastAccess(final String uid) throws DataAccessException;

    /**
     * @param ctxId
     * @param ctxType
     * @return <code>List< UserRole ></code>
     * @throws DataAccessException
     */
    List<UserRole> getUsersRolesForCtx(final Long ctxId, final String ctxType) throws DataAccessException;

    /**
     * @param uid
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean isPermitted(final String uid) throws DataAccessException;

    /**
     * @return <code>List< IEscoUser ></code>
     * @throws DataAccessException
     * @throws LdapException
     */
    List<IEscoUser> getAllSuperUsers() throws DataAccessException, LdapException;

    /**
     * @param uid
     * @param categoryId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean userRolesExistInTopicsOfcategory(final String uid, final Long categoryId) throws DataAccessException;

    /**
     * @param uid
     * @param itemId
     * @return <code>List< String ></code>
     * @throws DataAccessException
     */
    List<String> getUserRolesInTopicsByItem(final String uid, final Long itemId) throws DataAccessException;

    /**
     * @param uid
     * @param tIds
     * @return <code>List< String ></code>
     * @throws DataAccessException
     */
    List<String> getUserRolesInTopicsByTopics(final String uid, final Integer[] tIds) throws DataAccessException;

    /**
     * @param uid
     * @param entityId
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean userRolesExistInCategoriesOfEntity(final String uid, final Long entityId) throws DataAccessException;

    /**
     * @param uid
     * @return <code>boolean</code>
     * @throws DataAccessException
     */
    boolean userRoleExist(final String uid) throws DataAccessException;

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
     * @param usersUid a list of id/uid to retrieve in the LDAP.
     * @return <code>Map< String, IEscoUser ></code> A Map of IEscoUser (details of users) with id/uid as key.
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


}
