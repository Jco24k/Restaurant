package com.proyecto.idat.service;

import java.util.Collection;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.dtos.CreateArqueoDto;
import com.proyecto.idat.dtos.UpdateArqueoDto;
import com.proyecto.idat.entity.ArqueoEntity;
import com.proyecto.idat.mapper.ArqueoMapper;

public interface ArqueoService {

	public Collection<ArqueoMapper> findAll(String estado);

	public ArqueoEntity findOne(String search) throws ResponseStatusException;

	public ArqueoEntity create(CreateArqueoDto arqueoEntity);

	public ArqueoEntity update(UpdateArqueoDto arqueoEntity, String id)
			throws ResponseStatusException;

	public void delete(String id) throws ResponseStatusException, IllegalArgumentException, Exception;

}
