package br.com.kuntzedev.moneymaster.services.scraping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kuntzedev.moneymaster.dtos.ScrapingItemDTO;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.enums.SourcePlatform;

@Service
public class ScrapingMediatorService {

	@Autowired
	private AmazonScrapingService amazonScrapingService;

	@Autowired
	private AliExpressScrapingService aliexpressScrapingService;

	@Autowired
	private MercadoLivreScrapingService mercadoLivreScrapingService;

	@Autowired
	private SheinScrapingService sheinScrapingService;

	@Autowired
	private MagazineLuizaScrapingService magazineLuizaScrapingService;

	@Autowired
	private KabumScrapingService kabumScrapingService;

	public List<ScrapingItemDTO> searchForProductOnManyPlatforms(String item, int size, int page,
			SourcePlatform... platforms) {
		List<ScrapingItemDTO> items = new ArrayList<>();

		for (int i = 0; i < platforms.length; i++) {
			switch (platforms[i]) {
				case AMAZON:
					items.addAll(amazonScrapingService.searchForProduct(item, page, size, false, false).getContent());
					break;
				case ALI_EXPRESS:
					items.addAll(aliexpressScrapingService.searchForProduct(item, page, size).getContent());
					break;
				case MERCADO_LIVRE:
					items.addAll(mercadoLivreScrapingService.searchForProduct(item, page, size).getContent());
					break;
				case SHEIN:
					items.addAll(sheinScrapingService.searchForProduct(item, page, size).getContent());
					break;
				case MAGAZINE_LUIZA:
					items.addAll(magazineLuizaScrapingService.searchForProduct(item, page, size).getContent());
					break;
				case KABUM:
					items.addAll(kabumScrapingService.searchForProduct(item, page, size).getContent());
					break;
				default:
					break;
			}
		}
		
		return items;
	}

	public ScrapingItemDTO updateItemBasedOnLink(Item item, SourcePlatform sourcePlatform) {
		switch (sourcePlatform) {
		case AMAZON:
			return amazonScrapingService.updateItemBasedOnLink(item);
		case ALI_EXPRESS:
			return aliexpressScrapingService.updateItemBasedOnLink(item);
		case MERCADO_LIVRE:
			return mercadoLivreScrapingService.updateItemBasedOnLink(item);
		case SHEIN:
			return sheinScrapingService.updateItemBasedOnLink(item);
		case MAGAZINE_LUIZA:
			return magazineLuizaScrapingService.updateItemBasedOnLink(item);
		case KABUM:
			return kabumScrapingService.updateItemBasedOnLink(item);
		default:
			return new ScrapingItemDTO();
		}
	}
}