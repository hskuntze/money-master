package br.com.kuntzedev.moneymaster.controllers.scraping;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.dtos.AmazonItemDTO;
import br.com.kuntzedev.moneymaster.dtos.ItemDTO;
import br.com.kuntzedev.moneymaster.services.ItemService;
import br.com.kuntzedev.moneymaster.services.scraping.AmazonScrapingService;

@RestController
@RequestMapping(value = "/amazonScraping")
public class AmazonScrapingController {

	@Autowired
	private AmazonScrapingService amazonScrapingService;
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * -------------- GETS --------------
	 */
	
	@GetMapping(value = "/search")
	public ResponseEntity<Page<AmazonItemDTO>> searchForProduct(@RequestParam("product") String product,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "title") String sort,
			@RequestParam(value = "prime", defaultValue = "false") boolean prime,
			@RequestParam(value = "freeShiping", defaultValue = "false") boolean freeShiping){
		return ResponseEntity.ok().body(amazonScrapingService.searchForProduct(product, page, size, sort, prime, freeShiping));
	}
	
	/**
	 * -------------- POSTS --------------
	 */
	
	@PostMapping(value = "/register/amazon/onWishlist/{id}")
	public ResponseEntity<ItemDTO> insert(@RequestBody AmazonItemDTO dto, @PathVariable Long id) {
		ItemDTO amazon = itemService.insertAmazonProduct(id, dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(amazon.getId()).toUri();
		return ResponseEntity.created(uri).body(amazon);
	}
}