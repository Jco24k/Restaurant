package com.proyecto.idat.service;

import java.util.Collection;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.entity.MesaEntity;

public interface MesaService {

	//public List<MesaEntity> findAll(String estado, int pageNumber, int pageSize);
	public List<MesaEntity> findAll(int pageNumber, int pageSize);

	public MesaEntity findOne(String search) throws ResponseStatusException;

	public MesaEntity create(MesaEntity mesaEntity);

	public MesaEntity update(MesaEntity mesaEntity, String id) throws ResponseStatusException;

	public void changeState(String id, String value)
			throws ResponseStatusException, IllegalArgumentException, Exception;
	
	public Collection<MesaEntity> findTableByStatusFree();

}
