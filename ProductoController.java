package com.webapp08.pujahoy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.model.Review;
import com.webapp08.pujahoy.service.ProductoService;
import com.webapp08.pujahoy.service.UsuarioService;
import com.webapp08.pujahoy.service.ReviewService;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/seller/{id_vendedor}") 
    public String Seller(Model model, @PathVariable String id_vendedor) {
        Optional<Usuario> user = usuarioService.findById(id_vendedor);
        
        if (user.isPresent()) {
            model.addAttribute("Usuario", user.get());
            model.addAttribute("nombre", user.get().getNombre());
            model.addAttribute("nombreVisible", user.get().getNombreVisible());
            model.addAttribute("reputacion", user.get().getReputacion());
            model.addAttribute("contacto", user.get().getContacto());
            model.addAttribute("descripcion", user.get().getDescripcion());
            return "profile";
        } else {
            return "error"; // Falta crear página error
        }
    }

    @PostMapping("/product/{id_producto}/delete")
    public String DeleteProduct(@PathVariable long id_producto) {
        Optional<Producto> product = productoService.findById(id_producto);
        
        if (product.isPresent()) {
            productoService.DeleteById(id_producto);
            return "redirect:/";
        } else {
            return "error"; // Falta crear página error
        }
    }

    @PostMapping("/product/{id_producto}/review")
    public String AddReview(@PathVariable long id_producto, 
                            @RequestParam("comentario") String comentario, 
                            @RequestParam("rating") int rating, 
                            @RequestParam("user_id") String user_id) {
        
        Optional<Usuario> user = usuarioService.findById(user_id);
        Optional<Producto> product = productoService.findById(id_producto);
        
        if (user.isPresent() && product.isPresent() && user.get().getTipo().equals("comprador")) {
            Review review = new Review();
            review.setUsuario(user.get());
            review.setProducto(product.get());
            review.setComentario(comentario);
            review.setRating(rating);
            
            reviewService.Save(review);
            return "redirect:/product/" + id_producto;
        } else {
            return "error"; // Falta crear página error
        }
    }
}
