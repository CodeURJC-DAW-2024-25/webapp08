package com.webapp08.pujahoy.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collections;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import com.webapp08.pujahoy.dto.ProductBasicDTO;
import com.webapp08.pujahoy.dto.ProductDTO;
import com.webapp08.pujahoy.model.Product;
import com.webapp08.pujahoy.model.UserModel;
import com.webapp08.pujahoy.service.ProductService;
import com.webapp08.pujahoy.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    
    @GetMapping("/{id_product}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id_product) {
    Optional<Product> product = productService.findById(id_product);
    if (product.isPresent()) {
        ProductDTO prod=productService.findProduct(id_product);
        return ResponseEntity.ok(prod);
    } else {
        return ResponseEntity.notFound().build();
    }
}

    @GetMapping
    public Page<ProductBasicDTO> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        
        Principal principal = request.getUserPrincipal();
        if(principal != null) {
			Optional<UserModel> useraux = userService.findByName(principal.getName());
            if (useraux.isPresent()) {
                UserModel user= useraux.get();
                
                //isAdmin
                if("Administrator".equalsIgnoreCase(user.determineUserType())){
                    return productService.obtainAllProductOrdersByReputationDTO(page,size);
                }else{//Registered
                    return productService.obtainAllProductOrdersInProgressByReputationDTO(page,size); 
                }
            }
        }
        //Not registered
        return productService.obtainAllProductOrdersInProgressByReputationDTO(page,size);
        
    }

    


    @DeleteMapping("/{id_product}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id_product) {
        Optional<Product> product = productService.findById(id_product);
        if (product.isPresent()) {
            productService.deleteById(id_product);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getPostImage(@PathVariable long id) throws SQLException {
        Resource postImage = productService.getPostImage(id);
        if (postImage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<?> updatePostImage(@PathVariable long id, @RequestParam("image") MultipartFile imageFile,  HttpServletRequest request) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No image uploaded"));
        }

        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Product not found"));
        }

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "User not authenticated"));
        }

        Optional<UserModel> user = userService.findByName(principal.getName());

        Product product = optionalProduct.get();
        UserModel loggedInUser = user.get();

        if (!(product.getSeller().getId().equals(loggedInUser.getId()) || loggedInUser.determineUserType().equals("Administrator"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "You do not have permission to modify this product"));
        }
        try {
            productService.savePostImage(id, imageFile);
            Resource postImage = productService.getPostImage(id);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") 
                .body(postImage);
        } catch (SQLException e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Database error: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error updating image: " + e.getMessage()));
        }
    }

    

    @PostMapping("/{id}/image")
    public ResponseEntity<?> postImage(@PathVariable long id, 
                                    @RequestParam("image") MultipartFile imageFile,
                                    HttpServletRequest request) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No image uploaded"));
        }

        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Product not found"));
        }

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "User not authenticated"));
        }

        Optional<UserModel> user = userService.findByName(principal.getName());

        Product product = optionalProduct.get();
        UserModel loggedInUser = user.get();

        // Verificar que el usuario autenticado sea el dueño del producto
        if (!product.getSeller().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "You do not have permission to upload an image for this product"));
        }

        try {
            productService.savePostImage(id, imageFile);
            Resource postImage = productService.getPostImage(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") 
                    .body(postImage);
        } catch (SQLException e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Database error: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error uploading image: " + e.getMessage()));
        }
    }

    
}
