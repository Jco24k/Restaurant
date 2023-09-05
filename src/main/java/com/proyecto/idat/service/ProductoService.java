package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import  com.proyecto.idat.entity.ProductoEntity;

public interface ProductoService {

	 public List<ProductoEntity> findAll(String estado);

	    public ProductoEntity findOne(String search)throws ResponseStatusException;

	    public ProductoEntity create(ProductoEntity productoEntity);

	    public ProductoEntity update( ProductoEntity productoEntity,String id)throws ResponseStatusException;

	    public void delete(String id) throws ResponseStatusException,IllegalArgumentException,Exception;
}
