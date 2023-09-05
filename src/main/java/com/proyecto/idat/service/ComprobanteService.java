package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.dtos.CreateComprobanteDto;
import com.proyecto.idat.dtos.UpdateComprobanteDto;
import com.proyecto.idat.entity.ComprobanteEntity;

public interface ComprobanteService {

	public List<ComprobanteEntity> findAll(String estado, int pageNumber, int pageSize);

	public ComprobanteEntity findOne(String search) throws ResponseStatusException;

	public ComprobanteEntity create(CreateComprobanteDto comprobanteEntity);

	public ComprobanteEntity update(UpdateComprobanteDto comprobanteEntity, Long id) throws ResponseStatusException;

	public void changeState(Long id, Boolean value)
			throws ResponseStatusException, IllegalArgumentException, Exception;
}