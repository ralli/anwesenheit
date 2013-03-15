package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.service.AntragService;

@Controller
@RequestMapping("/api/antraege")
public class AntragApiController {
	@Autowired
	private AntragService antragService;
	private static final Logger logger = LoggerFactory
			.getLogger(AntragApiController.class);

	private String getCurrentUser() {
		return "juhnke_r";
	}

	private String toJson(Object object) {
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
					jsonMessage("Benutzer existiert nicht"), headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(toJson(liste), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> details(@PathVariable long id) {
		AntragsDaten daten = antragService.findAntragById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (daten == null) {
			return new ResponseEntity<String>(
					jsonMessage("Antrag existiert nicht"), headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(toJson(daten), headers, HttpStatus.OK);
	}

	private String jsonMessage(String message) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("message", message);
		return toJson(map);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	ResponseEntity<String> delete(@PathVariable long id) {
		boolean result = antragService.deleteAntrag(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (result == false) {
			return new ResponseEntity<String>(
					jsonMessage("Antrag existiert nicht"), headers,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(jsonMessage("Ok"), headers,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> insert(
			@RequestBody @Valid CreateAntragCommand createAntragCommand) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			createAntragCommand.setBenutzerId(getCurrentUser());
			long antragId = antragService.createAntrag(createAntragCommand);
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result.put("antragId", antragId);
			return new ResponseEntity<String>(toJson(result), headers,
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Fehler beim Speichern", ex);
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result.put("message", ex.getMessage());
			return new ResponseEntity<String>(toJson(result), headers,
					HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
