/**
 * ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esco.portlets.news.services;

import java.util.List;
import java.util.Map;

import org.esco.portlets.news.dao.EscoUserDao;
import org.esco.portlets.news.domain.EntityRole;
import org.esco.portlets.news.domain.IEscoUser;
import org.esco.portlets.news.services.ldap.LdapUserService;
import org.esupportail.commons.services.ldap.LdapException;
import org.uhp.portlets.news.domain.Group;
import org.uhp.portlets.news.domain.Item;
import org.uhp.portlets.news.domain.Role;
import org.uhp.portlets.news.domain.User;
import org.uhp.portlets.news.service.exception.UserRoleExistForCtxSubType;
import org.uhp.portlets.news.service.exception.UserExistsException;

/**
 * Reprise de la classe org.uhp.portlets.news.service.UserManager.
 * @author GIP RECIA - Gribonvald Julien
 * 8 juil. 09
 */
public interface UserManager {

	/**
	 * Obtains user's details from a list of user's id.
	 * @param usersUid A list of id/uid to retrieve in the LDAP.
	 * @return <code>Map< String, IEscoUser ></code> A Map of LdapUser (details of users) with id/uid as key.
	 */
	Map<String, IEscoUser> getUsersByListUid(final List<String> usersUid);

	/**
	 * Obtain Ldap Properties.
	 * @return <code>LdapUserService</code>
	 */
	LdapUserService getLdapUserService();

	/**
	 * @param userId
	 * @return <code>boolean</code>
	 */
	//    boolean isUserAccountEnabled(String userId);
	/**
	 * @param user
	 */
	void saveUser(final User user);
	/**
	 * @param userId
	 * @param enabled
	 */
	void updateUserStatus(final String userId, final String enabled);
	/**
	 *
	 */
	void updateLastUserAccess();

	/**
	 * @param userId
	 * @return <code>String</code>
	 */
	String getUserNameById(final String userId);

	/**
	 * @param uid
	 */
	void deleteUser(final String uid);

	/**
	 * @param searchBy
	 * @return <code>User</code>
	 */
	IEscoUser findUserByUid(final String searchBy);

	/**
	 * @param token
	 * @return <code>List< User ></code>
	 */
	List<IEscoUser> findPersonsByToken(final String token);

	/**
	 * @param token Research criteria.
	 * @param filter
	 * @return <code>List< User ></code> The list of users returned corresponding to the token.
	 * @throws LdapException
	 */
	List<IEscoUser> findPersonsByTokenAndFilter(final String token, final org.springframework.ldap.support.filter.Filter filter) throws LdapException;

	/**
	 * @param user
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @throws UserRoleExistForCtxSubType
	 */
	//    void addUserCtxRole(User user, String role, String ctx, Long ctxId) throws UserRoleExistForCtxSubType;

	/**
	 * @param group
	 * @param role
	 * @param ctx
	 * @param ctxId
	 * @throws UserRoleExistForCtxSubType
	 */
	//    void addGroupCtxRole(Group group, String role, String ctx, Long ctxId) throws UserRoleExistForCtxSubType;

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param userId
	 * @return <code>boolean</code>
	 */
	//    boolean isUserRoleExistForContext(Long ctxId, String ctxType, String userId);

	/**
	 * @return <code>List< User ></code>
	 */
	List<IEscoUser> getAllUsers();
	/**
	 * @return <code>List< User ></code>
	 */
	List<IEscoUser> getAllSuperUsers();

	/**
	 * Give the list of users or groups having a role in the context with there role associated.
	 * @param target
	 * @param targetCtx
	 * @return <code>List< UserRole ></code>
	 */
	//    List<UserRole> getUserRolesForCtx(Long target, String targetCtx);
	//  move to securityService

	/**
	 * @param ctxId
	 * @param ctxType
	 * @param groupId
	 * @return <code>String</code>
	 */
	//	String getGroupRoleInCtx(Long ctxId, String ctxType, String groupId);
	/**
	 * @param ctxId
	 * @param ctxType
	 * @param userId
	 * @return <code>boolean</code>
	 */
	//    boolean isUserAdminInCtx(Long ctxId, String ctxType, String userId);

	/**
	 * The attribute name of DisplayName in LDAP.
	 * @return <code>String</code>
	 */
	String getAttrDisplayName();

	/**
	 * The attribute name of email in LDAP.
	 * @return <code>String</code>
	 */
	String getAttrMail();

}
