/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esco.portlets.news.services;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esupportail.commons.services.ldap.LdapException;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.domain.UserRole;
import org.uhp.portlets.news.service.exception.SubscriberExistForCtxSubType;
import org.uhp.portlets.news.service.exception.UserExistsException;

/**
 * Reprise de la classe org.uhp.portlets.news.service.UserManager.
 * @author GIP RECIA - Gribonvald Julien
 * 8 juil. 09
 */
public interface UserManager {

    /**
     * Getter du membre userDao.
     * @return <code>EscoUserDao</code> le membre userDao.
     */
    EscoUserDao getUserDao();

    /**
     * Obtains user's details from a list of user's id.
     * @param usersUid A list of id/uid to retrieve in the LDAP.
     * @return <code>Map< String, IEscoUser ></code> A Map of LdapUser (details of users) with id/uid as key.
     */
	Map<String, IEscoUser> getUsersByListUid(List<String> usersUid);

	/**
	 * Obtain Ldap Properties.
	 * @return <code>LdapUserService</code>
	 */
	LdapUserService getLdapUserService();

    /**
     * @param userId
     * @return <code>boolean </code>
     */
    boolean isSuperAdmin(String userId);
    /**
     * @param userId
     * @return <code>boolean</code>
     */
    boolean isUserAccountEnabled(String userId);
    /**
     * @param user
     * @throws UserExistsException
     */
    void saveUser(User user) throws UserExistsException;
    /**
     * @param uid
     * @param enabled
     */
    void updateUserStatus(String uid, String enabled);
    /**
     * @param uid
     */
    void updateUserLastAccess(String uid);

    /**
     * @param uid
     * @return <code>String</code>
     */
    String getUserNameByUid(String uid);

    /**
     * @param uid
     */
    void deleteUser(String  uid);

    /**
     * @param searchBy
     * @return <code>User</code>
     */
    IEscoUser findUserByUid(String searchBy);

    /**
     * @param token
     * @return <code>List< User ></code>
     */
    List<IEscoUser> findPersonsByToken(String token);

    /**
     * @param token Research criteria.
     * @param filter
     * @return <code>List< User ></code> The list of users returned corresponding to the token.
     * @throws LdapException
     */
    List<IEscoUser> findPersonsByTokenAndFilter(final String token,
            final org.springframework.ldap.support.filter.Filter filter) throws LdapException;

    /**
     * @param user
     * @param role
     * @param ctx
     * @param ctxId
     * @throws SubscriberExistForCtxSubType
     */
    void addUserCtxRole(User user, String role, String ctx, Long ctxId) throws SubscriberExistForCtxSubType;

    /**
     * Use this function with caution,
     * it's usefull to make the migration of user role with the introduction of the entity context.
     * @param user
     * @param entityId
     */
    void migrationUserCtxRole(final User user, final Long entityId);

    /**
     * @param id
     * @param target
     * @param targetCtx
     */
    void removeUserRoleForCtx(String id, Long target, String targetCtx);

    /**
     * @param target
     * @param targetCtx
     * @return <code>Map< String, List< UserRole >></code>
     */
    Map<String, List<UserRole>> getUserRolesByCtxId(Long target, String targetCtx);


    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code>
     */
    boolean isUserRoleExistForContext(Long ctxId, String ctxType, String uid);

    /**
     * @return <code>List< User ></code>
     */
    List<IEscoUser> getAllUsers();
    /**
     * @return <code>List< User ></code>
     */
    List<IEscoUser> getAllSuperUsers();
    /**
     * @return <code>List< Role ></code>
     */
    List<Role> getAllRoles();

    /**
     * Give the list of users having a role in the context with there role associated.
     * @param target
     * @param targetCtx
     * @return <code>List< UserRole ></code>
     */
    List<UserRole> getUsersRolesForCtx(Long target, String targetCtx);
//  move to securityService
    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>String</code>
     */
    String getUserRoleInCtx(Long ctxId, String ctxType, String uid);
//   boolean hasRole(String uid, Long cId, Long[] tIds);

//   boolean isContributor(String uid,  String[] tIds);
    /**
     * @param ctxId
     * @param ctxType
     * @param uid
     * @return <code>boolean</code>
     */
    boolean isUserAdminInCtx(Long ctxId, String ctxType, String uid);
    /**
     * @param userId
     * @return <code>Map< String, String ></code>
     */
    Map<String, String> loadUserEntityRoleMaps(String userId);
    /**
     * @param userId
     * @return <code>Map< String, Map< String, String >></code>
     */
    Map<String, Map<String, String>> loadUserCategoryRoleMaps(String userId);
    /**
     * @param userId
     * @return <code>Map< String, Map< String, String >></code>
     */
    Map<String, Map<String, String>> loadUserTopicRoleMaps(String userId);

    /**
     * @param userId
     * @return <code>boolean</code>
     */
    boolean isPermitted(String userId);
    //      boolean canViewOnly(String uid, Long itemId);
    //       boolean canValidate(String uid, String itemId);
    /**
     * @param uid
     * @param item
     * @return <code>boolean</code>
     */
    boolean canValidate(String uid, Item item);
    /**
     * @param uid
     * @param item
     * @return <code>boolean</code>
     */
    boolean canEditItem(String uid, Item item);
}
