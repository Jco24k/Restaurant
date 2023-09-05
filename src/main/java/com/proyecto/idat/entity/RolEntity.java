package com.proyecto.idat.entity;



import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import java.util.UUID;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Data
public class RolEntity implements Serializable{
	 private static final long serialVersionUID = 1L;
	 	
	    @Id
	    private String id;
	    

	    @Enumerated(EnumType.STRING)
	    private ERole nombre;
	        
	    
		@Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
		private Boolean estado;

	    @PrePersist
	    public void prePersist() {
	        id = UUID.randomUUID().toString();
			if(estado == null){
				estado = true;
			}
	    }

		public void update(RolEntity rol) {
		if (rol.getNombre() != null)
			this.setNombre(rol.getNombre());
		if (rol.getEstado() != null)
			this.setEstado(rol.getEstado());
	}
		
	    
}