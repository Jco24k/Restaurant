package com.proyecto.idat.service;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.proyecto.idat.dtos.CreateReservacionDto;
import com.proyecto.idat.dtos.UpdateReservacionDto;
import com.proyecto.idat.entity.ReservacionEntity;

public interface ReservacionService {

	public List<ReservacionEntity> findAll(String estado, int pageNumber, int pageSize);

	public ReservacionEntity findOne(String search) throws ResponseStatusException;

	public ReservacionEntity create(CreateReservacionDto reservacionEntity);

	public ReservacionEntity update(UpdateReservacionDto reservacionEntity, String id) throws ResponseStatusException;

	public void changeState(String id, Boolean value)
			throws ResponseStatusException, IllegalArgumentException, Exception;
}