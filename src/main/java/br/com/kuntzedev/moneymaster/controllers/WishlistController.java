package br.com.kuntzedev.moneymaster.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.dtos.WishlistDTO;
import br.com.kuntzedev.moneymaster.services.WishlistService;

@RestController
@RequestMapping(value = "/wishlists")
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;

	/**
	 * -------------- GETS --------------
	 */

	@GetMapping(value = "/{id}")
	public ResponseEntity<WishlistDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(wishlistService.findById(id));
	}

	@GetMapping(value = "/user/{userId}")
	public ResponseEntity<Page<WishlistDTO>> findByUserId(Pageable pageable, @PathVariable Long userId) {
		return ResponseEntity.ok().body(wishlistService.findByUserId(pageable, userId));
	}

	@GetMapping(value = "/user")
	public ResponseEntity<Page<WishlistDTO>> findByAuthenticatedUser(Pageable pageable) {
		return ResponseEntity.ok().body(wishlistService.findByAuthenticatedUser(pageable));
	}

	@GetMapping(value = "/user/filter")
	public ResponseEntity<Page<WishlistDTO>> findByTitleAnduthenticatedUser(Pageable pageable,
			@RequestParam(name = "title", defaultValue = "") String title) {
		return ResponseEntity.ok().body(wishlistService.findByTitleAndAuthenticatedUser(pageable, title));
	}

	/**
	 * -------------- POSTS --------------
	 */

	@PostMapping(value = "/register")
	public ResponseEntity<WishlistDTO> insert(@RequestBody WishlistDTO dto) {
		dto = wishlistService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	/**
	 * -------------- PUTS --------------
	 */

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<WishlistDTO> update(@PathVariable Long id, @RequestBody WishlistDTO dto) {
		return ResponseEntity.ok().body(wishlistService.update(id, dto));
	}

	/**
	 * -------------- DELETES --------------
	 */

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		wishlistService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * -------------- PATCHES --------------
	 */
}