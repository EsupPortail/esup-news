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

import org.uhp.portlets.news.domain.Subscriber;

/**
 * @author GIP RECIA - Gribonvald Julien
 * 14 avr. 2010
 */
public class SubForm implements Serializable {

    /** */
    private static final long serialVersionUID = -5774020359554104868L;
    /** */
    private String token;
    /** */
    private String[] subKey;
    /** */
    private boolean found;
    /** */
    private Subscriber subscriber = new Subscriber();
    
    /**
     * Constructeur de l'objet SubForm.java.
     */
    public SubForm() {
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
     * Getter du membre subKey.
     * @return <code>String[]</code> le membre subKey.
     */
    public String[] getSubKey() {
        return subKey;
    }
    /**
     * Setter du membre subKey.
     * @param subKey la nouvelle valeur du membre subKey. 
     */
    public void setSubKey(final String[] subKey) {
        this.subKey = subKey;
    }
    /**
     * Getter du membre found.
     * @return <code>boolean</code> le membre found.
     */
    public boolean isFound() {
        return found;
    }
    /**
     * Setter du membre found.
     * @param found la nouvelle valeur du membre found. 
     */
    public void setFound(final boolean found) {
        this.found = found;
    }
    /**
     * Getter du membre subscriber.
     * @return <code>Subscriber</code> le membre subscriber.
     */
    public Subscriber getSubscriber() {
        return subscriber;
    }
    /**
     * Setter du membre subscriber.
     * @param subscriber la nouvelle valeur du membre subscriber. 
     */
    public void setSubscriber(final Subscriber subscriber) {
        this.subscriber = subscriber;
    }
    
}
