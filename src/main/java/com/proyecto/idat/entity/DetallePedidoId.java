package com.proyecto.idat.entity;


import lombok.Data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;



@Data
@Embeddable

public class DetallePedidoId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "cab_pedido_id")
	private String cab_pedido_id;
	@Column(name = "producto_id")
	private String producto_id;

	public DetallePedidoId(String cab_pedido_id, String producto_id) {
		this.cab_pedido_id = cab_pedido_id;
		this.producto_id = producto_id;
	}

	public DetallePedidoId() {
		
	}

	
	

}
