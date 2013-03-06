package de.fisp.anwesenheit.core.entities;

public class Benutzer {
	private String userId;
	private String vorname;
	private String nachname;
	private String benutzertyp; // native / ldap
	private String email;
	private String salt;
	private String passwordHash;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getBenutzertyp() {
		return benutzertyp;
	}

	public void setBenutzertyp(String benutzertyp) {
		this.benutzertyp = benutzertyp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
