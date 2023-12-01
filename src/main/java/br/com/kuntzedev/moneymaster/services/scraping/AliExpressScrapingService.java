package br.com.kuntzedev.moneymaster.services.scraping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.kuntzedev.moneymaster.dtos.ScrapingItemDTO;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.enums.SourcePlatform;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.InvalidLinkException;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.ScrapingConnectionException;

@Service
public class AliExpressScrapingService {

	/**
	 * Parâmetros de: pesquisa
	 */
	private static final String BASE_URL = "https://pt.aliexpress.com";
	private static final String SEARCH_PARAMETER = "/w/wholesale-%r.html?page=";
	private static final String GALLERY_PARAMETER = "&g=y";
	private static final String SEARCH_TEXT = "&SearchText=";
	private static final String FOUR_PLUS_STARS_PARAMETER = "&selectedSwitches=pop%3Atrue";
	
	/**
	 * Parâmetros de: acesso
	 */
	private static final String DIV_PRODUCT = "div[class*=search-item-card-wrapper-gallery]";
	private static final String DIV_PRODUCT_TITLE = "div[class*=multi--title--]";
	private static final String DIV_PRODUCT_IMAGE = "div[class*=multi--image--]";
	private static final String DIV_PRODUCT_LINK = "a[class*=search-card-item]";
	private static final String DIV_PRODUCT_PRICE = "div[class*=multi--price-sale--]";
	private static final String DIV_PAGINATION = "div[class=comet-pagination-options-quick-jumper]";
	
	public Page<ScrapingItemDTO> searchForProduct(String product, int page, int size, String sort) {
		Document document = null;
		
		try {
			document = Jsoup.connect(formatLinkToSearch(product, 1)).get();
			List<ScrapingItemDTO> items = new ArrayList<>();
			
			int totalPages = findTotalPages(document);
			
			for(int i = 1; i <= totalPages; i++) {
				document = Jsoup.connect(formatLinkToSearch(product, i)).get();
			
				Elements products = getProducts(document);
				
				for(int j = 0; j < products.size() - 1; j++) {
					ScrapingItemDTO item = new ScrapingItemDTO();
					
					item.setName(getProductTitle(document, j));
					item.setImage(getProductImage(document, j));
					item.setLink("https://" + getProductLink(document, j));
					item.setPrice(getProductPrice(document, j));
					item.setSourcePlatform(SourcePlatform.ALI_EXPRESS);
					
					items.add(item);
				}
			}
			
			PageRequest pageRequest = PageRequest.of(page, size, Sort.unsorted());
			return new PageImpl<>(items, pageRequest, items.size());
		} catch(HttpStatusException e) {
			String message = e.getMessage();
			String status = e.getMessage().substring(message.indexOf("=") + 1, message.indexOf(","));
			
			throw new ScrapingConnectionException("Something went wrong while trying to search for " + product, e, Integer.valueOf(status));
		} catch (IOException e) {
			throw new ScrapingConnectionException(e.getMessage());
		}
	}

	public ScrapingItemDTO updateItemBasedOnLink(Item item) {
		String productName = item.getName().substring(0, item.getName().indexOf(" "));
		
		List<ScrapingItemDTO> result = searchForProduct(productName, 0, 10, "name")
				.getContent()
				.stream()
				.filter(el -> el.getName().equals(item.getName()))
				.collect(Collectors.toList());
		
		ScrapingItemDTO actual = result.get(0);
		return new ScrapingItemDTO(actual.getPrice(), actual.getName(), actual.getLink(), actual.getImage(), SourcePlatform.ALI_EXPRESS);
	}

	private Elements getProducts(Document document) {
		return document.select(DIV_PRODUCT);
	}

	private String getProductTitle(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_TITLE).text();
	}
	
	private String getProductImage(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_IMAGE).select("img").get(0).attr("src").substring(2);
	}

	private String getProductLink(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_LINK).attr("href").substring(2);
	}
	
	private String getProductPrice(Document document, int index) {
		int i = index == 0 ? 0 : index;
		
		String aux = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_PRICE).text();
		String price = aux.substring(2).replace(".", "").replace(",", ".");
		
		return price;
	}
	
	private int findTotalPages(Document document) {
		String aux = document.select(DIV_PAGINATION).text();
		int i = aux.indexOf('/');
		
		return Integer.valueOf(aux.substring(i+1, i+2));
	}

	private String formatLinkToSearch(String product, int page) {
		StringBuilder sb = new StringBuilder(BASE_URL);
		String parsedProduct = "";
		
		try {
			parsedProduct = URLEncoder.encode(product, "UTF-8").replace("+", "-");
		} catch (UnsupportedEncodingException e) {
			throw new InvalidLinkException("Couldn't parse to the encoded format", e);
		}
		
		String aux = SEARCH_PARAMETER.replace("%r", parsedProduct);
		
		sb.append(aux);
		sb.append(page);
		sb.append(GALLERY_PARAMETER);
		sb.append(SEARCH_TEXT + parsedProduct.replace("-", "+"));
		sb.append(FOUR_PLUS_STARS_PARAMETER);
		
		return sb.toString();
	}
}