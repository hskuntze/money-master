package br.com.kuntzedev.moneymaster.services.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.kuntzedev.moneymaster.dtos.UserDTO;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.UserAlreadyExistsException;

public class VerifyUserExistenceValidator implements ConstraintValidator<VerifyUserExistence, UserDTO>{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void initialize(VerifyUserExistence constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {
		User user = userRepository.findByEmail(dto.getEmail());
		
		if(user != null) {
			throw new UserAlreadyExistsException("User already exists!");
		} else {
			return true;
		}
	}
}
