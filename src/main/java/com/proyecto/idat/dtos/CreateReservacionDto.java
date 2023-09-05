package com.proyecto.idat.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
@GroupSequence({ CreateReservacionDto.class, CreateReservacionDto.UpdateValidationReservation.class })
public class CreateReservacionDto {

	@Pattern(message = "cliente  must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationReservation.class, })
	@NotNull(message = "cliente must not be null")
	private String cliente;

	@Pattern(message = "empleado  must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationReservation.class, })
	@NotNull(message = "empleado must not be null")
	private String empleado;

	@Pattern(message = "mesa  must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationReservation.class, })
	@NotNull(message = "mesa must not be null")
	private String mesa;

	@NotNull(message = "cant_comensales must not be null")
	@Positive()
	private Integer cant_comensales;

	@NotNull()
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate fecha_reservacion;

	@NotNull()
	@DateTimeFormat(pattern = "HH-mm-ss", iso = ISO.DATE)
	private LocalTime hora_reservacion;

	@Nullable()
	private String observacion;

	@Column(name = "total", nullable = false, columnDefinition = "Decimal(10,2)")
	@NotNull(message = "total must not be null")
	@Positive()
	private BigDecimal total;

	public interface UpdateValidationReservation {
	}
}