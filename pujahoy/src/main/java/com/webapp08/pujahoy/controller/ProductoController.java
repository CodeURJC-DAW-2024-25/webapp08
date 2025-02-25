package com.webapp08.pujahoy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.service.ProductoService;
import com.webapp08.pujahoy.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ProductoController {

    @Autowired
    private ProductoService ProductoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/seller/{{id_vendedor}}") 
    public String Seller(Model model, @PathVariable String id_vendedor){
        Optional<Usuario> user = usuarioService.findById(id_vendedor);
        
        if (user.isPresent()) {
            model.addAttribute("Usuario",user.get());
			model.addAttribute("nombre", user.get().getNombre());
            model.addAttribute("nombreVisible", user.get().getNombreVisible());
            model.addAttribute("reputacion", user.get().getReputacion());
            model.addAttribute("contacto", user.get().getContacto());
            model.addAttribute("descripcion", user.get().getDescripcion());
			return "profile";
		} else {
			return "error"; //Falta crear página error
		}
		
    }
    @PostMapping("/product/{{id_producto}}/delete")
    public String postMethodName(@PathVariable long id_producto) {
        Optional<Producto> product = ProductoService.findById(id_producto);
        
        if (product.isPresent()) {
            //Producto.vendedor_id. borramos el producto del vendedro
            ProductoService.DeleteById(id_producto);
			return "/";
		} else {
			return "error"; //Falta crear página error
		}
        
    }
    

}
