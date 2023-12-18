package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.VaultDTO;
import br.com.kuntzedev.moneymaster.dtos.VaultSavingsResponseDTO;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.entities.Vault;
import br.com.kuntzedev.moneymaster.entities.Wishlist;
import br.com.kuntzedev.moneymaster.repositories.UserRepository;
import br.com.kuntzedev.moneymaster.repositories.VaultRepository;
import br.com.kuntzedev.moneymaster.repositories.WishlistRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class VaultService {

	@Autowired
	private VaultRepository vaultRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WishlistRepository wishlistRepository;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	private static final String RNFE = "Resource not found in the database.";
	private static final String NULL_PARAM = "Null parameter.";
	private static final String NOT_ACCEPTABLE = "This value can't be accepted. Try a value that is greater than 0 (zero).";
	
	@Transactional(readOnly = true)
	public VaultDTO findByAuthenticatedUser() {
		User user = authenticationService.authenticated();
		Vault vault = vaultRepository.findById(user.getVault().getId()).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		return new VaultDTO(vault);
	}
	
	@Transactional(readOnly = true)
	public VaultDTO findVaultById(Long id) {
		Vault vault = vaultRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		return new VaultDTO(vault);
	}
	
	@Transactional(readOnly = true)
	public Vault findReferenceById(Long id) {
		if(id != null) {
			return vaultRepository.getReferenceById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO insert(Vault entity) {
		if(entity != null) {
			entity = vaultRepository.save(entity);
			vaultRepository.flush();
			return new VaultDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	
	@Transactional
	public VaultDTO insert(VaultDTO dto) {
		if(dto != null) {
			Vault entity = new Vault();
			dtoToEntity(entity, dto);
			entity = vaultRepository.save(entity);
			vaultRepository.flush();
			return new VaultDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO update(Long id, Vault newEntity) {
		if(id != null && newEntity != null) {
			Vault entity = vaultRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			
			entity.setAllowedToSpend(newEntity.getAllowedToSpend());
			entity.setOnWallet(newEntity.getOnWallet());
			entity.setSavings(newEntity.getSavings());
			
			entity = vaultRepository.save(entity);
			vaultRepository.flush();
			return new VaultDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO update(Long id, VaultDTO dto) {
		if(id != null && dto != null) {
			Vault entity = vaultRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			
			dtoToEntity(entity, dto);
			entity = vaultRepository.save(entity);
			
			return new VaultDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public boolean deleteByUserId(Long userId) {
		if(userId != null) {
			int rows = vaultRepository.deleteFromVaultTable(userId);
			if(rows > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	public void deleteById(Long id) {
		if(id != null) {
			vaultRepository.deleteById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO increaseWalletValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.increaseWalletValue(value);
				
				vault = vaultRepository.save(vault);
				return new VaultDTO(vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultSavingsResponseDTO increaseSavingsValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				StringBuilder sb = new StringBuilder();
				
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.increaseSavingsValue(value);
				
				List<Wishlist> wishlists = this.verifyWishlistPurchasePossibility();
				if(wishlists.size() > 0) {
					sb.append("Você pode adquirir uma ou mais listas de desejo:");
					for(Wishlist w : wishlists) {
						sb.append(" " + w.getTitle() + ";");
					}
				}
				
				vault = vaultRepository.save(vault);
				return new VaultSavingsResponseDTO(sb.toString(), vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO increaseAllowedToSpendValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.increaseAllowedToSpendValue(value);
				
				vault = vaultRepository.save(vault);
				return new VaultDTO(vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO reduceWalletValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.reduceWalletValue(value);
				
				vault = vaultRepository.save(vault);
				return new VaultDTO(vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO reduceSavingsValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.reduceSavingsValue(value);
				
				vault = vaultRepository.save(vault);
				return new VaultDTO(vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public VaultDTO reduceAllowedToSpendValue(BigDecimal value) {
		if(value != null) {
			if(value.signum() > 0) {
				Long vaultId = authenticationService.authenticated().getVault().getId();
				Vault vault = vaultRepository.getReferenceById(vaultId);
				vault.reduceAllowedToSpendValue(value);
				
				vault = vaultRepository.save(vault);
				return new VaultDTO(vault);
			} else {
				throw new UnprocessableRequestException(NOT_ACCEPTABLE);
			}
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	private List<Wishlist> verifyWishlistPurchasePossibility() {
		Long userId = authenticationService.authenticated().getId();
		User user = userRepository.findUser(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		List<Wishlist> wishlists = new ArrayList<>();
		
		for(int i = 0; i < user.getWishlists().size(); i++) {
			Wishlist wishlist = wishlistRepository.findWishlistWithItems(user.getWishlists().get(i).getId()).orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
			int compare = user.getVault().getSavings().compareTo(wishlist.getTotalValue());
			if(compare >= 0) {
				wishlists.add(wishlist);
			}
		}
		
		return wishlists;
	}

	private void dtoToEntity(Vault entity, VaultDTO dto) {
		entity.setOnWallet(dto.getOnWallet());
		entity.setSavings(dto.getSavings());
		entity.setAllowedToSpend(dto.getAllowedToSpend());
	}
}