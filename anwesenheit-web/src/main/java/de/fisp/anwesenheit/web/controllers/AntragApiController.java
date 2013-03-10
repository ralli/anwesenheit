package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.service.AntragService;

@Controller
@RequestMapping("/api/antraege")
public class AntragApiController {
	@Autowired
	private AntragService antragService;

	private String getCurrentUser() {
		return "juhnke_r";
	}

	private String toJSon(Object object) {
		try {
			StringWriter stringWriter = new StringWriter();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(stringWriter, object);
			return stringWriter.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> index() {
		final String benutzerId = getCurrentUser();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AntragListe liste = antragService.findByBenutzer(benutzerId);
		if (liste == null) {
			return new ResponseEntity<String>(
					"{message: \"Benutzer existiert nicht\"}", headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(toJSon(liste), headers, HttpStatus.OK);		
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> details(@PathVariable long id) {
		AntragsDaten daten = antragService.findAntragById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (daten == null) {
			return new ResponseEntity<String>(
					"{message: \"Antrag existiert nicht\"}", headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(toJSon(daten), headers, HttpStatus.OK);		
	}
}
