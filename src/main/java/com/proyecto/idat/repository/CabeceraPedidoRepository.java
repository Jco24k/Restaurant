package com.proyecto.idat.repository;


import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.idat.dtos.ReporteMesDto;
import com.proyecto.idat.entity.CabeceraPedidoEntity;



public interface CabeceraPedidoRepository extends JpaRepository<CabeceraPedidoEntity, String> {
 

	@Query("SELECT c FROM CabeceraPedidoEntity c WHERE (c.estado = :estado) OR :estado = 'all'")
    Page<CabeceraPedidoEntity> findByAllEstado(@Param("estado") String estado,Pageable pageable);

    @Query(value = "SELECT " +
               "(CASE " +
               "    WHEN EXISTS (SELECT 1 FROM Clientes WHERE id = :cliente) " +
               "         AND EXISTS (SELECT 1 FROM Empleados WHERE id = :empleado) " +
               "         AND EXISTS (SELECT 1 FROM Mesas WHERE id = :mesa) " +
               "    THEN 1 " +
               "    ELSE 0 " +
               "END) AS existencia_registros", nativeQuery = true)
    Boolean validateForeignKey(@Param("cliente") String cliente,
                                   @Param("empleado") String empleado,
                                   @Param("mesa") String mesa);



    @Query("SELECT cab FROM CabeceraPedidoEntity cab  left join cab.fondoInicial fi left join cab.comprobante comp WHERE fi.fecha  = :fecha and cab.comprobante is not null")
    Page<CabeceraPedidoEntity> findAllByDate(@Param("fecha") LocalDate fecha, Pageable pageable);

    @Query("SELECT fi.fecha as fecha, SUM(cab.total) as total, count(*) as quantity FROM CabeceraPedidoEntity cab  left join cab.fondoInicial fi left join cab.comprobante comp WHERE YEAR(fi.fecha)  = :age  and MONTH(fi.fecha) =:month and cab.comprobante is not null GROUP BY fi.fecha")
    Page<ReporteMesDto> findAllMountYear(@Param("age") int age,@Param("month") int month, Pageable pageable);

}
