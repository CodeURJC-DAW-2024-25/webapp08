package com.webapp08.pujahoy.service;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import com.webapp08.pujahoy.dto.PublicUserDTO;
import com.webapp08.pujahoy.dto.UserMapper;
import com.webapp08.pujahoy.model.Product;
import com.webapp08.pujahoy.model.UserModel;
import com.webapp08.pujahoy.repository.UserModelRepository;

@Service
public class UserService {

    @Autowired
	private UserModelRepository repository;

	@Autowired
	private UserMapper mapper;
    
	public PublicUserDTO findUser(Long id) {
		return mapper.toDTO(repository.findById(id).get());
	}

	public Resource getPostImage(long id) throws SQLException {

		UserModel user = repository.findById(id).orElseThrow();

		if (user.getProfilePic() != null) {
			return new InputStreamResource(user.getProfilePic().getBinaryStream());
		} else {
			throw new NoSuchElementException();
		}
	}

	public PublicUserDTO replaceUser(long id, PublicUserDTO updatedPostDTO) throws SQLException {

		UserModel oldPost = repository.findById(id).orElseThrow();
		UserModel updatedPost = mapper.toDomain(updatedPostDTO);
		updatedPost.setId(id);

		if (oldPost.getImage() != null) {

			//Set the image in the updated post
			updatedPost.setProfilePic(BlobProxy.generateProxy(oldPost.getProfilePic().getBinaryStream(),
					oldPost.getProfilePic().length()));
			updatedPost.setImage(oldPost.getImage());
		}
		updatedPost.setRols(oldPost.getRols());
		updatedPost.setProducts(oldPost.getProducts());
		updatedPost.setPass(oldPost.getEncodedPassword());
		updatedPost.setActive(oldPost.isActive());

		repository.save(updatedPost);

		return mapper.toDTO(updatedPost);
	}

    public Optional<UserModel> findById(Long id) {
		return repository.findById(id);
	}

	public void save(UserModel user) {
		repository.save(user);
	}

	public Optional<UserModel> findByName(String name) {
		return repository.findByName(name);
	}

	public Optional<UserModel> findByProducts(Product product){
		return repository.findByProducts(product);
	}
	
}
