package de.fisp.anwesenheit.core.service;

public interface MailBenachtigungsService {

	public abstract void sendeAnragsMail(String benutzerId, long antragId);

}