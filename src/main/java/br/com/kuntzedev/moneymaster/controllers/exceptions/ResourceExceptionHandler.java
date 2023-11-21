package br.com.kuntzedev.moneymaster.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.kuntzedev.moneymaster.services.exceptions.BadFormattingException;
import br.com.kuntzedev.moneymaster.services.exceptions.EmailException;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidPasswordException;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidTokenException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceAlreadyExistsException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;
import br.com.kuntzedev.moneymaster.services.exceptions.UserAlreadyEnabledException;
import br.com.kuntzedev.moneymaster.services.exceptions.UserAlreadyExistsException;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.InvalidLinkException;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.ScrapingConnectionException;

@RestControllerAdvice
public class ResourceExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.NOT_FOUND;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getLocalizedMessage());
		err.setError("Resource could not be found");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<StandardError> userAlreadyExists(UserAlreadyExistsException e, HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.CONFLICT;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getLocalizedMessage());
		err.setError("Resource already exists");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> constrainViolation(ConstraintViolationException e, HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Violation in the resource validation");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<StandardError> emailException(EmailException e, HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.INTERNAL_SERVER_ERROR;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Email error");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(BadFormattingException.class)
	public ResponseEntity<StandardError> badFormatting(BadFormattingException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.INTERNAL_SERVER_ERROR;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Internal error: bad formatting");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(UnprocessableRequestException.class)
	public ResponseEntity<StandardError> unprocessableRequest(UnprocessableRequestException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Unprocessable request");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<StandardError> invalidPassword(InvalidPasswordException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Invalid password");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<StandardError> invalidToken(InvalidTokenException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Invalid token");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(UnauthorizedUserException.class)
	public ResponseEntity<StandardError> unauthorizedUser(UnauthorizedUserException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.UNAUTHORIZED;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("No authorization");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(UserAlreadyEnabledException.class)
	public ResponseEntity<StandardError> userAlreadyEnabled(UserAlreadyEnabledException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Already enabled");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(ScrapingConnectionException.class)
	public ResponseEntity<StandardError> amazonConnection(ScrapingConnectionException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st;
		if(e.getStatus() == null) {
			st = HttpStatus.INTERNAL_SERVER_ERROR;
		} else {
			st = HttpStatus.valueOf(e.getStatus());
		}
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Scraping exception");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(InvalidLinkException.class)
	public ResponseEntity<StandardError> linkException(InvalidLinkException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Scraping exception");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<StandardError> resourceAlreadyExists(ResourceAlreadyExistsException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		HttpStatus st = HttpStatus.CONFLICT;
		err.setTimestamp(Instant.now());
		err.setStatus(st.value());
		err.setMessage(e.getMessage());
		err.setError("Resource already exists");
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(st).body(err);
	}
}