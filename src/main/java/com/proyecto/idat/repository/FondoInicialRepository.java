package com.proyecto.idat.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.FondoInicialEntity;

public interface FondoInicialRepository extends JpaRepository<FondoInicialEntity, String> {

	@Query("SELECT c FROM FondoInicialEntity c WHERE c.id = :search")
	FondoInicialEntity findSearchFondoInicial(@Param("search") String search);

	@Query("SELECT c FROM FondoInicialEntity c WHERE (c.estado = true AND :estado = 'active') OR (c.estado = false AND :estado = 'inactive') OR :estado = 'all'")
	Collection<FondoInicialEntity> findByAllEstado(@Param("estado") String estado);

	@Query("SELECT c FROM FondoInicialEntity c ORDER BY c.fecha DESC, c.hora DESC LIMIT 1")
	FondoInicialEntity findUltimoFondo();
	
}
