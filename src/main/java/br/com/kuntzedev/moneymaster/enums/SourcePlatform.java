package br.com.kuntzedev.moneymaster.enums;

public enum SourcePlatform {
	ALI_EXPRESS("Ali Express"),
	AMAZON("Amazon"),
	MERCADO_LIVRE("Mercado Livre"),
	SHEIN("Shein"),
	MAGAZINE_LUIZA("Magazine Luiza");
	
	private final String desc;
	
	SourcePlatform(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}