package br.com.kuntzedev.moneymaster.enums;

public enum AddressType {
	RESIDENTIAL("Residencial"),
	COMERCIAL("Comercial"),
	DELIVERY("Entrega");
	
	private final String desc;
	
	AddressType(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
