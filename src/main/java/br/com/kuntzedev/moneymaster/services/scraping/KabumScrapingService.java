package br.com.kuntzedev.moneymaster.services.scraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.kuntzedev.moneymaster.dtos.ScrapingItemDTO;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.enums.SourcePlatform;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.ScrapingConnectionException;

@Service
public class KabumScrapingService {

	private static final String BASE_URL = "https://www.kabum.com.br";
	private static final String SEARCH_PARAM = "/busca/";
	
	private static final String DIV_ELEMENT = "div[class*=productCard]";
	private static final String DIV_ELEMENT_LINK = "a[class*=productLink]";
	private static final String DIV_ELEMENT_IMAGE = "img[class*=imageCard]";
	private static final String DIV_ELEMENT_TITLE = "span[class*=nameCard]";
	
	private static final String DIV_PAGE_IMAGE = "div[class*=selectedImage]";
	private static final String DIV_PAGE_PRICE = "b[class*=regularPrice]";
	private static final String DIV_PAGE_TITLE = "h1[class*=sc-]";
	
	public Page<ScrapingItemDTO> searchForProduct(String product, int page, int size) {
		Document document = null;
		
		try {
			document = Jsoup.connect(BASE_URL + SEARCH_PARAM + product).get();
			
			Elements elements = getProducts(document);
			List<ScrapingItemDTO> items = new ArrayList<>();
			
			for(Element e : elements) {
				ScrapingItemDTO item = new ScrapingItemDTO();
				
				String link = BASE_URL + getProductLink(e);
				
				item.setLink(link);
				item.setName(getProductTitle(e));
				item.setImage(getProductImage(e));
				item.setPrice(getProductPrice(link));
				item.setSourcePlatform(SourcePlatform.KABUM);
				
				items.add(item);
			}
			
			Pageable pageRequest = PageRequest.of(page, size);
			int start = (int) pageRequest.getOffset();
			int end = Math.min((start + pageRequest.getPageSize()), items.size());
			List<ScrapingItemDTO> pageContent = items.subList(start, end);
			
			return new PageImpl<>(pageContent, pageRequest, items.size());
		} catch(HttpStatusException e) {
			String message = e.getMessage();
			String status = e.getMessage().substring(message.indexOf("=") + 1, message.indexOf(","));
			
			throw new ScrapingConnectionException("Something went wrong while trying to search for " + product, e, Integer.valueOf(status));
		} catch (IOException e) {
			throw new ScrapingConnectionException(e.getMessage());
		}
	}
	
	public ScrapingItemDTO updateItemBasedOnLink(Item item) {
		Document doc = null;
		
		try {
			doc = Jsoup.connect(item.getLink()).get();
			
			ScrapingItemDTO dto = new ScrapingItemDTO();
			
			String title = doc.select(DIV_PAGE_TITLE).text();
			String image = doc.select(DIV_PAGE_IMAGE).select("img").attr("src");
			String price = formatPrice(doc.select(DIV_PAGE_PRICE).text());
			
			dto.setName(title);
			dto.setImage(image);
			dto.setPrice(price);
			dto.setLink(item.getLink());
			dto.setSourcePlatform(SourcePlatform.KABUM);

			return dto;
		} catch(HttpStatusException e) {
			String message = e.getMessage();
			String status = e.getMessage().substring(message.indexOf("=") + 1, message.indexOf(","));
			
			throw new ScrapingConnectionException("Something went wrong while trying to search for " + item.getName(), e, Integer.valueOf(status));
		} catch (IOException e) {
			throw new ScrapingConnectionException(e.getMessage());
		}
	}
	
	private Elements getProducts(Document document) {
		return document.select(DIV_ELEMENT);
	}
	
	private String getProductLink(Element element) {
		return element.select(DIV_ELEMENT_LINK).attr("href");
	}
	
	private String getProductTitle(Element element) {
		return element.select(DIV_ELEMENT_LINK).select(DIV_ELEMENT_TITLE).text();
	}
	
	private String getProductImage(Element element) {
		return element.select(DIV_ELEMENT_LINK).select(DIV_ELEMENT_IMAGE).attr("src");
	}
	
	private String getProductPrice(String url) throws IOException {
		Document doc = null;
		
		doc = Jsoup.connect(url).get();
		String price = doc.select(DIV_PAGE_PRICE).text();
		
		price = formatPrice(price);
		
		return price;
	}
	
	private String formatPrice(String price) {
		price = price.replace(".", "").replace(",", ".");
		
		if(price.length() == 0) {
			price = "0.0";
		} else {
			price = price.substring(3);
		}
		
		return price;
	}
}