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

import br.com.kuntzedev.moneymaster.dtos.ItemDTO;
import br.com.kuntzedev.moneymaster.dtos.ScrapingItemDTO;
import br.com.kuntzedev.moneymaster.services.ItemService;
import br.com.kuntzedev.moneymaster.services.scraping.AliExpressScrapingService;
import br.com.kuntzedev.moneymaster.services.scraping.AmazonScrapingService;
import br.com.kuntzedev.moneymaster.services.scraping.MercadoLivreScrapingService;
import br.com.kuntzedev.moneymaster.services.scraping.SheinScrapingService;

@RestController
@RequestMapping(value = "/scraping")
public class ScrapingController {

	@Autowired
	private AmazonScrapingService amazonScrapingService;
	
	@Autowired
	private SheinScrapingService sheinScrapingService;
	
	@Autowired
	private MercadoLivreScrapingService mercadoLivreScrapingService;
	
	@Autowired
	private AliExpressScrapingService aliExpressScrapingService;
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 				A M A Z O N
	 */
	@GetMapping(value = "/amazon/search")
	public ResponseEntity<Page<ScrapingItemDTO>> searchForAmazonProduct(@RequestParam("product") String product,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "prime", defaultValue = "false") boolean prime,
			@RequestParam(value = "freeShiping", defaultValue = "false") boolean freeShiping){
		return ResponseEntity.ok().body(amazonScrapingService.searchForProduct(product, page, size, sort, prime, freeShiping));
	}
	
	/**
	 * 				S H E I N
	 */
	@GetMapping(value = "/shein/search")
	public ResponseEntity<Page<ScrapingItemDTO>> searchForSheinProduct(@RequestParam("product") String product,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort){
		return ResponseEntity.ok().body(sheinScrapingService.searchForProduct(product, page, size, sort));
	}
	
	/**
	 * 				S H E I N
	 */
	@GetMapping(value = "/mercadolivre/search")
	public ResponseEntity<Page<ScrapingItemDTO>> searchForMercadoLivreProduct(@RequestParam("product") String product,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort){
		return ResponseEntity.ok().body(mercadoLivreScrapingService.searchForProduct(product, page, size, sort));
	}
	
	/**
	 * 				A L I   E X P R E S S
	 */
	@GetMapping(value = "/aliexpress/search")
	public ResponseEntity<Page<ScrapingItemDTO>> searchForAliExpressProduct(@RequestParam("product") String product,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort){
		return ResponseEntity.ok().body(aliExpressScrapingService.searchForProduct(product, page, size, sort));
	}
	
	/**
	 * 				I N S E R T    I T E M S
	 */
	@PostMapping(value = "/register/onWishlist/{id}")
	public ResponseEntity<ItemDTO> insert(@RequestBody ScrapingItemDTO dto, @PathVariable Long id) {
		ItemDTO item = itemService.insertScrapingProduct(id, dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(item.getId()).toUri();
		return ResponseEntity.created(uri).body(item);
	}
}