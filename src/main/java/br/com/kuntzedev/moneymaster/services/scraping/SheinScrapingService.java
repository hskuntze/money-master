package br.com.kuntzedev.moneymaster.services.scraping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
public class SheinScrapingService {

	/**
	 * Parâmetros de: pesquisa
	 */
	private static final String BASE_URL = "https://br.shein.com";
	private static final String SEARCH_PARAMETER = "/pdsearch/";
	private static final String ICI_PARAMETER = "/?ici=s1%60EditSearch%60";
	private static final String ICI_ARG2 = "%60_fb%60d0%60PageHome";
	private static final String SEARCH_SOURCE = "&search_source=1";
	private static final String SEARCH_TYPE = "&search_type=all";
	private static final String SRC_IDENTIFIER = "&src_identifier=st%3D2%60sc%3D";
	private static final String SRC_IDENTIFIER_ARG2 = "sr%3D0%60ps%3D1";
	private static final String SRC_MODULE = "&src_module=search";
	
	/**
	 * Parâmetros de: acesso
	 */
	private static final String DIV_PRODUCT = "section[class*=product-card j-expose__product-item hover-effect product-list__item product-list__item-new]";
	private static final String DIV_PRODUCT_TITLE = "a[class=goods-title-link--jump goods-title-link]";
	private static final String DIV_PRODUCT_IMAGE = "div[class=crop-image-container]";
	private static final String DIV_PRODUCT_PRICE = "span[class=normal-price-ctn__sale-price_discount normal-price-ctn__sale-price]";
	private static final String DIV_PRODUCT_NORMAL_PRICE = "span[class=normal-price-ctn__sale-price]";
	
	/**
	 * Parâmetros de: acesso a página do produto
	 */
	private static final String DIV_PRODUCT_PAGE = "div[class=product-intro]";
	private static final String DIV_PRODUCT_PAGE_TITLE = "h1[class=product-intro__head-name]";
	private static final String DIV_PRODUCT_PAGE_PRICE = "div[class=discount from]";
	private static final String DIV_PRODUCT_PAGE_IMG = "div[class=crop-image-container]";
	
	public Page<ScrapingItemDTO> searchForProduct(String product, int page, int size, String sort) {
		Document document = null;
		
		try {
			document = Jsoup.connect(formatLinkToSearch(product)).get();
			
			Elements products = getProducts(document);
			List<ScrapingItemDTO> items = new ArrayList<>();
			
			for(int i = 0; i < products.size(); i++) {
				ScrapingItemDTO item = new ScrapingItemDTO();
				
				item.setName(getProductTitle(document, i));
				item.setImage(getProductImage(document, i).substring(2));
				item.setLink(BASE_URL + getProductLink(document, i));
				item.setPrice(getProductPrice(document, i));
				item.setSourcePlatform(SourcePlatform.SHEIN);
				
				items.add(item);
			}
			
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort)));
			return new PageImpl<ScrapingItemDTO>(items, pageRequest, items.size());
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
			
			String title = document.select(DIV_PRODUCT_PAGE).select(DIV_PRODUCT_PAGE_TITLE).text();
			String price = document.select(DIV_PRODUCT_PAGE).select(DIV_PRODUCT_PAGE_PRICE).attr("aria-label");
			String img = document.select(DIV_PRODUCT_PAGE).select(DIV_PRODUCT_PAGE_IMG).attr("data-before-crop-src");
			
			dto.setLink(item.getLink());
			dto.setName(title);
			dto.setPrice(price.substring(price.indexOf("$") + 1).replace(",", "."));
			dto.setImage("http:" + img);
			dto.setSourcePlatform(SourcePlatform.SHEIN);
			
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
		return document.select(DIV_PRODUCT);
	}
	
	private String getProductTitle(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_TITLE).text();
	}
	
	private String getProductImage(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_IMAGE).select("img").attr("data-src");
	}

	private String getProductLink(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select("a").attr("href");
	}

	private String getProductPrice(Document document, int index) {
		int i = index == 0 ? 0 : index;
		
		Elements element = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_PRICE);
		boolean empty = element.isEmpty();
		
		String price = "0.0";
		if(!empty) {
			String aux = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_PRICE).text();
			price = aux.substring(2).replace(",", ".");
		} else {
			String aux = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_NORMAL_PRICE).text();
			price = aux.substring(2).replace(",", ".");
		}
		
		return price;
	}

	private static String formatLinkToSearch(String product) {
		StringBuilder sb = new StringBuilder(BASE_URL);
		String parsedProduct = "";	
		
		try {
			parsedProduct = URLEncoder.encode(product, "UTF-8").replace("+", "%20").toString();
		} catch (UnsupportedEncodingException e) {
			throw new InvalidLinkException("Couldn't parse to the encoded format", e);
		}
		
		sb.append(SEARCH_PARAMETER);
		sb.append(parsedProduct);
		sb.append(ICI_PARAMETER);
		sb.append(parsedProduct);
		sb.append(ICI_ARG2);
		sb.append(SEARCH_SOURCE);
		sb.append(SEARCH_TYPE);
		sb.append(SRC_IDENTIFIER);
		sb.append(parsedProduct);
		sb.append(SRC_IDENTIFIER_ARG2);
		sb.append(SRC_MODULE);
		
		return sb.toString();
	}
}