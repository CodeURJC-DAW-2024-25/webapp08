package com.webapp08.pujahoy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
//import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Usuario;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByDatos(String id);
    Page<Producto> findByVendedor_Nombre(String nombre, Pageable pageable);
}
