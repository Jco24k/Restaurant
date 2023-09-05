package com.proyecto.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.proyecto.idat.entity.MesaEntity;



public interface MesaRepository extends JpaRepository<MesaEntity, String> {
 

    @Query("SELECT c FROM MesaEntity c WHERE c.id = :search or c.nombre = :search")
	MesaEntity findSearchMesa(@Param("search") String search);


    @Query("SELECT c FROM MesaEntity c WHERE  (c.estado = :estado) OR :estado = 'all'")
    Page<MesaEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);
    
    //listar por estado libre
    //@Query("SELECT m FROM MesaEntity m where m.estado = 'libre'")
    @Query(value ="select * from mesas where estado ='libre'",nativeQuery = true)
    Collection<MesaEntity> findTableByStatusFree();
    
}
