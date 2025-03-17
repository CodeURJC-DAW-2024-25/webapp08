package com.webapp08.pujahoy.controller;

import com.webapp08.pujahoy.dto.PublicUserDTO;
import com.webapp08.pujahoy.dto.RatingDTO;
import com.webapp08.pujahoy.model.Product;
import com.webapp08.pujahoy.model.Rating;
import com.webapp08.pujahoy.model.UserModel;
import com.webapp08.pujahoy.service.ProductService;
import com.webapp08.pujahoy.service.RatingService;
import com.webapp08.pujahoy.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.net.URI;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{id}")
    public PublicUserDTO getUserById(@PathVariable Long id) { //Get user by id
        return userService.findUser(id);
    }

    
    @GetMapping("/{id}/image")//Get user image
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {

		Resource postImage = userService.getPostImage(id);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				.body(postImage);

	}

    @PutMapping("/{id}") //Update user
	public PublicUserDTO replacePost(@PathVariable long id, @RequestBody PublicUserDTO updatedUserDTO) throws SQLException {

		return userService.replaceUser(id, updatedUserDTO);
	}

    public void updateRating(UserModel user) { // Responsible for updating the reputation of a user
        List<Rating> ratings = ratingService.findAllBySeller(user);
        if (ratings.isEmpty()) {
            return; 
        }
        int amount = 0;
        for (Rating val : ratings) {
            amount += val.getRating();
        }
        double mean = (double) amount / ratings.size();

        user.setReputation(mean);
        userService.save(user);
    }

    @PostMapping("/{user_id}/products/{product_id}/ratings") //Rate user
	public ResponseEntity<RatingDTO> rateProduct(@PathVariable long user_id, @PathVariable long product_id, @RequestBody RatingDTO RatingDTO) {

        Optional<UserModel> user = userService.findById(RatingDTO.getIdSeller());
        Optional<Product> product = productService.findById(RatingDTO.getIdProduct());

        if (user.isPresent() && product.isPresent() && user.get().getId() == product.get().getSeller().getId() && RatingDTO.getRating() >= 1 && RatingDTO.getRating() <= 5 && user.get().getId() == user_id && product.get().getId() == product_id) { //User is the seller of the product
            Optional<Rating> test1 = ratingService.findById(RatingDTO.getId());
            Optional<Rating> test2 = ratingService.findByProduct(productService.findById(RatingDTO.getIdProduct()).get());
            if (test1.isPresent() || test2.isPresent()) {
                return ResponseEntity.badRequest().build(); //Id for the new Rating already exists or the product is already rated
            }
            RatingDTO = ratingService.createRating(RatingDTO);
            this.updateRating(user.get());
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(RatingDTO.getId()).toUri();

            return ResponseEntity.created(location).body(RatingDTO);
        } else { //User is not the seller of the product
            return ResponseEntity.badRequest().build();
        }
	}
}