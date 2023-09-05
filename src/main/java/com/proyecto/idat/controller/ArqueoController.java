package com.proyecto.idat.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.proyecto.idat.dtos.CreateArqueoDto;
import com.proyecto.idat.dtos.UpdateArqueoDto;
import com.proyecto.idat.dtos.CreateFondoDto.UpdateValidation;
import com.proyecto.idat.mapper.ArqueoMapper;
import com.proyecto.idat.service.ArqueoService;
import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/fondo-arqueo")
public class ArqueoController {

	@Autowired
	private ArqueoService arqueoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<ArqueoMapper> findAll(@RequestParam(defaultValue = "active") String estado) {
		return arqueoService.findAll(estado);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(arqueoService.findOne(search), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody CreateArqueoDto createFondoDto)
			throws ResponseStatusException {
		return new ResponseEntity<>(arqueoService.create(createFondoDto), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id,
			@RequestBody @Validated(UpdateValidation.class) UpdateArqueoDto ArqueoEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(arqueoService.update(ArqueoEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		arqueoService.delete(id);
		return httpStatus(HttpStatus.OK, String.format("Arqueo with id '%s' deleted successfully", id));
	}

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}
