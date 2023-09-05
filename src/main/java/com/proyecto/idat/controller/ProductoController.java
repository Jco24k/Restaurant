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
import com.proyecto.idat.entity.ProductoEntity;
import com.proyecto.idat.service.ProductoService;
import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private ProductoService ProductoService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductoEntity> findAll(@RequestParam(defaultValue = "active") String estado) {
		return ProductoService.findAll(estado);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(ProductoService.findOne(search), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody ProductoEntity clienteEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(ProductoService.create(clienteEntity), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody @Valid ProductoEntity ProductoEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(ProductoService.update(ProductoEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		ProductoService.delete(id);
		return httpStatus(HttpStatus.OK, String.format("Producto with id '%s' deleted successfully", id));
	}
	
	/*@PutMapping("/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable String id)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		ProductoService.delete(id,true);
		return httpStatus(HttpStatus.OK, String.format("Rol with id '%s' restored successfully", id));
	}*/

	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
}
