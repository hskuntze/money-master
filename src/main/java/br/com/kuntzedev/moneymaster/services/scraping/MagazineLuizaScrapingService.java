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
public class MagazineLuizaScrapingService {

	private static final String BASE_URL = "https://www.magazineluiza.com.br";
	private static final String SEARCH_PARAMETER = "/busca/";
	
	private static final String DIV_CONTENT = "section[style*=content]";
	private static final String DIV_CONTENT_ELEMENTS = "ul[data-testid=list]";
	private static final String DIV_ELEMENT_TITLE = "h2[data-testid=product-title]";
	private static final String DIV_ELEMENT_LINK = "a[data-testid=product-card-container]";
	private static final String DIV_ELEMENT_IMG = "img[data-testid=image]";
	private static final String DIV_ELEMENT_PIX_PRICE = "p[data-testid=price-value]";

	private static final String DIV_PAGE_TITLE = "h1[data-testid=heading-product-title]";
	private static final String DIV_PAGE_IMG = "img[data-testid=image-selected-thumbnail]";
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36";
	
	public Page<ScrapingItemDTO> searchForProduct(String product, int page, int size) {
		Document document = null;
		
		try {
			document = Jsoup.connect(formatLinkToSearch(product))
					.userAgent(USER_AGENT)
					.get();
			
			Elements elements = getProducts(document);
			List<ScrapingItemDTO> items = new ArrayList<>();
			
			for(Element e : elements) {
				ScrapingItemDTO item = new ScrapingItemDTO();
				
				item.setName(getProductTitle(e));
				item.setImage(getProductImg(e));
				item.setLink(getProductLink(e));
				item.setPrice(getProductPixPrice(e));
				item.setSourcePlatform(SourcePlatform.MAGAZINE_LUIZA);
				
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
		Document document = null;
		
		try {
			document = Jsoup.connect(item.getLink()).get();
			
			ScrapingItemDTO dto = new ScrapingItemDTO();
			
			String title = document.select(DIV_PAGE_TITLE).text();
			String img = document.select(DIV_PAGE_IMG).attr("src");
			String price = this.getProductPixPrice(document);
			
			dto.setName(title);
			dto.setImage(img);
			dto.setPrice(price);
			dto.setLink(item.getLink());
			dto.setSourcePlatform(SourcePlatform.MAGAZINE_LUIZA);
			
			return dto;
		} catch(HttpStatusException e) {
			String message = e.getMessage();
			String status = e.getMessage().substring(message.indexOf("=") + 1, message.indexOf(","));
			
			throw new ScrapingConnectionException("Something went wrong while trying to search for " + item.getName(), e, Integer.valueOf(status));
		} catch (IOException e) {
			throw new ScrapingConnectionException(e.getMessage());
		}
	}
	
	private Elements getProducts(Document doc) {
		return doc.select(DIV_CONTENT).select(DIV_CONTENT_ELEMENTS).select("li");
	}
	
	private String getProductTitle(Element element) {
		return element.select(DIV_ELEMENT_TITLE).text();
	}
	
	private String getProductLink(Element element) {
		String endpoint = element.select(DIV_ELEMENT_LINK).attr("href");
		return BASE_URL + endpoint;
	}

	private String getProductImg(Element element) {
		return element.select(DIV_ELEMENT_IMG).attr("src");
	}
	
	private String getProductPixPrice(Element element) {
		return element.select(DIV_ELEMENT_PIX_PRICE).text().substring(2).replace(".", "").replace(",", ".").trim();
	}
	
	private String formatLinkToSearch(String product) {
		StringBuilder sb = new StringBuilder(BASE_URL);
		String parsedProduct = "";
		
		parsedProduct = product.replace("%", "").replace("$", "").replace("ç", "c").replace(" ", "-");
		
		sb.append(SEARCH_PARAMETER);
		sb.append(parsedProduct);
		
		return sb.toString();
	}
}