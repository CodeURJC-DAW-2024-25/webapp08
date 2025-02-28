package com.webapp08.pujahoy.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Transaccion;
import com.webapp08.pujahoy.repository.UsuarioRepository;
import com.webapp08.pujahoy.repository.ProductoRepository;
import com.webapp08.pujahoy.repository.TransaccionRepository;

import jakarta.annotation.PostConstruct;


@Service
public class DataBaseInitializer {

    @Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private TransaccionRepository transaccionRepository;

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
			Producto p12 = new Producto("Producto 12", 2.5,  sqlDate, sqlDate, "En venta", null, user2);

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
			user2.addProductos(p12);

			// Transacciones donde p1 es el comprador (cambiando user1 y user2)
			Transaccion transaccion1 = new Transaccion(p1, user2, user1, p1.getValorIni());
			Transaccion transaccion2 = new Transaccion(p2, user2, user1, p2.getValorIni());
			Transaccion transaccion3 = new Transaccion(p3, user2, user1, p3.getValorIni());
			Transaccion transaccion4 = new Transaccion(p4, user2, user1, p4.getValorIni());
			Transaccion transaccion5 = new Transaccion(p5, user1, user2, p5.getValorIni());
			Transaccion transaccion6 = new Transaccion(p6, user2, user1, p6.getValorIni());
			Transaccion transaccion7 = new Transaccion(p7, user2, user1, p7.getValorIni());
			Transaccion transaccion8 = new Transaccion(p8, user2, user1, p8.getValorIni());
			Transaccion transaccion9 = new Transaccion(p9, user2, user1, p9.getValorIni());
			Transaccion transaccion10 = new Transaccion(p10, user2, user1, p10.getValorIni());
			Transaccion transaccion11 = new Transaccion(p11, user2, user1, p11.getValorIni());

			// Transacción donde p2 es el comprador
			Transaccion transaccion12 = new Transaccion(p12, user1, user2, p12.getValorIni());


			
			userRepository.save(user1);
			userRepository.save(user2);

			transaccionRepository.save(transaccion1);
			transaccionRepository.save(transaccion2);
			transaccionRepository.save(transaccion3);
			transaccionRepository.save(transaccion4);
			transaccionRepository.save(transaccion5);
			transaccionRepository.save(transaccion6);
			transaccionRepository.save(transaccion7);
			transaccionRepository.save(transaccion8);
			transaccionRepository.save(transaccion9);
			transaccionRepository.save(transaccion10);
			transaccionRepository.save(transaccion11);
			transaccionRepository.save(transaccion12);

			
	}
}
