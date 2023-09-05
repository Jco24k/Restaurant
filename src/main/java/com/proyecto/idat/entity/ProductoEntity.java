package com.proyecto.idat.entity;



import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "productos")
public class ProductoEntity implements Serializable{
	 private static final long serialVersionUID = 1L;
	 	
	    @Id
	    private String id;
	    

	    @Column(name = "nombre", nullable = false,length = 60,unique = true)
	    @NotBlank(message = "nombre must not be empty")
	    @Size(min = 3, max = 60, message = "nombre must be between 3 to 20 characters")
	    private String nombre;
	    
	    @Column(name = "descripcion", nullable = false,length = 600)
	    @NotNull(message = "descripcion must not be null")
	    @Size(min = 3, max = 60, message = "descripcion must be between 3 to 600 characters")
	    private String descripcion;
	    
	    /*@Column(name = "foto", nullable = false, length = 100)
	    @NotNull(message = "foto must not be null")
	    private String foto;*/
	    
        @Column(name = "stock", nullable = false)
	    @NotNull(message = "stock must not be null")
		@Positive()
	    private int stock;
	    
        @Column(name = "precio", nullable = false, columnDefinition="Decimal(10,2)")
	    @NotNull(message = "precio must not be null")
		@Positive()
	    private BigDecimal precio;
	    
	    @Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
	    private Boolean estado;
	    
		@ManyToOne 
		@JoinColumn(name = "categoria_id")
		@NotNull(message = "categoria must not be null")
		private CategoriaEntity categoria;

	    @PrePersist
	    public void prePersist() {
	        id = UUID.randomUUID().toString();
	        if (estado == null) {
	            estado = true;
	        }
	    }
 
}

