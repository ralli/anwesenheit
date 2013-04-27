package de.fisp.anwesenheit.core.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.service.MailBenachtigungsService;
import de.fisp.anwesenheit.core.service.MailService;

public class MailBenachrichtigungsServiceImpl implements
		MailBenachtigungsService {
	private AntragService antragService;
	private MailService mailService;
	private static final Logger log = LoggerFactory
			.getLogger(MailBenachrichtigungsServiceImpl.class);

	@Autowired
	public MailBenachrichtigungsServiceImpl(AntragService antragService,
			MailService mailService) {
		this.antragService = antragService;
		this.mailService = mailService;
	}

	@Override
	public void sendeAntragsMail(String benutzerId, long antragId) {
		AntragsDaten antrag = antragService
				.findAntragById(benutzerId, antragId);
		for (BewilligungsDaten b : antrag.getBewilligungen()) {
			BenutzerDaten bewilliger = b.getBenutzer();
			String email = bewilliger.getEmail();
			String betreff = getBetreff(antrag, b);
			String text = getAntragsText(antrag, b);			
			mailService.sendeMail(betreff, text,
					"noreply@f-i-solutions-plus.de", email);
		}
	}

	public String getBetreff(AntragsDaten antrag,
			BewilligungsDaten bewilligungsDaten) {
		BenutzerDaten antragSteller = antrag.getBenutzer();

		String betreff = String.format(
				"Urlaubsantrag für %s vom %s bis %s (%.1g Tage)",
				getBenutzerName(antragSteller), formatDate(antrag.getVon()),
				formatDate(antrag.getBis()), antrag.getAnzahlTage());
		return betreff;
	}

	public String getAntragsText(AntragsDaten antrag,
			BewilligungsDaten bewilligungsDaten) {
		BenutzerDaten antragSteller = antrag.getBenutzer();
		StringWriter stringWriter = new StringWriter();
		PrintWriter p = new PrintWriter(stringWriter);
		p.println(String.format("<p>Für Sie liegt ein Urlaubsantrag von %s zur Bewilligung vor.</p>", getBenutzerName(antragSteller)));
		
		p.print("Details: <ul>");
		p.print("<li>");
		p.print(String.format("Art: %s", antrag.getAntragArt().getAntragArt()));
		if ("SONDERURLAUB".equals(antrag.getAntragArt().getAntragArt())) {
			p.println(" Sonderurlaubsart: "
					+ antrag.getSonderUrlaubArt().getBezeichnung());
		} else {
			p.println();
		}
		p.print("</li>");
		p.print("<li>");
		p.println(String.format("Antragszeitraum: %s bis %s (%s Tage)\n\n",
				formatDate(antrag.getVon()), formatDate(antrag.getBis()),
				formatTage(antrag.getAnzahlTage())));
		p.print("</li>");
		p.print("</ul>");
		p.print("<p>");
		p.println(String.format("<a href=\"%s\">Direkt zum Antrag</a>",
				getUrlFor(antrag.getId())));

		p.print("</p>");

		p.flush();
		String result = stringWriter.toString();
		log.debug("getAntragText: {}", result);
		return result;

	}

	private String formatTage(double tage) {
		NumberFormat fmt = new DecimalFormat("0.#");
		return fmt.format(tage);
	}
	private Object getUrlFor(long antragId) {
		return "http://srv-1822direkt2:8080/anwesenheit-web/#!/antraege/"
				+ antragId;
	}

	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		return format.format(date);
	}

	private String getBenutzerName(BenutzerDaten benutzer) {
		List<String> namen = new ArrayList<String>();
		if (StringUtils.isNotBlank(benutzer.getVorname()))
			namen.add(benutzer.getVorname());
		if (StringUtils.isNotBlank(benutzer.getNachname()))
			namen.add(benutzer.getNachname());
		if (namen.isEmpty())
			namen.add(benutzer.getBenutzerId());
		return StringUtils.join(namen, ' ');
	}
}
