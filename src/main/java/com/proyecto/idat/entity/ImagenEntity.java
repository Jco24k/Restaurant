package com.proyecto.idat.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name = "imagenes")
public class ImagenEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Column(name = "nombre", nullable = false,unique = true)
    @NotNull(message = "nombre must not be null")
    @Size(min = 1)
    private String nombre;


    @Column(name = "url", nullable = false,unique = true)
    @NotNull(message = "url must not be null")
    @Size(min = 1)
    private String url;
    
    @Column(name = "public_id", nullable = false,unique = true)
    @NotNull(message = "public_id must not be null")
    @Size(min = 1)
    private String pubic_id;


    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
    }

	
}
