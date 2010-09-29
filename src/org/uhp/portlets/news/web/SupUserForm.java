package org.uhp.portlets.news.web;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.Serializable;

import org.esco.portlets.news.domain.EscoUser;
import org.esco.portlets.news.domain.IEscoUser;

/**
 * Modifications GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public class SupUserForm implements Serializable {
	/** */
	private static final long serialVersionUID = -2708596118806519851L;
	/** */
	private String token;
	/** */
	private String role;
	/** */
	private String principal;
	/** */
	private IEscoUser user = new EscoUser();
	
    /**
     * Constructeur de l'objet SupUserForm.java.
     */
    public SupUserForm() {
        super();
    }
    /**
     * Constructeur de l'objet SupUserForm.java.
     * @param token
     * @param role
     * @param principal
     * @param user
     */
    public SupUserForm(final String token, final String role, final String principal, final IEscoUser user) {
        super();
        this.token = token;
        this.role = role;
        this.principal = principal;
        this.user = user;
    }
    
    /**
     * Getter du membre token.
     * @return <code>String</code> le membre token.
     */
    public String getToken() {
        return token;
    }
    /**
     * Setter du membre token.
     * @param token la nouvelle valeur du membre token. 
     */
    public void setToken(final String token) {
        this.token = token;
    }
    /**
     * Getter du membre role.
     * @return <code>String</code> le membre role.
     */
    public String getRole() {
        return role;
    }
    /**
     * Setter du membre role.
     * @param role la nouvelle valeur du membre role. 
     */
    public void setRole(final String role) {
        this.role = role;
    }
    /**
     * Getter du membre principal.
     * @return <code>String</code> le membre principal.
     */
    public String getPrincipal() {
        return principal;
    }
    /**
     * Setter du membre principal.
     * @param principal la nouvelle valeur du membre principal. 
     */
    public void setPrincipal(final String principal) {
        this.principal = principal;
    }
    /**
     * Getter du membre user.
     * @return <code>IEscoUser</code> le membre user.
     */
    public IEscoUser getUser() {
        return user;
    }
    /**
     * Setter du membre user.
     * @param user la nouvelle valeur du membre user. 
     */
    public void setUser(final IEscoUser user) {
        this.user = user;
    }

	
}
