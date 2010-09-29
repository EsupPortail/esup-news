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
 * Modification GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public final class UserForm implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private String token;
	/** */
	private IEscoUser user = new EscoUser();
	
    /**
     * Constructeur de l'objet UserForm.java.
     */
    public UserForm() {
        super();
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
