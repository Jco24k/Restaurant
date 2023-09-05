package com.proyecto.idat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.idat.entity.ImagenEntity;



public interface ImagenRepository extends JpaRepository<ImagenEntity, String> {
 
}