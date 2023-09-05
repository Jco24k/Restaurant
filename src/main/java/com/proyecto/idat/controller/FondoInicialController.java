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

import com.proyecto.idat.dtos.CreateFondoDto;
import com.proyecto.idat.dtos.CreateFondoDto.UpdateValidation;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.dtos.UpdateFondoDto;
import com.proyecto.idat.mapper.FondoMapper;
import com.proyecto.idat.service.FondoInicialService;
import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/fondo-inicial")
public class FondoInicialController {

	@Autowired
	private FondoInicialService fondoInicialService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<FondoMapper> findAll(@RequestParam(defaultValue = "active") String estado) {
		return fondoInicialService.findAll(estado);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(fondoInicialService.findOne(search), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody CreateFondoDto createFondoDto)
			throws ResponseStatusException {
		return new ResponseEntity<>(fondoInicialService.create(createFondoDto), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id,
			@RequestBody @Validated(UpdateValidation.class) UpdateFondoDto FondoInicialEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(fondoInicialService.update(FondoInicialEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		fondoInicialService.delete(id, false);
		return httpStatus(HttpStatus.OK, String.format("FondoInicial with id '%s' deleted successfully", id));
	}

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}

	@GetMapping("/verificar/procesos-activos")
	@ResponseStatus(HttpStatus.OK)
	public FondoInicialEntity findVerify() {
		return fondoInicialService.verificarInicioProcesos();
	}
}
