package com.proyecto.idat.entity;



import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

import java.util.UUID;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mesas")
@Data
public class MesaEntity implements Serializable{
	 private static final long serialVersionUID = 1L;
	 	
	    @Id
	    private String id;
	    

	    @Column(name = "nombre", nullable = false,length = 60,unique = true)
	    @NotBlank(message = "nombre must not be empty")
	    @Size(min = 3, max = 60, message = "nombre must be between 3 to 20 characters")
	    private String nombre;
	    
        @Column(name = "capacidad", nullable = false)
	    @NotNull(message = "capacidad must not be null")
		@Positive()
	    private int capacidad;
	    
	    
	    /*@Column(name = "estado", nullable = false, columnDefinition = "varchar(15) default 'activo'")
	    @Pattern(regexp = "activo|inactivo|mantenimiento", message = "estado is invalid (activo|inactivo|mantenimiento )")
	    private String estado;*/
	    @Column(name = "estado", nullable = false, columnDefinition = "varchar(15) default 'libre'")
	    @Pattern(regexp = "libre|ocupado|reserva|mantenimiento", message = "estado is invalid (libre|ocupado|reserva|mantenimiento )")
	    private String estado;

	    @PrePersist
	    public void prePersist() {
	        id = UUID.randomUUID().toString();
			if(estado == null){
				estado = "activo";
			}
	    }

		
	    
}

