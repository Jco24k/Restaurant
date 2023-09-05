package com.proyecto.idat.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.entity.ComprobanteEntity;


public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity, Long> {
 
    @Query("SELECT c FROM ComprobanteEntity c WHERE c.id = :search or c.nro_comprobante = :search")
	ComprobanteEntity findSearchComprobante(@Param("search") String search);

	@Query("SELECT c FROM ComprobanteEntity c WHERE (c.estado = true AND :estado = 'activo') OR (c.estado = false AND :estado = 'inactivo') OR :estado = 'all'")
    Page<ComprobanteEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);
    
}
