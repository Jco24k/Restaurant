package com.proyecto.idat.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.proyecto.idat.entity.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class CabeceraPedidoDto extends CabeceraPedidoEntity{

	private static final long serialVersionUID = 1L;
	
	private String id;

	private List<DetallePedidoEntity> det_pedido;
	private EmpleadoEntity empleado;
	private ClienteEntity cliente;
	private MesaEntity mesa;
	private ComprobanteEntity comprobante;
	private LocalDate fecha;
	private BigDecimal total;
	private String estado;
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
		if(total == null) total = new BigDecimal(0);
	}

	public void updateCabeceraPedido(CabeceraPedidoDto cabeceraPedido) {
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
