package com.proyecto.idat.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.ArqueoEntity;

public interface ArqueoRepository extends JpaRepository<ArqueoEntity, String> {

	@Query("SELECT c FROM ArqueoEntity c WHERE c.id = :search")
	ArqueoEntity findSearchArqueo(@Param("search") String search);

	@Query("SELECT c FROM ArqueoEntity c WHERE (c.estado = true AND :estado = 'active') OR (c.estado = false AND :estado = 'inactive') OR :estado = 'all'")
	Collection<ArqueoEntity> findByAllEstado(@Param("estado") String estado);
	
}
