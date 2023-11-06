package br.com.kuntzedev.moneymaster.services.builders;

public class LinkBuilder {

	private String urlParam;
	private String searchParam;
	private String searchValue;
	private String prefixParam;
	private String prefixValue;
	private String refParam;
	private String mkPtBrParam;
	private String primeParam;
	private String freeShipingParam;
	
	public LinkBuilder(String urlParam, String searchParam, String searchValue) {
		this.urlParam = urlParam;
		this.searchParam = searchParam;
		this.searchValue = searchValue;
	}
	
	public LinkBuilder setPrefixParam(String prefix) {
		this.prefixParam = prefix;
		return this;
	}
	
	public LinkBuilder setPrefixValue(String value) {
		this.prefixValue = value;
		return this;
	}
	
	public LinkBuilder setRefParam(String ref) {
		this.refParam = ref;
		return this;
	}
	
	public LinkBuilder setMkPtBrParam(String ptBr) {
		this.mkPtBrParam = ptBr;
		return this;
	}
	
	public LinkBuilder setPrimeParam(String prime) {
		this.primeParam = prime;
		return this;
	}
	
	public LinkBuilder setFreeShipingParam(String freeShiping) {
		this.freeShipingParam = freeShiping;
		return this;
	}
	
	public String get() {
		return (urlParam + searchParam + searchValue + mkPtBrParam + prefixParam + prefixValue + prefixParam + prefixValue + refParam).replace(" ", "");
	}
	
	public String getWithPrime() {
		return (urlParam + searchParam + searchValue + primeParam + mkPtBrParam + prefixParam + prefixValue + prefixParam + prefixValue + refParam).replace(" ", "");
	}
	
	public String getWithFreeShiping() {
		return (urlParam + searchParam + searchValue + freeShipingParam + mkPtBrParam + prefixParam + prefixValue + prefixParam + prefixValue + refParam).replace(" ", "");
	}
}