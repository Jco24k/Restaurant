package com.proyecto.idat.controller;

import java.util.HashMap;
import java.util.List;
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

import com.proyecto.idat.dtos.CreateComprobanteDto;
import com.proyecto.idat.dtos.UpdateComprobanteDto;
import com.proyecto.idat.dtos.CreateComprobanteDto.UpdateValidationComprobante;
import com.proyecto.idat.entity.ComprobanteEntity;
import com.proyecto.idat.service.ComprobanteService;
import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

	@Autowired
	private ComprobanteService comprobanteService;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<ComprobanteEntity> findAll(
			@RequestParam(defaultValue = "activo") String estado,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int page_size) {
		return comprobanteService.findAll(estado, page, page_size);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(comprobanteService.findOne(search), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<?> add(@Valid @RequestBody CreateComprobanteDto ComprobanteEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(comprobanteService.create(ComprobanteEntity), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable Long id,
			@RequestBody @Validated(UpdateValidationComprobante.class) UpdateComprobanteDto ComprobanteEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(comprobanteService.update(ComprobanteEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		comprobanteService.changeState(id, false);
		return httpStatus(HttpStatus.OK, String.format("Comprobante with id '%s' deleted successfully", id));
	}

	@PutMapping("/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable Long id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		comprobanteService.changeState(id, true);
		return httpStatus(HttpStatus.OK, String.format("Comprobante with id '%s' restored successfully", id));
	}

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}