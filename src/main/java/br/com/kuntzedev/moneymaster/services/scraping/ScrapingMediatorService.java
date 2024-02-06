package br.com.kuntzedev.moneymaster.services.scraping;

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

	public ScrapingItemDTO updateItemBasedOnLink(Item item, SourcePlatform sourcePlatform) {
		switch(sourcePlatform) {
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