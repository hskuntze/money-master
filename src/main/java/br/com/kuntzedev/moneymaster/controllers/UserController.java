package br.com.kuntzedev.moneymaster.controllers;

import java.net.URI;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import br.com.kuntzedev.moneymaster.dtos.tokens.TokenPasswordDTO;
import br.com.kuntzedev.moneymaster.services.UserService;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidPasswordException;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private Environment env;

	private static String FRONT_APP_URL = "";
	
	/**
	 * -------------- GETS --------------
	 */

	/**
	 * Resgata um usuário baseado em seu ID
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
		return ResponseEntity.ok().body(userService.findUserById(id));
	}

	/**
	 * Resgata um usuário baseado em seu e-mail
	 * @param email
	 * @return
	 */
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok().body(userService.findUserByEmail(email));
	}

	/**
	 * Função que confirma o registro de determinado usuário
	 * 
	 * @param request
	 * @param token
	 * @return
	 */
	@GetMapping(value = "/confirm")
	public ResponseEntity<Void> confirm(WebRequest request, @RequestParam("token") String token) {
		userService.enableRegisteredUser(token);

		if (Arrays.asList(env.getActiveProfiles()).contains("test")
				|| Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			FRONT_APP_URL = "http://localhost:3000";
		}

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(FRONT_APP_URL + "/confirmregistration"))
				.build();
	}

	/**
	 * Função que redireciona o usuário para a aplicação front-end para realizar a
	 * troca de senha.
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/redirectToChangePassword")
	public ResponseEntity<Void> redirectToChangePassword(@RequestParam("token") String token) {
		userService.validateTokenForRecovery(token);

		if (Arrays.asList(env.getActiveProfiles()).contains("test")
				|| Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			FRONT_APP_URL = "http://localhost:3000";
		}

		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create(FRONT_APP_URL + "/auth/recoverpassword/" + token)).build();
	}
	
	/**
	 * -------------- POSTS --------------
	 */

	/**
	 * Função que registra o usuário e dispara um e-mail de confirmação
	 * 
	 * @param dto
	 * @param request
	 * @param errors
	 * @return
	 */
	@PostMapping(value = "/register")
	public ResponseEntity<UserBasicDTO> insert(@Valid @RequestBody UserInsertDTO dto, HttpServletRequest request,
			Errors errors) {
		UserBasicDTO user = userService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

		String appUrl = getAppUrl(request);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
		return ResponseEntity.created(uri).body(user);
	}

	/**
	 * Função que dispara um e-mail para a recuperação de senha
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/recover")
	public ResponseEntity<Void> sendEmailToRecoverPassword(@RequestParam("email") String email,
			HttpServletRequest request) {
		String appUrl = getAppUrl(request);
		userService.createAndInsertRecoveryToken(email, appUrl);

		return ResponseEntity.ok().build();
	}

	/**
	 * Função que vai realizar a troca de senhas e persistir o usuário com a nova
	 * senha
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/saveChangeOfPassword")
	public ResponseEntity<Void> saveChangeOfPassword(@RequestBody TokenPasswordDTO dto) {
		userService.validateTokenForRecovery(dto.getToken());
		userService.changePasswordWithToken(dto);

		return ResponseEntity.ok().build();
	}

	/**
	 * Função que vai realizar uma troca de senhas de forma simples, sem a necessidade
	 * de tokens.
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @throws InvalidPasswordException
	 */
	@PostMapping(value = "/simplePasswordChange")
	public ResponseEntity<Void> simplePasswordChange(@RequestParam("newPassword") String newPassword,
			@RequestParam("oldPassword") String oldPassword) throws InvalidPasswordException {
		userService.updatePassword(newPassword, oldPassword);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * -------------- PUTS --------------
	 */
	
	/**
	 * Função que realiza a operação de atualização de um usuário
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping(value = "/update/{id}")
	public ResponseEntity<UserBasicDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
		return ResponseEntity.ok().body(userService.update(id, dto));
	}
	
	/**
	 * -------------- DELETES --------------
	 */
	
	/**
	 * Função que exclui por completo um usuário do sistema
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * -------------- PATCHES --------------
	 */
	
	/**
	 * Função que adiciona uma autorização para um determinado usuário
	 * @param userId
	 * @param authId
	 * @return
	 */
	@PatchMapping(value = "/grant/{userId}+{authId}")
	public ResponseEntity<Void> makeAdmin(@PathVariable Long userId, @PathVariable Long authId) {
		userService.grantUserAuthorization(userId, authId);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Função que revoga uma autorização para um determinado usuário
	 * @param userId
	 * @param authId
	 * @return
	 */
	@PatchMapping(value = "/revoke/{userId}+{authId}")
	public ResponseEntity<Void> revokeAdmin(@PathVariable Long userId, @PathVariable Long authId) {
		userService.revokeUserAuthorization(userId, authId);
		return ResponseEntity.ok().build();
	}

	private String getAppUrl(HttpServletRequest request) {
		String aux = request.getRequestURL().toString();
		return aux.substring(0, StringUtils.ordinalIndexOf(aux, "/", 3));
	}
}