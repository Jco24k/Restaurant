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
import com.proyecto.idat.dtos.CreateReservacionDto;
import com.proyecto.idat.dtos.CreateReservacionDto.UpdateValidationReservation;
import com.proyecto.idat.dtos.UpdateReservacionDto;
import com.proyecto.idat.entity.ReservacionEntity;
import com.proyecto.idat.service.ReservacionService;
import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/reservacion")
public class ReservacionController {

	@Autowired
	private ReservacionService ReservacionService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ReservacionEntity> findAll(@RequestParam(defaultValue = "activo") String estado,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int page_size) {
		return ReservacionService.findAll(estado, page, page_size);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(ReservacionService.findOne(search), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody CreateReservacionDto createReservacionDto)
			throws ResponseStatusException {
		return new ResponseEntity<>(ReservacionService.create(createReservacionDto), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id,
			@RequestBody @Validated(UpdateValidationReservation.class) UpdateReservacionDto ReservacionEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(ReservacionService.update(ReservacionEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		ReservacionService.changeState(id, false);
		return httpStatus(HttpStatus.OK, String.format("Reservacion with id '%s' deleted successfully", id));
	}

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}
