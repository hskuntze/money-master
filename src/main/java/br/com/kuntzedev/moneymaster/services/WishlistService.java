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
import br.com.kuntzedev.moneymaster.entities.Wishlist;
import br.com.kuntzedev.moneymaster.repositories.WishlistRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class WishlistService {

	@Autowired
	private WishlistRepository wishlistRepository;
	
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
			entity.setCreated(LocalDate.now());
			entity.setUser(authenticationService.authenticated());
			System.out.println(entity.getUser());
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
			Wishlist entity = wishlistRepository.getReferenceById(id);
			dtoToEntity(entity, dto);
			entity = wishlistRepository.save(entity);
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
		
		if(dto.isInstallment() == false) {
			entity.setTotalInstallments(1);
			entity.setInstallmentsValue(dto.getTotalValue());
		} else {
			entity.setTotalInstallments(dto.getTotalInstallments());
			BigDecimal totalInstallments = BigDecimal.valueOf(dto.getTotalInstallments().doubleValue());
			entity.setInstallmentsValue(dto.getTotalValue().divide(totalInstallments, 4, RoundingMode.HALF_EVEN));
		}
	}
}