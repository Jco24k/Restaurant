package com.proyecto.idat.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.ClienteEntity;



public interface ClienteRepository extends JpaRepository<ClienteEntity, String> {
 
	@Query("SELECT u FROM ClienteEntity u WHERE u.id = :search OR u.email = :search OR u.dni = :search")
	ClienteEntity findSearchEntity(@Param("search") String search);
	
	ClienteEntity findByDniAndEmail(String dniString, String emaiString);

	@Query("SELECT u FROM ClienteEntity u WHERE (u.estado = true AND :estado = 'activo') OR "+
		"(u.estado = false AND :estado = 'inactivo') OR :estado = 'all'")
    Page<ClienteEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);
}