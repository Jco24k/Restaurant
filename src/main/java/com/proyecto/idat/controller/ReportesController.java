package com.proyecto.idat.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.proyecto.idat.dtos.ReporteMesDto;
import com.proyecto.idat.entity.CabeceraPedidoEntity;
import com.proyecto.idat.service.CabeceraPedidoService;



@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/reportes")
public class ReportesController {

	@Autowired
	private CabeceraPedidoService cabPedidoService;

	@GetMapping("/ventas")
	@ResponseStatus(HttpStatus.OK)
	public List<CabeceraPedidoEntity> findAllByDate(@RequestParam(required = false) LocalDate fecha,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int page_size) {
		return cabPedidoService.filterSale(fecha, page, page_size);
	}

	@GetMapping("/mes")
	@ResponseStatus(HttpStatus.OK)
	public List<ReporteMesDto> findAllByMes(@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int page_size) {
		return cabPedidoService.findAllMountYear(year, month, page, page_size);
	}

}
