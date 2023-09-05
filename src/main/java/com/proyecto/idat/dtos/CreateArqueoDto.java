package com.proyecto.idat.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.annotation.Nullable;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
@GroupSequence({ CreateArqueoDto.class, CreateArqueoDto.UpdateValidationFondo.class })
public class CreateArqueoDto {

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime hora;

	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate fecha;

	@NotNull(message = "billetes must not be null")
	@Positive(groups = { UpdateValidationFondo.class })
	@Digits(integer = 10, fraction = 2, message = "billetes must be a number with a maximum of 10 digits and 2 decimal places", groups = {
			UpdateValidationFondo.class, })
	private BigDecimal billetes;

	@NotNull(message = "monedas must not be null")
	@Positive(groups = { UpdateValidationFondo.class })
	@Digits(integer = 10, fraction = 2, message = "billetes must be a number with a maximum of 10 digits and 2 decimal places", groups = {
			UpdateValidationFondo.class })
	private BigDecimal monedas;

	@Nullable()
	private BigDecimal total;

	@Nullable()
	private String comentario;

	public interface UpdateValidationFondo {
	}

}