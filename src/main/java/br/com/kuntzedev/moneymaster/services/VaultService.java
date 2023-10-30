package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.VaultDTO;
import br.com.kuntzedev.moneymaster.entities.Vault;
import br.com.kuntzedev.moneymaster.repositories.VaultRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class VaultService {

	@Autowired
	private VaultRepository vaultRepository;
	
	private static final String RNFE = "Resource not found in the database.";
	private static final String NULL_PARAM = "Null parameter.";
	private static final String NOT_ACCEPTABLE = "This value can't be accepted. Try a value that is greater than 0 (zero).";
	
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
			flush();
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
			flush();
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
			flush();
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
	public VaultDTO increaseWalletValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
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
	public VaultDTO increaseSavingsValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
				vault.increaseSavingsValue(value);
				
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
	public VaultDTO increaseAllowedToSpendValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
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
	public VaultDTO reduceWalletValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
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
	public VaultDTO reduceSavingsValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
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
	public VaultDTO reduceAllowedToSpendValue(Long id, BigDecimal value) {
		if(id != null && value != null) {
			if(value.signum() > 0) {
				Vault vault = vaultRepository.getReferenceById(id);
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
	
	private void flush() {
		vaultRepository.flush();
	}

	private void dtoToEntity(Vault entity, VaultDTO dto) {
		entity.setOnWallet(dto.getOnWallet());
		entity.setSavings(dto.getSavings());
		entity.setAllowedToSpend(dto.getAllowedToSpend());
	}
}