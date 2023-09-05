package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.entity.DetallePedidoEntity;

public interface DetallePedidoService {

	public List<DetallePedidoEntity> findAll(int pageNumber, int pageSize);

	public DetallePedidoEntity findOne(String cab, String pro) throws ResponseStatusException;

	public DetallePedidoEntity create(DetallePedidoEntity detallePedidoEntity);

	public DetallePedidoEntity update(DetallePedidoEntity detallePedidoEntity, String id)
			throws ResponseStatusException;

	public void delete(String id) throws ResponseStatusException, IllegalArgumentException, Exception;
}
