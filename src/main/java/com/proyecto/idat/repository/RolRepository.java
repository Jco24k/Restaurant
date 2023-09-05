package com.proyecto.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.proyecto.idat.entity.RolEntity;



public interface RolRepository extends JpaRepository<RolEntity, String> {
 

    @Query("SELECT c FROM RolEntity c WHERE c.id = :search or c.nombre = :search")
	RolEntity findSearchRol(@Param("search") String search);


	@Query("SELECT c FROM RolEntity c WHERE (c.estado = true AND :estado = 'activo') "+
    "OR (c.estado = false AND :estado = 'inactivo') OR :estado = 'all'")
    Page<RolEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);

    @Query("SELECT COUNT(r) = :countIds FROM RolEntity r WHERE r.id IN :ids")
    boolean allExistByIds(@Param("ids") List<String> ids, @Param("countIds") int countIds);
}