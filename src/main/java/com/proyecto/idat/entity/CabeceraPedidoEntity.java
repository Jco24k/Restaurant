package com.proyecto.idat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cab_pedidos")
public class CabeceraPedidoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, length = 36, columnDefinition = "char(36)")
	private String id;

	// @OneToMany(mappedBy = "cab_pedido")
	@JsonIgnoreProperties("cab_pedido")
	@OneToMany(mappedBy = "cab_pedido")
	private List<DetallePedidoEntity> det_pedido;

	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false)
	private EmpleadoEntity empleado;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private ClienteEntity cliente;

	@ManyToOne
	@JoinColumn(name = "fondo_inicial_id")
	private FondoInicialEntity fondoInicial;

	@ManyToOne
	@JoinColumn(name = "mesa_id", nullable = true)
	private MesaEntity mesa;

	@OneToOne(mappedBy = "cab_pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private ComprobanteEntity comprobante;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime hora = LocalTime.now();

	@Column(name = "total", nullable = false, columnDefinition = "Decimal(10,2)")
	private BigDecimal total;

	@Column(name = "estado", nullable = false, columnDefinition = "varchar(15) default 'activo'")
	@Pattern(regexp = "activo|inactivo|finalizado", message = "estado is invalid (activo|inactivo|finalizado )")
	private String estado;

	@Column(name = "forma_pago", nullable = false, columnDefinition = "varchar(10) default 'efectivo'")
	@Pattern(regexp = "efectivo|tarjeta", message = "forma_pago is invalid ( efectivo | tarjeta )")
	private String forma_pago;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID().toString();
		fecha = LocalDate.now();
		if (estado == null) {
			estado = "activo";
		}
		if (forma_pago == null)
			forma_pago = "efectivo";
		if (total == null)
			total = new BigDecimal(0);
		hora = LocalTime.now();
	}

	public void updateCabeceraPedido(CabeceraPedidoEntity cabeceraPedido) {
		if (cabeceraPedido.getComprobante() != null)
			this.setComprobante(cabeceraPedido.getComprobante());
		if (cabeceraPedido.getFecha() != null)
			this.setFecha(cabeceraPedido.getFecha());
		if (cabeceraPedido.getTotal() != null)
			this.setTotal(cabeceraPedido.getTotal());
		if (cabeceraPedido.getForma_pago() != null)
			this.setForma_pago(cabeceraPedido.getForma_pago());
		if (cabeceraPedido.getEstado() != null)
			this.setEstado(cabeceraPedido.getEstado());
		if (cabeceraPedido.getEmpleado() != null)
			this.setEmpleado(cabeceraPedido.getEmpleado());
		if (cabeceraPedido.getMesa() != null)
			this.setMesa(cabeceraPedido.getMesa());
		if (cabeceraPedido.getCliente() != null)
			this.setCliente(cabeceraPedido.getCliente());
		if (cabeceraPedido.getDet_pedido() != null) {
			this.setDet_pedido(cabeceraPedido.getDet_pedido());
		}

	}

	public void setData(ClienteEntity clienteEntity,
			MesaEntity mesaEntity, EmpleadoEntity empleadoEntity,
			BigDecimal total) {
		this.setCliente(clienteEntity);
		this.setMesa(mesaEntity);
		this.setEmpleado(empleadoEntity);
		this.setTotal(total);
	}

}