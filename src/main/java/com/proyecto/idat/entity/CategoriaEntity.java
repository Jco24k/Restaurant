package com.proyecto.idat.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categorias")
public class CategoriaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Column(name = "nombre", nullable = false,unique = true)
    @NotNull(message = "nombre must not be null")
    @Size(min = 1)
    private String nombre;

    @Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
    private Boolean estado;

    // @OneToMany(mappedBy = "categoria")
    // private List<ProductoEntity> productos;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
        if (estado == null) {
            estado = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // if (estado != null && !estado) {
        //     if(productos!= null){
        //         for (ProductoEntity producto : productos) {
        //             producto.setEstado(false);
        //         }
        //     }
        // }
    }

    public CategoriaEntity(String nombre) {
        this.nombre = nombre;
    }
	
}
