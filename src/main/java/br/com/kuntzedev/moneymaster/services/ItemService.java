package br.com.kuntzedev.moneymaster.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.AmazonItemDTO;
import br.com.kuntzedev.moneymaster.dtos.ItemDTO;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.entities.ItemHistory;
import br.com.kuntzedev.moneymaster.entities.ItemPrice;
import br.com.kuntzedev.moneymaster.entities.Wishlist;
import br.com.kuntzedev.moneymaster.repositories.ItemHistoryRepository;
import br.com.kuntzedev.moneymaster.repositories.ItemPriceRepository;
import br.com.kuntzedev.moneymaster.repositories.ItemRepository;
import br.com.kuntzedev.moneymaster.repositories.WishlistRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;
import br.com.kuntzedev.moneymaster.services.exceptions.UnprocessableRequestException;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemHistoryRepository itemHistoryRepository;
	
	@Autowired
	private ItemPriceRepository itemPriceRepository;
	
	@Autowired
	private WishlistRepository wishlistRepository;
	
	private static final String RNFE = "Resource not found in the database.";
	private static final String NULL_PARAM = "Null parameter.";
	
	@Transactional(readOnly = true)
	public Page<ItemDTO> findAll(int page, int size, String sort) {
		List<Item> list = itemRepository.findAllItemsWithHistory();
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
		return new PageImpl<Item>(list, pageRequest, list.size()).map(ItemDTO::new);
	}
	
	@Transactional(readOnly = true)
	public ItemDTO findById(Long id) {
		Item item = itemRepository.findItemWithHistory(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		return new ItemDTO(item);
	}
	
	@Transactional
	public ItemDTO insert(Long wishlistId, ItemDTO dto) {
		if(dto != null) {
			Item entity = new Item();
			
			dtoToEntity(entity, dto);
			entity.setWishlist(wishlistRepository.getReferenceById(wishlistId));
			entity = itemRepository.save(entity);
			
			ItemHistory history = initializeHistory(entity);
			entity.setItemHistory(history);
			
			return new ItemDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public ItemDTO insertAmazonProduct(Long wishlistId, AmazonItemDTO dto) {
		if(dto != null) {
			Item entity = new Item();
			
			entity.setName(dto.getName());
			entity.setImage(dto.getImage());
			entity.setLink(dto.getLink());
			entity.setPrice(dto.getPrice());
			entity.setVariation(0.0f);
			entity.setWishlist(wishlistRepository.getReferenceById(wishlistId));
			entity = itemRepository.save(entity);
			
			ItemHistory history = initializeHistory(entity);
			entity.setItemHistory(history);
			
			return new ItemDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public ItemDTO update(Long itemId, ItemDTO dto) {
		if(itemId != null && dto != null) {
			Item entity = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			
			dtoToEntity(entity, dto);
			entity = itemRepository.save(entity);
			
			return new ItemDTO(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	@Transactional
	public void updateItemWishlist(Long itemId, Long wishlistId) {
		if(itemId != null & wishlistId != null) {
			Item entity = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist: " + RNFE));
			
			entity.setWishlist(wishlist);
			entity = itemRepository.save(entity);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	public void deleteById(Long id) {
		if(id != null) {
			itemRepository.deleteById(id);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}
	
	public void deleteAll(List<Item> items) {
		if(!items.isEmpty()) {
			itemRepository.deleteAll(items);
		} else {
			throw new UnprocessableRequestException(NULL_PARAM);
		}
	}

	private void dtoToEntity(Item entity, ItemDTO dto) {
		entity.setImage(dto.getImage());
		entity.setLink(dto.getLink());
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setVariation(dto.getVariation());
		
		entity.setItemHistory(dto.getItemHistory());
	}
	
	private ItemHistory initializeHistory(Item entity) {
		ItemPrice itemPrice = new ItemPrice(LocalDate.now(), entity.getPrice());
		itemPriceRepository.save(itemPrice);
		
		ItemHistory history = new ItemHistory();
		history.setItem(entity);
		history.setFluctuation(0f);
		history.getItemPrices().add(itemPrice);
		itemHistoryRepository.save(history);
		
		return history;
	}
}