package br.com.kuntzedev.moneymaster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<User> findUserById(@PathVariable Long id) {
		return ResponseEntity.ok().body(userService.findUserById(id));
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(userService.findUserByEmail(email));
	}
}