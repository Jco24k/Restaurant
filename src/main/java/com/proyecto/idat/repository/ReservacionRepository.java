package com.proyecto.idat.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.proyecto.idat.entity.ReservacionEntity;



public interface ReservacionRepository extends JpaRepository<ReservacionEntity, String> {
 
     @Query("SELECT c FROM ReservacionEntity c WHERE (c.estado = true AND :estado = 'activo') OR (c.estado = false AND :estado = 'inactivo') OR :estado = 'all'")
    Page<ReservacionEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);



    @Query("SELECT r FROM ReservacionEntity r inner join ClienteEntity c WHERE r.id = :search or c.dni = :search ")
	ReservacionEntity findSearchReservacion(@Param("search") String search);
}
