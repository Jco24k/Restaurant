package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.idat.entity.RolEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.RolRepository;

@Service
public class RolServiceImpl implements RolService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private RolRepository rolRepository;

	@Override
	@Transactional(readOnly = true)
	public List<RolEntity> findAll(String estado, int pageNumber, int pageSize) {
		if (!estado.equals("all") && !estado.equals("activo") && !estado.equals("inactivo")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, activo, inactivo",
					HttpStatus.BAD_REQUEST);
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return rolRepository.findByAllEstado(estado, pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public RolEntity findOne(String search) throws ResponseException {
		RolEntity Rol = rolRepository.findSearchRol(search);
		if (Rol != null) {
			return Rol;
		}
		throw new ResponseException(String.format("Rol with id or name '%s' not found", search),
				HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public RolEntity create(RolEntity rolEntity) throws ResponseException {
		try {
			return rolRepository.save(rolEntity);
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public RolEntity update(RolEntity rolEntity, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		RolEntity rol = rolRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Rol not found", HttpStatus.NOT_FOUND));
		rolEntity.setId(id);
		rol.update(rolEntity);
		return rolRepository.save(rol);
	}

	@Override
	@Transactional
	public void changeState(String id, Boolean value) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		RolEntity rolEntity = rolRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Rol not found", HttpStatus.NOT_FOUND));
		rolEntity.setEstado(value);
		rolRepository.save(rolEntity);
	}

}