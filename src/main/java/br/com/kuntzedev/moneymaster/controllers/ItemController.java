package br.com.kuntzedev.moneymaster.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.dtos.ItemDTO;
import br.com.kuntzedev.moneymaster.services.ItemService;

@RestController
@RequestMapping(value = "/items")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * -------------- GETS --------------
	 */
	
	@GetMapping
	public ResponseEntity<Page<ItemDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "id") String sort) {
		return ResponseEntity.ok().body(itemService.findAll(page, size, sort));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ItemDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(itemService.findById(id));
	}
	
	/**
	 * -------------- POSTS --------------
	 */

	@PostMapping(value = "/register/onWishlist/{id}")
	public ResponseEntity<ItemDTO> insert(@RequestBody ItemDTO dto, @PathVariable Long id) {
		dto = itemService.insert(id, dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	/**
	 * -------------- PUTS --------------
	 */
	
	@PutMapping(value = "/update/bylink")
	public ResponseEntity<Void> update(@RequestBody ItemDTO dto) {
		itemService.updateItemBasedOnLink(dto);
		return ResponseEntity.ok().build();
	}

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<ItemDTO> update(@RequestBody ItemDTO dto, @PathVariable Long id) {
		return ResponseEntity.ok().body(itemService.update(id, dto));
	}
	
	/**
	 * -------------- DELETES --------------
	 */

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		itemService.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * -------------- PATCHES --------------
	 */

	@PatchMapping(value = "/update/{itemId}/wishlist/{wishlistId}")
	public ResponseEntity<Void> updateItemWishlist(@PathVariable Long itemId, @PathVariable Long wishlistId) {
		itemService.updateItemWishlist(itemId, wishlistId);
		return ResponseEntity.ok().build();
	}
}