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
import com.proyecto.idat.entity.EmpleadoEntity;
import com.proyecto.idat.service.EmpleadoService;
import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<EmpleadoEntity> findAll(@RequestParam(defaultValue = "active") String estado) {
		return empleadoService.findAll(estado);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(empleadoService.findOne(search), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody EmpleadoEntity empleadoEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(empleadoService.create(empleadoEntity), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody EmpleadoEntity empleadoEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(empleadoService.update(empleadoEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		empleadoService.changeState(id,false);
		return httpStatus(HttpStatus.OK, String.format("Empleado with id '%s' deleted successfully", id));
	}

	@PutMapping("/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		empleadoService.changeState(id,true);
		return httpStatus(HttpStatus.OK, String.format("Empleado with id '%s' restored successfully", id));
	}

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}