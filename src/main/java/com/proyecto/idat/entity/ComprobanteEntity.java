package com.proyecto.idat.entity;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comprobantes")
@Data
public class ComprobanteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnore()
    private CabeceraPedidoEntity cab_pedido;

    @NotNull(message = "tipo must not be null")
    @Column(name = "tipo", columnDefinition = "varchar(15) default 'boleta'")
    @Pattern(regexp = "boleta|factura", message = "type is invalid ( boleta | factura )")
    private String tipo;

    @Column(name = "nro_comprobante", nullable = true)
    private Integer nro_comprobante;

    @Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
    private Boolean estado;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
        if (estado == null) {
            estado = true;
        }
        tipo = "boleta";
    }

    public void updateComprobante(ComprobanteEntity comprobante) {
        if (comprobante.getTipo() != null)
            this.setTipo(comprobante.getTipo());
        if (comprobante.getNro_comprobante() != null)
            this.setNro_comprobante(comprobante.getNro_comprobante());
        if (comprobante.getEstado() != null)
            this.setEstado(comprobante.getEstado());
    }

}
