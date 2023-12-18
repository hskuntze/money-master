package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.WishlistDTO;
import br.com.kuntzedev.moneymaster.entities.Installment;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.entities.Wishlist;
import br.com.kuntzedev.moneymaster.repositories.InstallmentRepository;
import br.com.kuntzedev.moneymaster.repositories.WishlistRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class WishlistService {

	@Autowired
	private WishlistRepository wishlistRepository;
	
	@Autowired
	private InstallmentRepository installmentRepository;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	private static final String RNFE2 = " was not found.";
	private static final String NULL_PARAM = "Null parameter.";
	
	@Transactional(readOnly = true)
	public WishlistDTO findById(Long wishlistId) {
		Wishlist wishlist = wishlistRepository.findWishlistWithItems(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist with ID + " + wishlistId + RNFE2));
		return new WishlistDTO(wishlist);
	}
	
	@Transactional(readOnly = true)
	public Page<WishlistDTO> findByUserId(Pageable pageable, Long userId) {
		Page<Wishlist> wishlist = wishlistRepository.findByUserId(pageable, userId).orElseThrow(() -> new ResourceNotFoundException("User with ID + " + userId + RNFE2));
		return wishlist.map(WishlistDTO::new);
	}
	
	@Transactional(readOnly = true)
	public Page<WishlistDTO> findByAuthenticatedUser(Pageable pageable) {
		Long userId = authenticationService.authenticated().getId();
		Page<Wishlist> wishlist = wishlistRepository.findByUserId(pageable, userId).orElseThrow(() -> new ResourceNotFoundException("User with ID + " + userId + RNFE2));
		return wishlist.map(WishlistDTO::new);
	}
	
	@Transactional(readOnly = true)
	public Page<WishlistDTO> findByTitleAndAuthenticatedUser(Pageable pageable, String title){
		Long id = authenticationService.authenticated().getId();
		Page<Wishlist> wishlist = wishlistRepository.findByTitleAndAuthenticatedUser(pageable, title, id);
		return wishlist.map(WishlistDTO::new);
	}
	
	@Transactional(readOnly = true)
	public Wishlist findReference(Long id) {
		if(id != null) {
			return wishlistRepository.getReferenceById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public WishlistDTO insert(WishlistDTO dto) {
		if(dto != null) {
			Wishlist entity = new Wishlist();
			
			dtoToEntity(entity, dto);
			
			entity.setTotalInstallments(0);
			entity.setTotalValue(BigDecimal.ZERO);
			entity.setInstallmentsValue(BigDecimal.ZERO);
			entity.setCreated(LocalDate.now());
			entity.setUser(authenticationService.authenticated());
			entity.setEnabled(false);
			entity = wishlistRepository.save(entity);
			
			return new WishlistDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public WishlistDTO update(Long id, WishlistDTO dto) {
		if(id != null && dto != null) {
			Wishlist entity = wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource" + RNFE2));
			
			dtoToEntity(entity, dto);
			
			BigDecimal value = BigDecimal.ZERO;
			
			/**
			 * Verifica se a Wishlist tem itens e se tem parcelas:
			 * 
			 * No primeiro caso onde tem itens e não tem parcelas;
			 * No segundo caso onde tem itens e tem parcelas.
			 * 
			 * Aqui são definidos o valor total, a quantidade de parcelas, os seus respectivos valores
			 * e por fim são criadas as novas parcelas. A lógica (por enquanto) é excluir as parcelas
			 * antigas e registrar novas.
			 */
			if(!(entity.getItems().isEmpty())) {
				value = entity.getItems().stream().map(Item::getPrice).reduce(value, BigDecimal::add);
				
				if(!dto.isInstallment()) {
					//Aqui é se "installment" = false -> não tem parcelas
					entity.setTotalInstallments(1);
					entity.setInstallmentsValue(value);
					entity.setTotalValue(value);
					
					if(entity.getInstallments().isEmpty()) {
						Installment installment = new Installment();
						installment.setPrice(value);
						installment.setDateOfCharge(dto.getToBuyAt());
						installment.setWishlist(entity);
						
						installmentRepository.save(installment);
						entity.getInstallments().add(installment);
					} else {
						//Aqui "installment" = false e as parcelas de "entity" não são vazias, ou seja, já existem parcelas
						entity.getInstallments().forEach(inst -> installmentRepository.deleteById(inst.getId()));
						entity.getInstallments().clear();
						
						Installment installment = new Installment();
						installment.setPrice(value);
						installment.setDateOfCharge(dto.getToBuyAt());
						installment.setWishlist(entity);
						
						installmentRepository.save(installment);
						entity.getInstallments().add(installment);
					}
				} else {
					//Aqui é se "installment" = true -> tem parcelas
					entity.setTotalInstallments(dto.getTotalInstallments());
					BigDecimal totalInstallments = BigDecimal.valueOf(dto.getTotalInstallments().doubleValue());
					BigDecimal installmentValue = value.divide(totalInstallments, 2, RoundingMode.HALF_EVEN);
					entity.setInstallmentsValue(installmentValue);
					entity.setTotalValue(value);
					
					if(entity.getInstallments().isEmpty()) {
						//Aqui "installment" = true e as parcelas de "entity" são vazias, ou seja, não existem parcelas
						for(int i = 0; i < dto.getTotalInstallments(); i++) {
							Installment installment = new Installment();
							installment.setPrice(installmentValue);
							installment.setDateOfCharge(dto.getToBuyAt().plusMonths(i));
							installment.setWishlist(entity);
							
							installmentRepository.save(installment);
							entity.getInstallments().add(installment);
						}
					} else {
						//Aqui "installment" = true e as parcelas de "entity" não são vazias, ou seja, já existem parcelas
						entity.getInstallments().forEach(inst -> installmentRepository.deleteById(inst.getId()));
						entity.getInstallments().clear();
						
						for(int i = 0; i < dto.getTotalInstallments(); i++) {
							Installment installment = new Installment();
							installment.setPrice(installmentValue);
							installment.setDateOfCharge(dto.getToBuyAt().plusMonths(i));
							installment.setWishlist(entity);
							
							installmentRepository.save(installment);
							entity.getInstallments().add(installment);
						}
					}
				}
			}
			
			wishlistRepository.save(entity);
			
			return new WishlistDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	public void deleteById(Long id) {
		if(id != null) {
			Wishlist entity = wishlistRepository.findWishlistWithItems(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist with ID + " + id + RNFE2));
			itemService.deleteAll(entity.getItems());
			wishlistRepository.deleteById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}

	private void dtoToEntity(Wishlist entity, WishlistDTO dto) {
		entity.setTitle(dto.getTitle());
		entity.setCreated(dto.getCreated());
		entity.setDescription(dto.getDescription());
		entity.setTitle(dto.getTitle());
		entity.setEnabled(dto.isEnabled());
		entity.setTotalValue(dto.getTotalValue());
		entity.setToBuyAt(dto.getToBuyAt());
		entity.setInstallment(dto.isInstallment());
	}
}