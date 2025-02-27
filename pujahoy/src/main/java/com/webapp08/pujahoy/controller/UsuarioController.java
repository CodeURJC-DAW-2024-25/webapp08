package com.webapp08.pujahoy.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import com.webapp08.pujahoy.model.Usuario;
import com.webapp08.pujahoy.service.UsuarioService;
import com.webapp08.pujahoy.model.Producto;
import com.webapp08.pujahoy.model.Transaccion;
import com.webapp08.pujahoy.service.ProductoService;
import com.webapp08.pujahoy.model.Valoracion;
import com.webapp08.pujahoy.service.ValoracionService;
//import com.webapp08.pujahoy.model.Transaccion;
import com.webapp08.pujahoy.service.TransaccionService;

import org.springframework.ui.Model;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ValoracionService valoracionService;

    @Autowired
    private TransaccionService transaccionService;

    //Para ver perfil falta el contacto q se saca de Auth0

    @GetMapping("/usuario") //Cuando acceden a su perfil
    public String verTuPerfilUsuario(Model model,  HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            String username = principal.getName(); // Obtiene el nombre de usuario
            Optional<Usuario> user = usuarioService.findByNombre(username); // Busca en la base de datos
            if (user.isPresent()) {
                model.addAttribute("Usuario",user.get());
                model.addAttribute("id",user.get().getId());
                model.addAttribute("nombre", user.get().getNombre());
                model.addAttribute("nombreVisible", user.get().getNombreVisible());
                model.addAttribute("reputacion", user.get().getReputacion());
                model.addAttribute("contacto", user.get().getContacto());
                model.addAttribute("descripcion", user.get().getDescripcion());
                model.addAttribute("admin", false);
                if (!user.get().isActivo()) { //Si esta baneado
                    model.addAttribute("baneado", true);
                    model.addAttribute("registrado", false);
                } else {
                    model.addAttribute("baneado", false);
                    model.addAttribute("registrado", true);
                }
                return "profile";
            } else {
                model.addAttribute("texto", "el usuario no existe");
            }
        }
		model.addAttribute("texto", "usted no esta autenticado");
        return "pageError";
    }

    @GetMapping("/usuario/{id}") //El id es el del producto
    //Doy por hecho q el valor asociado a la sesión es el id del usuario
    public String verPerfilAjeno(Model model, @PathVariable String id, HttpServletRequest request) {
        Optional<Producto> product = productoService.findByDatos(id);
		if (product.isPresent()) {
            Optional<Usuario> vendedor = usuarioService.findByProductos(product.get());
            if (vendedor.isPresent()){
                Principal principal = request.getUserPrincipal();
                Optional<Usuario> user;
                String tipo;
                if (principal != null) { //Si esta autenticado
                    String username = principal.getName(); // Obtiene el nombre de usuario
                    user = usuarioService.findByNombre(username); // Busca en la base de datos
                    tipo = user.get().determinarTipoUsuario();
                    if (user.get().getId() == vendedor.get().getId()){ //Si el perfil es el suyo propio
                        return "redirect:/usuario";
                    }
                } else {
                    user = null;
                    tipo = "";
                }
                model.addAttribute("registrado", false);
                model.addAttribute("baneado", false);
                model.addAttribute("Usuario",vendedor.get());
                model.addAttribute("id",vendedor.get().getId());
                model.addAttribute("nombre", vendedor.get().getNombre());
                model.addAttribute("nombreVisible", vendedor.get().getNombreVisible());
                model.addAttribute("reputacion", vendedor.get().getReputacion());
                model.addAttribute("contacto", vendedor.get().getContacto());
                model.addAttribute("descripcion", vendedor.get().getDescripcion());
                if (tipo.equals("Administrador")) {
                    model.addAttribute("admin", true);
                } else{ //Usuario registrado
                    model.addAttribute("admin", false);
                }
                return "profile";
            } else {
                model.addAttribute("texto", "el vendedor no existe");
            }
		} else {
            model.addAttribute("texto", "el prodcuto no existe");
		}
        return "pageError";
    }

    @GetMapping("/usuario/editar")
    public String editarUsuario(){

        return "editProfile";
    }

    @PostMapping("/usuario/{id}/banear")
	public String deletePost(Model model, @PathVariable String id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        System.out.println("0");
        if (principal != null){
            Optional<Usuario> admin = usuarioService.findByNombre(principal.getName());
            System.out.println("1");
            Optional<Usuario> user = usuarioService.findById(Long.parseLong(id));
            System.out.println("2");
            String tipo = admin.get().determinarTipoUsuario();
            System.out.println("3");
            if (user.isPresent() && tipo.equals("Administrador")) {
                user.get().setActivo(false);
                System.out.println("4");
                usuarioService.save(user.get());
                System.out.println("5");
                return "bannedProfile";
            } else if (!tipo.equals("Administrador")) {
                model.addAttribute("texto", "no tienes permisos para banear a un usuario");
            } else {
                model.addAttribute("texto", "el usuario no existe");
            }
        } else {
            model.addAttribute("texto", "usted no esta autenticado");
        }
        return "pageError";
	}

    @GetMapping("/usuario/NuevoProducto")
    public String nuevoProducto(){

        return "newAuction";
    }

    @GetMapping("/usuario/verProductos")
    public String verProductos(){

        return "YourAuctions";
    }

    @GetMapping("/usuario/verCompras")
    public String verCompras(){

        return "YourWinningBids"; 
    }

    @GetMapping("/usuario/{id}/valorar") //Te envia a la pagina de valorar
    public String irValorar(Model model, @PathVariable long id, HttpSession sesion){
        Optional<Producto> product = productoService.findById(id);
        if (product.isPresent()) {
            Optional<Transaccion> trans = transaccionService.findByProducto_id(id);
            Optional<Usuario> user = usuarioService.findById(trans.get().getComprador().getId());
            Optional<Usuario> user1 = usuarioService.findById((Long) sesion.getAttribute("id"));
            if (user.isPresent() && user1.isPresent()) {
                if (user.get().determinarTipoUsuario().equals("Usuario Registrado") && user1.get().getId().equals(user.get().getId())) {
                    model.addAttribute("id", id);
                    model.addAttribute("imagen", product.get().getImagen());
                    return "ratingProduct";
                } else {
                    model.addAttribute("texto", "este producto no es tuyo");
                    return "pageError";
                }
            } else {
                model.addAttribute("texto", "el usuario comprador no existe");
                return "pageError";
            }
		} else {
            model.addAttribute("texto", "el producto no existe");
            return "pageError";
        } 
    }

    @PostMapping("/usuario/{id}/valorado") //BORRAR PRINCIPIO EN CASO DE COMPROBAR EL FORMULARIO EN EL CLIENTE
    public String valorarProducto(Model model, @PathVariable long id, @RequestParam String comentario, @RequestParam int puntuacion){
        if (puntuacion < 1 || puntuacion > 5) {
            model.addAttribute("texto", "la puntuación debe ser un número entre 1 y 5");
            return "pageError";
        } else if (comentario.length() > 255) {
            model.addAttribute("texto", "el comentario no puede tener más de 255 caracteres");
            return "pageError";
        }
        Optional<Producto> product = productoService.findById(id);
        if (product.isPresent()) {
            Valoracion val = new Valoracion(product.get().getVendedor(),product.get(),puntuacion,comentario);
            valoracionService.save(val);
            return "productRated";
        } else {
            model.addAttribute("texto", "no se encontró el producto");
            return "pageError";
        }
    }
}
