package com.proyecto.idat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.ProductoEntity;



public interface ProductoRepository extends JpaRepository<ProductoEntity, String> {
 
    @Query("SELECT c FROM ProductoEntity c WHERE (c.estado = true AND :estado = 'active') OR (c.estado = false AND :estado = 'inactive') OR :estado = 'all'")
    List<ProductoEntity> findByAllEstado(@Param("estado") String estado);
	
    @Query("SELECT c FROM ProductoEntity c WHERE c.id = :search or c.nombre = :search")
	ProductoEntity findSearchProducto(@Param("search") String search);
}
