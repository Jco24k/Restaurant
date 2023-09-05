package com.proyecto.idat.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.proyecto.idat.entity.FondoInicialEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FondoMapper {
	private String id;
	private FondoView empleado;
	private LocalTime hora;
	private LocalDate fecha;
	private BigDecimal billetes;
	private BigDecimal monedas;
	private BigDecimal total;
	private Boolean estado;
	private String comentario;

	public FondoMapper(FondoInicialEntity fondoInicial) {
		this(fondoInicial.getId(),
				new FondoView(fondoInicial.getEmpleado().getId(),
						fondoInicial.getEmpleado().getNombre() + " " + fondoInicial.getEmpleado().getApellido(), fondoInicial.getEmpleado().getDni()),
				fondoInicial.getHora(), fondoInicial.getFecha(),
				fondoInicial.getBilletes(), fondoInicial.getMonedas(), fondoInicial.getTotal(),
				fondoInicial.getEstado(), fondoInicial.getComentario());
	}

}

@AllArgsConstructor
@Getter
class FondoView {
	String id;
	String nombre_completo;
	String dni;
}