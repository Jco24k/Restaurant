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
import com.proyecto.idat.entity.MesaEntity;
import com.proyecto.idat.service.MesaService;
import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/mesas")
public class MesaController {

	@Autowired
	private MesaService mesaService;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<MesaEntity> findAll(
			 @RequestParam(defaultValue = "0") int page,
	         @RequestParam(defaultValue = "10") int page_size
			) {
		return mesaService.findAll(page,page_size);
	}

	@GetMapping("/{search}")
	public ResponseEntity<?> search(@PathVariable String search) throws ResponseStatusException {
		return new ResponseEntity<>(mesaService.findOne(search), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<?> add(@Valid @RequestBody MesaEntity mesaEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(mesaService.create(mesaEntity), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody @Valid MesaEntity mesaEntity)
			throws ResponseStatusException {
		return new ResponseEntity<>(mesaService.update(mesaEntity, id), HttpStatus.OK);

	}

	@DeleteMapping("/{id}/{estado}")
	public ResponseEntity<?> changeState(@PathVariable String id,@PathVariable String estado)
			throws ClassNotFoundException, IllegalArgumentException, Exception {
		mesaService.changeState(id,estado);
		return httpStatus(HttpStatus.OK, String.format("Mesa with id '%s' deleted successfully", id));
	}


	private ResponseEntity<?> httpStatus(HttpStatus http, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, http);
	}
	
	@GetMapping("/libres")
	public ResponseEntity<?> getMesasLibres(){
		return new ResponseEntity<>(mesaService.findTableByStatusFree(),HttpStatus.OK);
	}
}
