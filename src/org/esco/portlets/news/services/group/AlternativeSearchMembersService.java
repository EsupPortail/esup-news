/**
 *
 */
package org.esco.portlets.news.services.group;

import java.util.List;

/**
 * @author GIP RECIA - Julien Gribonvald
 * 26 mars 2014
 */
public interface AlternativeSearchMembersService {

	/**
	 * @param groupId
	 * @return <code>List<String></code> List of userId that could be found in ldap.
	 * @throws Exception
	 */
	List<String> AlternativeSearchMembersOfGroupById(final String groupId) throws Exception;

}
