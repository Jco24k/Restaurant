package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.idat.entity.ClienteEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ClienteEntity> findAll(String estado, int pageNumber, int pageSize) {
		if (!estado.equals("all") && !estado.equals("activo") && !estado.equals("inactivo")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, activo, inactivo",
					HttpStatus.BAD_REQUEST);
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return clienteRepository.findByAllEstado(estado, pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteEntity findOne(String search) throws ResponseException {
		ClienteEntity Cliente;
		Cliente = clienteRepository.findSearchEntity(search);
		if (Cliente == null) {
			throw new ResponseException(String.format("Cliente with id | email | dni '%s' not found", search),
					HttpStatus.NOT_FOUND);
		}
		return Cliente;
	}

	@Override
	@Transactional
	public ClienteEntity create(ClienteEntity ClienteEntity) throws ResponseException {
		try {
			return clienteRepository.save(ClienteEntity);
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	// @Transactional
	public ClienteEntity update(ClienteEntity clienteEntity, String id) throws ResponseException {
		// TODO Auto-generated method stub
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ClienteEntity cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Cliente not found", HttpStatus.NOT_FOUND));
		clienteEntity.setId(id);
		cliente.updateClient(clienteEntity);
		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public void changeState(String id, Boolean value) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ClienteEntity ClienteEntity = clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Cliente not found", HttpStatus.NOT_FOUND));
		ClienteEntity.setEstado(value);
		clienteRepository.save(ClienteEntity);
	}

}