package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.entity.RolEntity;

public interface RolService {

	public List<RolEntity> findAll(String estado, int pageNumber, int pageSize);

	public RolEntity findOne(String search) throws ResponseStatusException;

	public RolEntity create(RolEntity RolEntity);

	public RolEntity update(RolEntity RolEntity, String id) throws ResponseStatusException;

	public void changeState(String id, Boolean value) throws ResponseStatusException,IllegalArgumentException,Exception;
	
}