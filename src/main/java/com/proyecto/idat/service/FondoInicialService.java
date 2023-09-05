package com.proyecto.idat.service;

import java.util.Collection;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.dtos.CreateFondoDto;
import com.proyecto.idat.dtos.UpdateFondoDto;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.mapper.FondoMapper;

public interface FondoInicialService {

	public Collection<FondoMapper> findAll(String estado);

	public FondoInicialEntity findOne(String search) throws ResponseStatusException;

	public FondoInicialEntity create(CreateFondoDto FondoInicialEntity);

	public FondoInicialEntity update(UpdateFondoDto FondoInicialEntity, String id)
			throws ResponseStatusException;

	public void delete(String id, Boolean option) throws ResponseStatusException, IllegalArgumentException, Exception;

	public FondoInicialEntity verificarFondoInicial(Boolean option, String message) throws ResponseStatusException;

	public void isStartedChange(FondoInicialEntity fondoInicialEntity, Boolean option)
			throws ResponseException, IllegalArgumentException, Exception;

	public FondoInicialEntity verificarInicioProcesos() throws ResponseException;

}
