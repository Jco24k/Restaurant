package com.proyecto.idat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.CategoriaEntity;



public interface CategoriaRepository extends JpaRepository<CategoriaEntity, String> {
 
	@Query("SELECT c FROM CategoriaEntity c WHERE c.id = :search or c.nombre = :search")
	CategoriaEntity findSearchCategoria(@Param("search") String search);

	@Query("SELECT c FROM CategoriaEntity c WHERE (c.estado = true AND :estado = 'active') OR (c.estado = false AND :estado = 'inactive') OR :estado = 'all'")
    List<CategoriaEntity> findByAllEstado(@Param("estado") String estado);

	CategoriaEntity findByNombre(String nombre);
}
