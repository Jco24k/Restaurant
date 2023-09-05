package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import  com.proyecto.idat.entity.CategoriaEntity;

public interface CategoriaService {

	 public List<CategoriaEntity> findAll(String estado);

	    public CategoriaEntity findOne(String search)throws ResponseStatusException;

	    public CategoriaEntity create(CategoriaEntity categoriaEntity);

	    public CategoriaEntity update( CategoriaEntity categoriaEntity,String id)throws ResponseStatusException;

	    public void delete(String id) throws ResponseStatusException,IllegalArgumentException,Exception;
}
