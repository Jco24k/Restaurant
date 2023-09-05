package com.proyecto.idat.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.dtos.CabeceraPedidoDto;
import com.proyecto.idat.dtos.CreateCabPedidoDto;
import com.proyecto.idat.dtos.ReporteMesDto;
import com.proyecto.idat.entity.CabeceraPedidoEntity;
import com.proyecto.idat.exception.ResponseException;

public interface CabeceraPedidoService {

	public List<CabeceraPedidoEntity> findAll(String estado, int pageNumber, int pageSize);

	public CabeceraPedidoEntity findOne(String search) throws ResponseStatusException;

	public CabeceraPedidoEntity create(CabeceraPedidoDto cabeceraPedidoEntity);
	
	public CabeceraPedidoEntity createOne(CreateCabPedidoDto createCabPedidoDto) throws ResponseException;

	public CabeceraPedidoEntity update(CabeceraPedidoDto cabeceraPedidoEntity, String id)
			throws ResponseStatusException;

	public void changeState(String id, String value)
			throws ResponseStatusException, IllegalArgumentException, Exception;
	
	public List<CabeceraPedidoEntity> filterSale(LocalDate date, int pageNumber, int pageSize);

	public List<ReporteMesDto> findAllMountYear(Integer year, Integer month, int pageNumber, int pageSize);

}
