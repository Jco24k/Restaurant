package com.proyecto.idat.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@GroupSequence({ CreateFondoDto.class, CreateFondoDto.UpdateValidation.class })
public class CreateFondoDto {

	@Pattern(message = "empleado '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidation.class, })
	@NotNull(message = "empleado must not be null")
	private String empleado;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime hora;

	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate fecha;

	@NotNull(message = "billetes must not be null")
	@Positive(groups = { UpdateValidation.class })
	@Digits(integer = 10, fraction = 2, message = "billetes must be a number with a maximum of 10 digits and 2 decimal places", groups = {
			UpdateValidation.class, })
	private BigDecimal billetes;

	@NotNull(message = "monedas must not be null")
	@Positive(groups = { UpdateValidation.class, })
	@Digits(integer = 10, fraction = 2, message = "billetes must be a number with a maximum of 10 digits and 2 decimal places", groups = {
			UpdateValidation.class, })
	private BigDecimal monedas;

	@Nullable()
	private BigDecimal total;

	@Nullable()
	private String comentario;


	public interface UpdateValidation {
	}

}