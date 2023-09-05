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
import com.proyecto.idat.entity.ClienteEntity;
import com.proyecto.idat.service.ClienteService;
import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<ClienteEntity> findAll(
			 @RequestParam(defaultValue = "activo") String estado,
			 @RequestParam(defaultValue = "0") int page,
	         @RequestParam(defaultValue = "10") int page_size
			) {
		return clienteService.findAll(estado,page,page_size);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(clienteService.findOne(search), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<?> add(@Valid @RequestBody ClienteEntity clienteEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(clienteService.create(clienteEntity), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody  ClienteEntity clienteEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(clienteService.update(clienteEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		clienteService.changeState(id,false);
		return httpStatus(HttpStatus.OK, String.format("Cliente with id '%s' deleted successfully", id));
	}
	
	@PutMapping("/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		clienteService.changeState(id,true);
		return httpStatus(HttpStatus.OK, String.format("Cliente with id '%s' restored successfully", id));
	}
	
	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}