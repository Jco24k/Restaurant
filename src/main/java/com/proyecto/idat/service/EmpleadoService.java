package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.entity.EmpleadoEntity;

public interface EmpleadoService {

	public List<EmpleadoEntity> findAll(String estado);

	public EmpleadoEntity findOne(String search) throws ResponseStatusException;

	public EmpleadoEntity create(EmpleadoEntity empleadoEntity);

	public EmpleadoEntity update(EmpleadoEntity empleadoEntity, String id) throws ResponseStatusException;

	public void changeState(String id, Boolean value) throws ResponseStatusException,IllegalArgumentException,Exception;
	
}
