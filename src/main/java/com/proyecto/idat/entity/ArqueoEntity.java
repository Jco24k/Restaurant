package com.proyecto.idat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "arqueo")
public class ArqueoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, length = 36, columnDefinition = "char(36)")
	private String id;

	@Column(name = "hora", nullable = false)
	private LocalTime hora;

	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Column(name = "billetes", nullable = false, columnDefinition = "Decimal(10,2)")
	private BigDecimal billetes;

	@Column(name = "monedas", nullable = false, columnDefinition = "Decimal(10,2)")
	private BigDecimal monedas;

	@Column(name = "total", nullable = false, columnDefinition = "Decimal(10,2)")
	private BigDecimal total;

	@Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
	private Boolean estado;

	@Column(name = "comentario", nullable = true, length = 240)
	private String comentario;

	@OneToOne
	@JoinColumn(name = "fondo_inicial_id", nullable = true, unique = true)
	private FondoInicialEntity fondoInicial;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID().toString();
		if (fecha == null) {
			LocalDate horaActual = LocalDate.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fecha = LocalDate.parse(horaActual.format(formato));
		}
		if (hora == null) {
			LocalTime horaActual = LocalTime.now();
			hora = horaActual;
		}
		if (estado == null) {
			estado = true;
		}

		total = billetes.add(monedas);
	}

	@PreUpdate
	public void preUpdate() {
		total = billetes.add(monedas);
	}
}