package br.com.kuntzedev.moneymaster.services;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.UserBasicDTO;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.entities.tokens.PasswordRecoveryToken;
import br.com.kuntzedev.moneymaster.entities.tokens.VerificationToken;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.repositories.tokens.PasswordRecoveryTokenRepository;
import br.com.kuntzedev.moneymaster.repositories.tokens.VerificationTokenRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidTokenException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class TokenService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
	
	private static final String RNFE = "Resource not found in the database.";
	private static final String NULL_PARAM = "Null parameter.";
	private static final String ITE = "The token is ";
	private static final String ITE_EXPIRED = "expired";
	
	@Transactional(readOnly = true)
	public VerificationToken findVerificationTokenByToken(String token) {
		Optional<VerificationToken> tk = verificationTokenRepository.findByToken(token);
		
		if(tk.isEmpty()) {
			throw new ResourceNotFoundException(token + ": " + RNFE);
		}
		
		return tk.get();
	}
	
	@Transactional(readOnly = true)
	public PasswordRecoveryToken findPasswordRecoveryTokenByToken(String token) {
		Optional<PasswordRecoveryToken> tk = passwordRecoveryTokenRepository.findByToken(token);
		
		if(tk.isEmpty()) {
			throw new ResourceNotFoundException(token + ": " + RNFE);
		}
		
		return tk.get();
	}
	
	@Transactional(readOnly = true)
	public User findUserByRecoveryToken(String token) {
		User user = userRepository.findByRecoveryToken(token);
		if(user == null) {
			throw new ResourceNotFoundException("Could not find user by the provided token.");
		}
		
		return user;
	}
	
	@Transactional
	public void insertVerificationToken(UserBasicDTO dto, String token) {
		if(dto != null && token != null) {
			User user = userRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found while saving the verification token."));
			VerificationToken newToken = new VerificationToken(user, token);
			verificationTokenRepository.save(newToken);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void insertPasswordRecoveryToken(UserBasicDTO dto, String token) {
		if(dto != null && token != null) {
			User user = userRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found while saving the password recovery token."));
			PasswordRecoveryToken newToken = new PasswordRecoveryToken(user, token);
			passwordRecoveryTokenRepository.save(newToken);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public boolean deleteVerificationToken(String token) {
		int rows = verificationTokenRepository.deleteVerificationTokenByTokenNumber(token);
		if(rows == 0) {
			return false;
		}
		
		return true;
	}
	
	@Transactional
	public boolean deleteRecoveryToken(String token) {
		int rows = passwordRecoveryTokenRepository.deleteRecoveryTokenByTokenNumber(token);
		if(rows == 0) {
			return false;
		}
		
		return true;
	}
	
	public void validatePasswordRecoveryToken(String token) {
		Optional<PasswordRecoveryToken> tk = passwordRecoveryTokenRepository.findByToken(token);
		
		if(!tk.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			boolean expired = tk.get().getExpiryDate().before(calendar.getTime());
			
			if(tk.isEmpty()) {
				throw new ResourceNotFoundException(RNFE);
			}
			
			if(expired) {
				throw new InvalidTokenException(ITE + ITE_EXPIRED);
			}
		} else {
			throw new ResourceNotFoundException("Invalid token");
		}
	}
}