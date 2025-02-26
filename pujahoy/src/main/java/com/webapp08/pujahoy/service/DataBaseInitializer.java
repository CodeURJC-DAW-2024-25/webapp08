package com.webapp08.pujahoy.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.repository.UsuarioRepository;
import com.webapp08.pujahoy.repository.ProductoRepository;

import jakarta.annotation.PostConstruct;


@Service
public class DataBaseInitializer {

    @Autowired
	private UsuarioRepository userRepository;
	 @Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private ProductoRepository productoRepository;

    @PostConstruct
	public void init() throws IOException, URISyntaxException {
			Usuario user1 = new Usuario("Juan", "Juanito", 100, "Descripción de Juan", "juan@example.com", passwordEncoder.encode("password123"), true, new ArrayList<>(List.of("USER"))); 
			Usuario user2 =  new Usuario("Maria", "Mari", 200, "Descripción de Maria", "maria@example.com", passwordEncoder.encode("securepass456"), true, new ArrayList<>(List.of("ADMIN")));

			long millis = System.currentTimeMillis(); // Obtener el tiempo actual en milisegundos
        	Date sqlDate = new Date(millis);

			Producto p1 = new Producto("Producto 1", sqlDate, sqlDate, "En venta", null, user1);
			Producto p2 = new Producto("Producto 2", sqlDate, sqlDate, "En venta", null, user1);

			
			user1.addProducto(p1);
			user1.addProducto(p2);

			userRepository.save(user1);
			userRepository.save(user2);

			productoRepository.save(p1);
			productoRepository.save(p2);

	}
}
