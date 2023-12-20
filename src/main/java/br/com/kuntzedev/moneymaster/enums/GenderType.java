package br.com.kuntzedev.moneymaster.enums;

public enum GenderType {
	MALE("Masculino"),
	FEMALE("Feminino"),
	NONBINARY("Não Binário"),
	TRANSGENDER("Transgênero"),
	GENDERFLUID("Gênero Fluido"),
	AGENDER("Agênero"),
	OTHER("Outro"),
	UNDEFINED("Não definido");
	
	private final String desc;
	
	GenderType(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
