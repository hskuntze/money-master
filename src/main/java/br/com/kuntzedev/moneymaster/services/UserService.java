package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.UserBasicDTO;
import br.com.kuntzedev.moneymaster.dtos.UserDTO;
import br.com.kuntzedev.moneymaster.dtos.UserInsertDTO;
import br.com.kuntzedev.moneymaster.dtos.tokens.TokenPasswordDTO;
import br.com.kuntzedev.moneymaster.entities.Address;
import br.com.kuntzedev.moneymaster.entities.Role;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.entities.Vault;
import br.com.kuntzedev.moneymaster.entities.tokens.VerificationToken;
import br.com.kuntzedev.moneymaster.repositories.RoleRepository;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.DeletionProcedureException;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidPasswordException;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidTokenException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnauthroziedUserException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;
import br.com.kuntzedev.moneymaster.services.exceptions.UserAlreadyEnabledException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VaultService vaultService;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private static final String RNFE2 = " was not found.";
	private static final String NULL_PARAM = "Null parameter.";
	
	@Transactional(readOnly = true)
	public UserDTO findUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + RNFE2));
		return new UserDTO(user);
	}
	
	@Transactional(readOnly = true)
	public UserDTO findUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if(user == null) {
			throw new ResourceNotFoundException("User with email " + email + RNFE2);
		}
		return new UserDTO(user);
	}
	
	@Transactional(readOnly = true)
	public User findReference(Long id) {
		if(id != null) {
			return userRepository.getReferenceById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public UserBasicDTO insert(UserInsertDTO dto) {
		if(dto != null) {
			User user = new User();
			
			dtoToEntity(user, dto);
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user = userRepository.save(user);
			
			Vault vault = initializeEmptyVault();
			vault.setUser(user);
			vaultService.insert(vault);
			
			return new UserBasicDTO(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public UserBasicDTO update(Long id, UserDTO dto) {
		if(id != null && dto != null) {
			User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + RNFE2));
			
			dtoToEntity(user, dto);
			user.setEnabled(user.isEnabled());
			user = userRepository.save(user);
			
			return new UserBasicDTO(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public UserBasicDTO updatePassword(String newPassword, String oldPassword) throws InvalidPasswordException {
		if(newPassword != null && oldPassword != null) {
			User user = authService.authenticated();
			
			if(user == null) {
				throw new UnauthroziedUserException(NULL_PARAM + " Auth failed.");
			}
			
			if(!validateOldPassword(user, oldPassword)) {
				throw new InvalidPasswordException("Invalid password.");
			}
			
			changePassword(user, newPassword);
			return new UserBasicDTO(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void enableRegisteredUser(String token) {
		VerificationToken verificationToken = tokenService.findVerificationTokenByToken(token);
		
		User user = verificationToken.getUser();
		Calendar calendar = Calendar.getInstance();
		
		if((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
			throw new InvalidTokenException();
		}
		
		if(user != null) {
			boolean enabled = user.isEnabled();
			
			if(enabled) {
				throw new UserAlreadyEnabledException();
			}
			
			user.setEnabled(true);
			userRepository.save(user);
			
			boolean deleted = tokenService.deleteVerificationToken(token);
			if(!deleted) {
				throw new DeletionProcedureException("Token deletion procedure went wrong.");
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void createAndInsertRecoveryToken(String email, String appUrl) {
		Map<String, Object> variables = new HashMap<>();
		
		User user = userRepository.findByEmail(email);
		String token = UUID.randomUUID().toString();
		tokenService.insertPasswordRecoveryToken(new UserBasicDTO(user), token);
		
		String url = appUrl + "/users/redirectToChangePassword?token=" + token;
		String text = "You have 24 hours to change your password.";
		
		variables.put("urlConfirmation", url);
		variables.put("linkText", "Click here to change your password");
		variables.put("text", text);
		
		mailService.sendHtmlEmailWithThymeleaf("email-confirmation", email, "Change password", variables);
	}

	
	@Transactional
	private void changePassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
	
	@Transactional
	public void changePasswordWithToken(TokenPasswordDTO dto) {
		User user = tokenService.findUserByRecoveryToken(dto.getToken());
		changePassword(user, dto.getPassword());
		
		boolean deleted = tokenService.deleteRecoveryToken(dto.getToken());
		if(!deleted) {
			throw new DeletionProcedureException("Token deletion procedure went wrong.");
		}
	}
	
	@Transactional
	public void grantUserAuthorization(Long userId, Long authId) {
		if(userId != null && authId != null) {
			User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + RNFE2));
			user.getRoles().add(roleRepository.getReferenceById(authId));
			userRepository.save(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void revokeUserAuthorization(Long userId, Long authId) {
		if(userId != null && authId != null) {
			User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + RNFE2));
			user.getRoles().remove(roleRepository.getReferenceById(authId));
			userRepository.save(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void deleteUser(Long id) {
		if(id != null) {
			boolean deleteFromVault = vaultService.deleteByUserId(id);
			int rows = roleRepository.deleteUserFromUserRoleTable(id);
			
			if(deleteFromVault && rows > 0) {
				userRepository.deleteById(id);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	public void validateTokenForRecovery(String token) {
		tokenService.validatePasswordRecoveryToken(token);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new ResourceNotFoundException("Username " + username + RNFE2);
		}
		
		return user;
	}
	
	private boolean validateOldPassword(User user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	private void dtoToEntity(User user, UserDTO dto) {
		user.setName(dto.getName().toUpperCase());
		user.setEmail(dto.getEmail());
		user.setBirth(dto.getBirth());
		user.setGender(dto.getGender());
		user.setIdNumber(dto.getIdNumber());
		user.setIdType(dto.getIdType());
		user.setPhoneNumber(dto.getPhoneNumber());
		
		Address ads = new Address();
		ads.setAdditionalDetails(dto.getAddress().getAdditionalDetails());
		ads.setAddressLine(dto.getAddress().getAddressLine());
		ads.setAddressType(dto.getAddress().getAddressType());
		ads.setCity(dto.getAddress().getCity());
		ads.setCountry(dto.getAddress().getCountry());
		ads.setDistrict(dto.getAddress().getDistrict());
		ads.setNumber(dto.getAddress().getNumber());
		ads.setState(dto.getAddress().getState());
		ads.setZipCode(dto.getAddress().getZipCode());
		user.setAddress(ads);
		
		user.getRoles().clear();
		dto.getRoles().forEach(role -> {
			Role r = roleRepository.getReferenceById(role.getId());
			user.getRoles().add(r);
		});
		
		if(dto.getVault() != null) {
			Vault vault = vaultService.findReferenceById(dto.getVault().getId());
			user.setVault(vault);
		}
	}
	
	private Vault initializeEmptyVault() {
		Vault v = new Vault();
		v.setAllowedToSpend(BigDecimal.valueOf(0));
		v.setSavings(BigDecimal.valueOf(0));
		v.setOnWallet(BigDecimal.valueOf(0));
		return v;
	}
}