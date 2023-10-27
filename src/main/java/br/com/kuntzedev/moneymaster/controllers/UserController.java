package br.com.kuntzedev.moneymaster.controllers;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.controllers.events.OnRegistrationCompleteEvent;
import br.com.kuntzedev.moneymaster.dtos.UserBasicDTO;
import br.com.kuntzedev.moneymaster.dtos.UserDTO;
import br.com.kuntzedev.moneymaster.dtos.UserInsertDTO;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.entities.tokens.VerificationToken;
import br.com.kuntzedev.moneymaster.services.TokenService;
import br.com.kuntzedev.moneymaster.services.UserService;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidTokenException;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private Environment env;
	
	private static String FRONT_APP_URL = "";
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
		return ResponseEntity.ok().body(userService.findUserById(id));
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(userService.findUserByEmail(email));
	}
	
	@PostMapping
	public ResponseEntity<UserBasicDTO> insert(@Valid @RequestBody UserInsertDTO dto, HttpServletRequest request, Errors errors) {
		UserBasicDTO user = userService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getId())
				.toUri();
		
		String appUrl = getAppUrl(request);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
		return ResponseEntity.created(uri).body(user);
	}
	
	@GetMapping(value = "/confirm")
	public ResponseEntity<Void> confirm(WebRequest request, @RequestParam("token") String token) {
		VerificationToken verificationToken = tokenService.findVerificationTokenByToken(token);
		
		User user = verificationToken.getUser();
		Calendar calendar = Calendar.getInstance();
		
		if((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
			throw new InvalidTokenException();
		}
		
		userService.enableRegisteredUser(user);
		
		if(Arrays.asList(env.getActiveProfiles()).contains("test") || Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			FRONT_APP_URL = "http://localhost:3000";
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(FRONT_APP_URL + "/confirmRegistration"));
		return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
	}
	
	private String getAppUrl(HttpServletRequest request) {
		String aux = request.getRequestURL().toString();
		return aux.substring(0, StringUtils.ordinalIndexOf(aux, "/", 3));
	}
}