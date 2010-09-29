/**
* ESUP-Portail News - Copyright (c) 2009 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/

package org.esco.portlets.news.domain;

import java.io.Serializable;
import java.util.Date;

import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserImpl;
import org.uhp.portlets.news.domain.User;

/**
 * @author GIP RECIA - Gribonvald Julien
 * 20 juil. 09
 */
public class EscoUser extends LdapUserImpl implements User, LdapUser, IEscoUser, Serializable {

    /** Identifiant de sérialisation. */
	private static final long serialVersionUID = -46755581124730956L;

	/** Nom d'affichage. */
	private String displayName;
	/** Email. */
	private String email;
	/** To be super Admin. */
	private String isSuperAdmin;
	/** Account enabled. */
	private String enabled;
	/** Date of the first access. */
	private Date registerDate;
	/** Date of the last access. */
	private Date lastAccess;
	
	/** If the user exist in the Ldap directory. */
	private boolean foundInLdap;

	/**
	 * Constructeur de l'objet EscoUser.java.
	 */
	public EscoUser() {
		super();		
	}
	
	/**
     * Constructeur de l'objet EscoUser.java.
     * @param userId
     * @param displayName
     * @param email
     * @param isSuperAdmin
     * @param enabled
     * @param lastAccess
     */
    /*public EscoUser(final String userId, final String displayName, final String email, 
            final String isSuperAdmin, final String enabled, final Date lastAccess) {
        super();
        this.setId(userId);
        this.displayName = displayName;
        this.email = email;
        this.isSuperAdmin = isSuperAdmin;
        this.enabled = enabled;
        this.lastAccess = lastAccess;
    }*/

	/**
	 * @return The details of the person.
	 * @see org.esupportail.commons.services.ldap.LdapEntityImpl#toString()
	 */
	@Override
    public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append(" EscoUser [displayName = ");
	    sb.append(this.displayName);
	    sb.append(", email = ");
	    sb.append(this.email);
	    sb.append(", uid = ");
	    sb.append(this.getId());
	    sb.append(", superAdmin = ");
	    sb.append(this.isSuperAdmin);
	    sb.append(", enabled = ");
	    sb.append(this.enabled);
	    sb.append(", registerDate = ");
	    sb.append(this.registerDate);
	    sb.append(", lastAccess = ");
	    sb.append(this.lastAccess);
	    sb.append(", foundInLdap = ");
	    sb.append(this.foundInLdap);
	    sb.append(", ");
	    sb.append(super.toString());
	    sb.append("] ");
		return sb.toString();
	}

	/**
     * Getter du membre userId.
     * @return <code>String</code> le membre userId.
     */
    public String getUserId() {
        return getId();
    }

    /**
     * Setter du membre userId.
     * @param userId la nouvelle valeur du membre userId. 
     */
    public void setUserId(final String userId) {
        this.setId(userId);
    }

    /**
     * Getter du membre displayName.
     * @return <code>String</code> le membre displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter du membre displayName.
     * @param displayName la nouvelle valeur du membre displayName. 
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter du membre email.
     * @return <code>String</code> le membre email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter du membre email.
     * @param email la nouvelle valeur du membre email. 
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Getter du membre isSuperAdmin.
     * @return <code>String</code> le membre isSuperAdmin.
     */
    public String getIsSuperAdmin() {
        return isSuperAdmin;
    }

    /**
     * Setter du membre isSuperAdmin.
     * @param isSuperAdmin la nouvelle valeur du membre isSuperAdmin. 
     */
    public void setIsSuperAdmin(final String isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    /**
     * Getter du membre enabled.
     * @return <code>String</code> le membre enabled.
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * Setter du membre enabled.
     * @param enabled la nouvelle valeur du membre enabled. 
     */
    public void setEnabled(final String enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter du membre registerDate.
     * @return <code>Date</code> le membre registerDate.
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * Setter du membre registerDate.
     * @param registerDate la nouvelle valeur du membre registerDate. 
     */
    public void setRegisterDate(final Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Getter du membre lastAccess.
     * @return <code>Date</code> le membre lastAccess.
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     * Setter du membre lastAccess.
     * @param lastAccess la nouvelle valeur du membre lastAccess. 
     */
    public void setLastAccess(final Date lastAccess) {
        this.lastAccess = lastAccess;
    }
    /**
     * Getter du membre foundInLdap.
     * @return <code>boolean</code> le membre foundInLdap.
     */
    public boolean isFoundInLdap() {
        return foundInLdap;
    }

    /**
     * Setter du membre foundInLdap.
     * @param foundInLdap la nouvelle valeur du membre foundInLdap. 
     */
    public void setFoundInLdap(final boolean foundInLdap) {
        this.foundInLdap = foundInLdap;
    }
    
    /**
     * Init this object from a database request.
     * @param user
     */
    public void setFromUser(final User user) {
        this.setId(user.getUserId());
        this.enabled = user.getEnabled();
        this.isSuperAdmin = user.getIsSuperAdmin();
        this.lastAccess = user.getLastAccess();
        this.registerDate = user.getRegisterDate();        
    }
    
    /**
     * @param ldapuser The user entity from Ldap.
     * @param attrDisplayName The name of the ldap attribute for the displayName.
     * @param attrEmail The name of the Ldap attribute for the email.
     */
    public void setFromLdapUser(final LdapUser ldapuser, final String attrDisplayName, final String attrEmail) {
        this.setAttributes(ldapuser.getAttributes());
        this.setId(ldapuser.getId());
        this.setDisplayName(ldapuser.getAttribute(attrDisplayName));
        this.setEmail(ldapuser.getAttribute(attrEmail));
        this.setFoundInLdap(true);
    }

    /**
     * @return <code>int</code> Get the hashCode value of the instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (displayName != null) {
		    result += displayName.hashCode();
		}
		result = prime * result;
		if (email != null) {
		    result += email.hashCode();
		}
		result = prime * result;
		if (getId() != null) {
		    result += getId().hashCode();
		}
		return result;
	}

	/**
	 * @param obj the instance of object to compare
	 * @return <code>boolean</code> Check if the instance of obj is equals to this object.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
		    return true;
		}
		if (obj == null) {
		    return false;
		}
		if (getClass() != obj.getClass()) {
		    return false;
		}
		final EscoUser other = (EscoUser) obj;
		if (displayName == null) {
			if (other.displayName != null) {
			    return false;
			}
		} else if (!displayName.equals(other.displayName)) {
		    return false;
		}
		if (email == null) {
			if (other.email != null) {
			    return false;
			}
		} else if (!email.equals(other.email)) {
		    return false;
		}
		if (getId() == null) {
			if (other.getId() != null) {
			    return false;
			}
		} else if (!getId().equals(other.getId())) {
		    return false;
		}
		return true;
	}

}