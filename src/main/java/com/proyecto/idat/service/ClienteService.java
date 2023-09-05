package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.entity.ClienteEntity;

public interface ClienteService {

	public List<ClienteEntity> findAll(String estado, int pageNumber, int pageSize);

	public ClienteEntity findOne(String search) throws ResponseStatusException;

	public ClienteEntity create(ClienteEntity ClienteEntity);

	public ClienteEntity update(ClienteEntity ClienteEntity, String id) throws ResponseStatusException;

	public void changeState(String id, Boolean value)
			throws ResponseStatusException, IllegalArgumentException, Exception;
}
