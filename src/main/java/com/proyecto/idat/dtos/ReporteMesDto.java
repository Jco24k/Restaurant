package com.proyecto.idat.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReporteMesDto {
	LocalDate getFecha();

	Integer getQuantity();

	BigDecimal getTotal();

}
