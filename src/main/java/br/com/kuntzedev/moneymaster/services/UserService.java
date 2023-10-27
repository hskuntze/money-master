package br.com.kuntzedev.moneymaster.services;

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
import br.com.kuntzedev.moneymaster.entities.Address;
import br.com.kuntzedev.moneymaster.entities.Role;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.entities.Vault;
import br.com.kuntzedev.moneymaster.repositories.RoleRepository;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.repositories.VaultRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.InvalidPasswordException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;
import br.com.kuntzedev.moneymaster.services.exceptions.UserAlreadyEnabledException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VaultRepository vaultRepository;
	
	@Autowired
	private AuthenticationService authService;
	
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
	
	@Transactional
	public UserBasicDTO insert(UserInsertDTO dto) {
		if(dto != null) {
			User user = new User();
			dtoToEntity(user, dto);
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user = userRepository.save(user);
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
			user.setEnabled(dto.isEnabled());
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
				throw new UnprocessableRequestException(NULL_PARAM + " Auth failed.");
			}
			
			if(!validateOldPassword(user, oldPassword)) {
				throw new InvalidPasswordException("Invalid password.");
			}
			
			user.setPassword(passwordEncoder.encode(newPassword));
			user = userRepository.save(user);
			return new UserBasicDTO(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void enableRegisteredUser(User user) {
		if(user != null) {
			boolean enabled = user.isEnabled();
			
			if(enabled) {
				throw new UserAlreadyEnabledException();
			}
			
			user.setEnabled(true);
			userRepository.save(user);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
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
		
//		Vault vlt = new Vault();
//		vlt.setAllowedToSpend(dto.getVault().getAllowedToSpend());
//		vlt.setOnWallet(dto.getVault().getOnWallet());
//		vlt.setSavings(dto.getVault().getSavings());
//		user.setVault(vlt);
		
		/**
		 * Fazer uma função que verifica a existência de um Vault, se existir ok
		 * mas caso não exista deverá salvar uma nova instância no banco de dados.
		 */
		if(dto.getVault() != null) {
			Vault vault = vaultRepository.getReferenceById(dto.getVault().getId());
			if(vault != null) {
				user.setVault(vault);
			}
		}
		
		user.getRoles().clear();
		dto.getRoles().forEach(role -> {
			Role r = roleRepository.getReferenceById(role.getId());
			user.getRoles().add(r);
		});
	}
}