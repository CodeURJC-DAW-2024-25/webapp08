package com.webapp08.pujahoy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
	private ProductoRepository repository;
    
    public Optional<Producto> findById(long id) {
		return repository.findById(id);
	}

	public Optional<Producto> findByDatos(String id) {
		return repository.findByDatos(id);
	}	

	public List<Producto> findByVendedor_Nombre(String usuario) {
		return repository.findByVendedor_Nombre(usuario);
	}	


	public Producto save(Producto producto) {
		return repository.save(producto);
	}



}
