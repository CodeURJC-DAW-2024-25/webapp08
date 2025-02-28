package com.webapp08.pujahoy.controller;

import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.sql.Blob;
import java.sql.Date;

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

    @GetMapping("/producto_template")
    public String verProductos(Model model, HttpServletRequest request,
                            @RequestParam(defaultValue = "0") int pagina,
                            @RequestParam(defaultValue = "10") int tamaño) {
        Principal principal = request.getUserPrincipal();
        
        if (principal != null) {
            String username = principal.getName(); // Obtener nombre de usuario
            Optional<Usuario> user = usuarioService.findByNombre(username);

            if (user.isPresent()) {
                Page<Producto> productos = productoService.obtenerProductosPaginados(username, pagina, tamaño);
                
                model.addAttribute("productos", productos); // Pasamos la página completa
                return "producto_template";
            }
        }

        model.addAttribute("texto", "Usted no está autenticado");
        return "pageError";
    }

    @GetMapping("/verProductos")
    public String verProductosIni(Model model, HttpServletRequest request,
                            @RequestParam(defaultValue = "0") int pagina,
                            @RequestParam(defaultValue = "10") int tamaño) {
        Principal principal = request.getUserPrincipal();
        
        if (principal != null) {
            String username = principal.getName(); // Obtener nombre de usuario
            Optional<Usuario> user = usuarioService.findByNombre(username);

            if (user.isPresent()) {
                Page<Producto> productos = productoService.obtenerProductosPaginados(username, pagina, tamaño);

                System.out.println(productos);
                System.out.println("ANO");
                
                model.addAttribute("productos", productos); // Pasamos la página completa
                return "YourProducts";
            }
        }

        model.addAttribute("texto", "Usted no está autenticado");
        return "pageError";
}
    

        

    @GetMapping("/NuevoProducto")
        public String nuevoProducto(){
            return "newAuction";
        }

    @PostMapping("/submit_auction")
    public String publicarProducto(
        @RequestParam("datos") String nombre,
        @RequestParam("valorIni") double precio,
        @RequestParam("duracion") int duracion,
        @RequestParam("estado") String estado,
        @RequestParam("imagen") MultipartFile imagenFile,
        HttpServletRequest request,
        Model model) {    
    
        Principal principal = request.getUserPrincipal();
    
        if (principal == null) {
            model.addAttribute("texto", "Usuario no encontrado");
            return "pageError";
        }
    
        Optional<Usuario> usuario = usuarioService.findByNombre(principal.getName());
    
        if (usuario.isEmpty()) {
            model.addAttribute("texto", "Usuario no encontrado en la base de datos");
            return "pageError";
        }
    
        try {
            // Obtener la fecha y hora actual en java.sql.Date
            Date horaIni = new Date(System.currentTimeMillis());
            Date horaFin = new Date(horaIni.getTime() + (long) duracion * 24 * 60 * 60 * 1000);
    
            // Convertir la imagen a Blob si existe
            Blob imagen = null;
            if (!imagenFile.isEmpty()) {
                byte[] imagenBytes = imagenFile.getBytes();
                imagen = new SerialBlob(imagenBytes);
            }
    
            // Crear el producto con el usuario obtenido
            Producto producto = new Producto(nombre, precio, horaIni, horaFin, estado, imagen, usuario.get());
    
            // Guardar el producto en la base de datos
            productoService.save(producto);
    
            model.addAttribute("producto", producto);
            return "redirect:/usuario/producto/" + producto.getId();
            
        } catch (Exception e) {
            model.addAttribute("texto", "Error al procesar el producto: " + e.getMessage());
            return "pageError";
        }
    }

    @GetMapping("/producto_template_compras")
    public String verProductosCompras(Model model, HttpServletRequest request,
                            @RequestParam(defaultValue = "0") int pagina,
                            @RequestParam(defaultValue = "10") int tamaño) {
        Principal principal = request.getUserPrincipal();
        
        if (principal != null) {
            String username = principal.getName(); // Obtener nombre de usuario
            Optional<Usuario> user = usuarioService.findByNombre(username);

            if (user.isPresent()) {
                Page<Producto> productos = productoService.obtenerProductosComprados(username, pagina, tamaño);
                
                model.addAttribute("productos", productos); // Pasamos la página completa
                return "producto_template";
            }
        }

        model.addAttribute("texto", "Usted no está autenticado");
        return "pageError";
    }
        

    @GetMapping("/verCompras")
    public String verProductosComprasIni(Model model, HttpServletRequest request,
                            @RequestParam(defaultValue = "0") int pagina,
                            @RequestParam(defaultValue = "10") int tamaño) {
        Principal principal = request.getUserPrincipal();
        
        if (principal != null) {
            String username = principal.getName(); // Obtener nombre de usuario
            Optional<Usuario> user = usuarioService.findByNombre(username);

            if (user.isPresent()) {
                Page<Producto> productos = productoService.obtenerProductosComprados(username, pagina, tamaño);

                System.out.println(productos);
                
                model.addAttribute("productos", productos); // Pasamos la página completa
                return "YourWinningBids";
            }
        }

        model.addAttribute("texto", "Usted no está autenticado");
        return "pageError";
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
