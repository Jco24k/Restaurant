package com.proyecto.idat.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.proyecto.idat.entity.EmpleadoEntity;




public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, String> {
 
    @Query("SELECT c FROM EmpleadoEntity c WHERE (c.estado = true AND :estado = 'active') OR (c.estado = false AND :estado = 'inactive') OR :estado = 'all'")
    List<EmpleadoEntity> findByAllEstado(@Param("estado") String estado);
	
    @Query("SELECT c FROM EmpleadoEntity c WHERE c.id = :search or c.nombre = :search")
    EmpleadoEntity findSearchEmpleado(@Param("search") String search);
    
    Optional<EmpleadoEntity> findOneByEmail(String email);
    
    //Optional<EmpleadoEntity> findByUsername(String username);
    
    //@Query("SELECT e FROM EmpleadoEntity e WHERE e.username = ?1")
    //Optional<EmpleadoEntity> getName(String username);
}
