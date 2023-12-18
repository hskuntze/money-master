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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.kuntzedev.moneymaster.dtos.ScrapingItemDTO;
import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.enums.SourcePlatform;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.InvalidLinkException;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.ScrapingConnectionException;

@Service
public class MercadoLivreScrapingService {

	/**
	 * Parâmetros de: pesquisa
	 */
	private static final String BASE_URL = "https://lista.mercadolivre.com.br";
	private static final String SEARCH_PARAMETER = "#D[A:%r]";
	
	/**
	 * Parâmetros de: acesso
	 */
	private static final String DIV_PRODUCT = "li[class=ui-search-layout__item]";
	private static final String DIV_PRODUCT_TITLE = "h2[class=ui-search-item__title]";
	private static final String DIV_PRODUCT_IMAGE = "img[class*=ui-search-result-image__element]";
	private static final String DIV_PRODUCT_LINK = "a[class*=ui-search-item__group__element ui-search-link__title-card ui-search-link]";
	private static final String DIV_PRODUCT_PRICE = "span[class=andes-money-amount ui-search-price__part ui-search-price__part--medium andes-money-amount--cents-superscript]";
	private static final String DIV_PRODUCT_PRICE_INTEGER = "span[class=andes-money-amount__fraction]";
	private static final String DIV_PRODUCT_PRICE_CENTS = "span[class=andes-money-amount__cents andes-money-amount__cents--superscript-24]";	
	
	/**
	 * Parâmetros de: acesso a página do produto
	 */
	private static final String DIV_PRODUCT_PAGE = "div[class*=ui-pdp-container__row ui-pdp-with--separator--fluid ui-pdp-with--separator--40]";
	private static final String DIV_PRODUCT_PAGE_TITLE = "h1[class*=ui-pdp-title]";
	private static final String DIV_PRODUCT_PAGE_PRICE = "span[class=andes-money-amount ui-pdp-price__part andes-money-amount--cents-superscript andes-money-amount--compact]";
	private static final String DIV_PRODUCT_PAGE_PRICE_INT = "span[class=andes-money-amount__fraction]";
	private static final String DIV_PRODUCT_PAGE_PRICE_FRACTION = "span[class*=andes-money-amount__cents]";
	private static final String DIV_PRODUCT_PAGE_IMG = "img[class=ui-pdp-image ui-pdp-gallery__figure__image]";
	
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/119.0";
	
	public Page<ScrapingItemDTO> searchForProduct(String product, int page, int size) {
		Document document = null;
		
		try {
			document = Jsoup.connect(formatLinkToSearch(product)).get();
			
			Elements products = getProducts(document);
			List<ScrapingItemDTO> items = new ArrayList<>();
			
			for(int i = 0; i < products.size(); i++) {
				ScrapingItemDTO item = new ScrapingItemDTO();
				
				item.setName(getProductTitle(document, i));
				item.setImage(getProductImage(document, i));
				item.setLink(getProductLink(document, i));
				item.setPrice(getProductPrice(document, i));
				item.setSourcePlatform(SourcePlatform.MERCADO_LIVRE);
				
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
			document = Jsoup.connect(item.getLink())
					.userAgent(USER_AGENT)
					.get();
			
			ScrapingItemDTO dto = new ScrapingItemDTO();
			
			String title = document.select(DIV_PRODUCT_PAGE).select(DIV_PRODUCT_PAGE_TITLE).text();
			String img = document.select(DIV_PRODUCT_PAGE).select(DIV_PRODUCT_PAGE_IMG).attr("src");
			
			String priceInt = document.select(DIV_PRODUCT_PAGE_PRICE).get(0).select(DIV_PRODUCT_PAGE_PRICE_INT).text();
			String priceFraction = document.select(DIV_PRODUCT_PAGE_PRICE).get(0).select(DIV_PRODUCT_PAGE_PRICE_FRACTION).text();
			String price = priceInt.replace(".", "") + "." + priceFraction;

			dto.setLink(item.getLink());
			dto.setName(title);
			dto.setImage(img);
			dto.setPrice(price);
			dto.setSourcePlatform(SourcePlatform.MERCADO_LIVRE);
				
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
		return document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_IMAGE).attr("data-src");
	}
	
	private String getProductLink(Document document, int index) {
		int i = index == 0 ? 0 : index;
		return document.select(DIV_PRODUCT).get(i).select("div[class*=ui-search-result__content]") .select(DIV_PRODUCT_LINK).attr("href");
	}
	
	private String getProductPrice(Document document, int index) {
		int i = index == 0 ? 0 : index;
		
		Elements intPrice = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_PRICE).select(DIV_PRODUCT_PRICE_INTEGER);
		Elements fractionPrice = document.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_PRICE).select(DIV_PRODUCT_PRICE_CENTS);
		
		if(!fractionPrice.isEmpty()) {
			return intPrice.text().replace(".", "") + "." + fractionPrice.text();
		} else {
			return intPrice.text().replace(".", "") + ".0";
		}
	}

	private String formatLinkToSearch(String product) {
		StringBuilder sb = new StringBuilder(BASE_URL);
		String parsedProduct = "";
		
		try {
			parsedProduct = URLEncoder.encode(product, "UTF-8").replace("+", "%20");
		} catch(UnsupportedEncodingException e) {
			throw new InvalidLinkException("Couldn't parse to the encoded format", e);
		}
		
		sb.append("/" + parsedProduct.replace("%20", "-"));
		sb.append(SEARCH_PARAMETER.replace("%r", parsedProduct));
		
		return sb.toString();
	}
}