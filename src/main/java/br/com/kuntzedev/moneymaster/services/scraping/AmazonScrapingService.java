package br.com.kuntzedev.moneymaster.services.scraping;

import java.io.IOException;
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

import br.com.kuntzedev.moneymaster.dtos.AmazonItemDTO;
import br.com.kuntzedev.moneymaster.services.builders.LinkBuilder;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.AmazonConnectionException;
import br.com.kuntzedev.moneymaster.services.scraping.exceptions.InvalidLinkException;

@Service
public class AmazonScrapingService {

	/**
	 * Parâmetros de: pesquisa
	 */
	private static final String BASE_URL = "https://www.amazon.com.br";
	private static final String SEARCH_PARAMETER = "/s?k=";
	private static final String REF_PARAMETER = "&ref=nb_sb_noss";
	private static final String MK_PT_BR_PARAMETER = "&__mk_pt_BR=ÅMÅŽÕÑ";
	private static final String PREFIX_PARAMETER = "&sprefix=";
	private static final String PRIME_PARAMETER = "&rh=p_85%3A19171728011";
	private static final String FREE_SHIPING_PARAMETER = "&rh=p_n_free_shipping_eligible%3A19171733011";
	
	/**
	 * Parâmetros de: preços
	 */
	private static final String FULL_PRICE = "span[class=a-offscreen]";
	
	/**
	 * Parâmetros de: acesso
	 */
	private static final String DIV_PRODUCT = "div[class*=sg-col-4-of-24 sg-col-4-of-12 s-result-item s-asin sg-col-4-of-16]";
	private static final String DIV_PRODUCT_TITLE = "a[class=a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal]";
	private static final String DIV_PRODUCT_LINK = "a[class=a-link-normal s-no-outline]";
	private static final String DIV_PRODUCT_IMAGE = "div[class=a-section aok-relative s-image-square-aspect]";
	private static final String IMG_TAG = "img[class=s-image]";
	private static final String TITLE_TAG = "span[class=a-size-base-plus a-color-base a-text-normal]";
	
	public Page<AmazonItemDTO> searchForProduct(String product, int page, int size, String sort, boolean prime, boolean freeShiping) {
		Document document = null;
		
		try {
			document = Jsoup.connect(formatLinkToSearch(product, prime, freeShiping)).get();
			System.out.println(formatLinkToSearch(product, prime, freeShiping));
			
			Elements products = getProducts(document);
			List<AmazonItemDTO> items = new ArrayList<>();
			
			for(int i = 0; i < products.size(); i++) {
				AmazonItemDTO item = new AmazonItemDTO();
				
				item.setName(getProductTitle(document, i));
				item.setPrice(getProductPrice(document, i));
				item.setImage(getImageLink(document, i));
				item.setLink(getProductLink(document, i));
				items.add(item);
			}
			
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort)));
			return new PageImpl<AmazonItemDTO>(items, pageRequest, items.size());
		} catch (HttpStatusException e) {
			String message = e.getMessage();
			String status = e.getMessage().substring(message.indexOf("=") + 1, message.indexOf(","));
			
			throw new AmazonConnectionException("Something went wrong while trying to search for " + product, e, Integer.valueOf(status));
		} catch (IOException e) {
			throw new AmazonConnectionException(e.getMessage());
		}
	}

	private Elements getProducts(Document doc) {
		return doc.select(DIV_PRODUCT);
	}
	
	private String getProductTitle(Document doc, int index) {
		int i = index == 0 ? 0 : index;
		return doc.select(DIV_PRODUCT).get(i).select(DIV_PRODUCT_TITLE).select(TITLE_TAG).text();
	}
	
	private String getProductLink(Document doc, int index) {
		int i = index == 0 ? 0 : index;
		return BASE_URL + doc.select(DIV_PRODUCT).select(DIV_PRODUCT_LINK).get(i).attr("href");
	}
	
	private String getProductPrice(Document doc, int index) {
		int i = index == 0 ? 0 : index;
		boolean empty = doc.select(DIV_PRODUCT).get(i).select(FULL_PRICE).isEmpty();
		if(!empty) {
			String full = doc.select(DIV_PRODUCT).get(i).select(FULL_PRICE).first().text().replace(".", "").replace(",", ".");
			String formatted = full.substring(full.indexOf(" ") + 1, full.length());
			return formatted;
		} else {
			return "0.0";
		}
	}
	
	private String getImageLink(Document doc, int index) {
		int i = index == 0 ? 0 : index;
		return doc.select(DIV_PRODUCT).select(DIV_PRODUCT_IMAGE).get(i).select(IMG_TAG).first().attr("src");
	}
	
	private String formatLinkToSearch(String product, boolean prime, boolean freeShiping) {
		String formattedProduct = product.replace(" ", "+").replace("-", "+");
		
		String formattedPrefix = "";
		int firstSpace = product.indexOf(" ");
		if(firstSpace != -1) {
			int secondSpace = product.indexOf(" ", firstSpace + 1);
			
			if(secondSpace != -1) {
				formattedPrefix = product.substring(0, secondSpace).replace(" ", "+").replace("-", "+");
			} else {
				formattedPrefix = product.substring(0, firstSpace).replace(" ", "+").replace("-", "+");
			}
		}
		
		if(!prime && !freeShiping) {
			return new LinkBuilder(BASE_URL, SEARCH_PARAMETER, formattedProduct).setMkPtBrParam(MK_PT_BR_PARAMETER).setPrefixParam(PREFIX_PARAMETER).setPrefixValue(formattedPrefix).setRefParam(REF_PARAMETER).get();
		} else if(prime && !freeShiping) {
			return new LinkBuilder(BASE_URL, SEARCH_PARAMETER, formattedProduct).setMkPtBrParam(MK_PT_BR_PARAMETER).setPrefixParam(PREFIX_PARAMETER).setPrefixValue(formattedPrefix).setRefParam(REF_PARAMETER).setPrimeParam(PRIME_PARAMETER).getWithPrime();
		} else if(freeShiping && !prime){
			return new LinkBuilder(BASE_URL, SEARCH_PARAMETER, formattedProduct).setMkPtBrParam(MK_PT_BR_PARAMETER).setPrefixParam(PREFIX_PARAMETER).setPrefixValue(formattedPrefix).setRefParam(REF_PARAMETER).setFreeShipingParam(FREE_SHIPING_PARAMETER).getWithFreeShiping();
		} else {
			throw new InvalidLinkException("Either select prime or free shiping.");
		}
	}
}