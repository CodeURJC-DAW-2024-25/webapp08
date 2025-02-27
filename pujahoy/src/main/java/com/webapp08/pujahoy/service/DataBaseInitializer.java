package com.webapp08.pujahoy.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;

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
	private ProductoRepository productoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @PostConstruct
	public void init() throws IOException, URISyntaxException {
			Usuario user1 = new Usuario("Juan", 5, "Juanito", "juanElGrande@gmail.com", "descripción prueba arturo", true, passwordEncoder.encode("pass"), "ADMIN");
			Usuario user2 = new Usuario("Pedro", 2, "Pedrito", "pedrosimple@gmail.com", "descripcion", true, passwordEncoder.encode("pass"), "USER");
			Usuario user3 = new Usuario("Pablo", 3, "Pablito", "pablo@gmail.com", "descripcion", true, passwordEncoder.encode("pass"), "USER");

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
	
			long millis = System.currentTimeMillis(); // Obtener el tiempo actual en milisegundos
        	Date sqlDate = new Date(millis);

			Producto p1 = new Producto("Producto 1", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p2 = new Producto("Producto 2", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p3 = new Producto("Producto 3", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p4 = new Producto("Producto 4", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p5 = new Producto("Producto 5", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p6 = new Producto("Producto 6", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p7 = new Producto("Producto 7", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p8 = new Producto("Producto 8", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p9 = new Producto("Producto 9", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p10 = new Producto("Producto 10", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p11 = new Producto("Producto 11", 2.5,  sqlDate, sqlDate, "En venta", null, user1);
			Producto p12 = new Producto("Producto 12", 2.5,  sqlDate, sqlDate, "En venta", null, user1);

			productoRepository.save(p1);
			productoRepository.save(p2);
			productoRepository.save(p3);
			productoRepository.save(p4);
			productoRepository.save(p5);
			productoRepository.save(p6);
			productoRepository.save(p7);
			productoRepository.save(p8);
			productoRepository.save(p9);
			productoRepository.save(p10);
			productoRepository.save(p11);
			productoRepository.save(p12);

			user1.addProductos(p1);
			user1.addProductos(p2);
			user1.addProductos(p3);
			user1.addProductos(p4);
			user1.addProductos(p5);
			user1.addProductos(p6);
			user1.addProductos(p7);
			user1.addProductos(p8);
			user1.addProductos(p9);
			user1.addProductos(p10);
			user1.addProductos(p11);
			user1.addProductos(p12);

			userRepository.save(user1);

			
	}
}
