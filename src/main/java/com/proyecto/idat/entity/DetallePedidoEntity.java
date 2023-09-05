package com.proyecto.idat.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
// @Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "det_pedidos")
public class DetallePedidoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    DetallePedidoId id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @MapsId("producto_id")
    private ProductoEntity producto;

    
    @ManyToOne
    @JoinColumn(name = "cab_pedido_id")
    @MapsId("cab_pedido_id")
    private CabeceraPedidoEntity cab_pedido;

    @Column(name = "precio_producto", nullable = false, columnDefinition = "Decimal(10,2)")
    @Positive()
    private BigDecimal precio_producto;

    @Column(name = "cantidad", nullable = false)
    @Positive
    private Integer cantidad;

    public void update(DetallePedidoEntity DetallePedido) {
        if (DetallePedido.getPrecio_producto() != null)
            this.setPrecio_producto(DetallePedido.getPrecio_producto());
        if (DetallePedido.getCantidad() != null)
            this.setCantidad(DetallePedido.getCantidad());
    }

    public void chargeIds(CabeceraPedidoEntity cabeceraPedidoEntity, ProductoEntity productoEntity) {
        this.cab_pedido = cabeceraPedidoEntity;
        this.producto = productoEntity;
        DetallePedidoId ids = new DetallePedidoId(cabeceraPedidoEntity.getId(), productoEntity.getId());
        this.id = ids;
    }

}
