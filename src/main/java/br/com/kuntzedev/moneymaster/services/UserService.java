package br.com.kuntzedev.moneymaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static final String RNFE2 = " was not found.";
	
	@Transactional(readOnly = true)
	public User findUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + RNFE2));
	}
	
	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if(user == null) {
			throw new ResourceNotFoundException("User with email " + email + RNFE2);
		}
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new ResourceNotFoundException("Username " + username + RNFE2);
		}
		
		return user;
	}
}