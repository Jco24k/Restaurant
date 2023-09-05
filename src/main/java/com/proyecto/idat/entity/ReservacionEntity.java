package com.proyecto.idat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reservaciones")
public class ReservacionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 36, columnDefinition = "char(36)")
	private String id;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = true)
	private ClienteEntity cliente;

	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = true)
	private EmpleadoEntity empleado;

	@ManyToOne
	@JoinColumn(name = "mesa_id")
	private MesaEntity mesa;

	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate fecha_reservacion;

	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime hora_reservacion;

	@NotNull(message = "total must not be null")
	private BigDecimal total;
	
	@Column(name = "cant_comensales", nullable = false)
	private Integer cant_comensales;

	@Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
	private Boolean estado;
	
	@Column(name = "observacion", nullable = true, length = 240)
	private String observacion;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID().toString();
		if (fecha_reservacion == null) {
			LocalDate horaActual = LocalDate.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fecha_reservacion = LocalDate.parse(horaActual.format(formato));
		}
		if (hora_reservacion == null) {
			LocalTime horaActual = LocalTime.now();
			hora_reservacion = horaActual;
		}
		if (estado == null) {
			estado = true;
		}
	}

}
