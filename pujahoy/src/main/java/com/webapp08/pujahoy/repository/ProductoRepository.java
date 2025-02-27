package com.webapp08.pujahoy.repository;

import java.util.List;
import java.util.Optional;

//import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Usuario;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByDatos(String id);
    List<Producto> findByVendedor_Nombre(String usuario);
}
