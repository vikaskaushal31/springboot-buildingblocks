package com.stacksimplify.restservices.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stacksimplify.restservices.entities.User;
import com.stacksimplify.restservices.exceptions.UserNotFoundException;
import com.stacksimplify.restservices.repositories.UserRepository;
import com.stacksimplify.restservices.services.UserService;

@RestController
@RequestMapping(value = "/hateoas/users")
@Validated
public class UserHateoasController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")

	public User getUserById(@PathVariable("id") @Min(1) Long id) {

	try {

	Optional<User> userOptional = userService.getUserById(id);
	User user = userOptional.get();
	Long userId = user.getId();

	Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserHateoasController.class).getUserById(userId)).withSelfRel();

	user.add(selfLink);

	return user;

	} catch (UserNotFoundException e) {

	     throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

	}

	}
	
	@GetMapping
	public List<User> getAllUsers(){
		
		List<User> allUsers =  userService.getAllUsers();
		for(User user : allUsers) {
			Long userid = user.getId();
			Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserHateoasController.class).getUserById(userid)).withSelfRel();
			user.add(selfLink);
		}
		return allUsers;
	}
}
