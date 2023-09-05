package com.proyecto.idat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.idat.entity.DetallePedidoEntity;
import com.proyecto.idat.entity.DetallePedidoId;



public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, DetallePedidoId> {
 
}
