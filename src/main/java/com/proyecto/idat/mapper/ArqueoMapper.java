package com.proyecto.idat.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.proyecto.idat.entity.ArqueoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArqueoMapper {
	private String id;
	private FondoInicialView fondoInicial;
	private LocalTime hora;
	private LocalDate fecha;
	private BigDecimal billetes;
	private BigDecimal monedas;
	private BigDecimal total;
	private Boolean estado;
	private String comentario;

	public ArqueoMapper(ArqueoEntity arqueEntity) {
		this(arqueEntity.getId(),
				new FondoInicialView(
					arqueEntity.getFondoInicial().getId(),
					new EmpleadoView(arqueEntity.getFondoInicial().getEmpleado().getId(),arqueEntity.getFondoInicial().getEmpleado().getNombre() + " " + 
					arqueEntity.getFondoInicial().getEmpleado().getApellido()),
					arqueEntity.getFondoInicial().getHora(),
					arqueEntity.getFondoInicial().getFecha(),
					arqueEntity.getFondoInicial().getBilletes(),
					arqueEntity.getFondoInicial().getMonedas(),
					arqueEntity.getFondoInicial().getTotal(),
					arqueEntity.getFondoInicial().getComentario()
				),
				arqueEntity.getHora(), arqueEntity.getFecha(),
				arqueEntity.getBilletes(), arqueEntity.getMonedas(), arqueEntity.getTotal(),
				arqueEntity.getEstado(), arqueEntity.getComentario());
	}

}

@AllArgsConstructor
@Getter
class FondoInicialView {
	String id;
	EmpleadoView empleado;
	LocalTime hora;
	LocalDate fecha;
	private BigDecimal billetes;
	private BigDecimal monedas;
	private BigDecimal total;
	private String comentario;
}

@AllArgsConstructor
@Getter
class EmpleadoView {
	String id;
	String nombre;
	
} 