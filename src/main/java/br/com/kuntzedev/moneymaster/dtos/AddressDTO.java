package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;

import br.com.kuntzedev.moneymaster.entities.Address;
import br.com.kuntzedev.moneymaster.enums.AddressType;

public class AddressDTO implements Serializable {
	private static final long serialVersionUID = -7243842609474326837L;
	
	private String addressLine;
	private String number;
	private String district;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String additionalDetails;
	private AddressType addressType;
	
	public AddressDTO() {
	}
	
	public AddressDTO(Address address) {
		this.addressLine = address.getAddressLine();
		this.number = address.getNumber();
		this.district = address.getDistrict();
		this.city = address.getCity();
		this.state = address.getState();
		this.zipCode = address.getZipCode();
		this.country = address.getCountry();
		this.additionalDetails = address.getAdditionalDetails();
		this.addressType = address.getAddressType();
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	@Override
	public String toString() {
		return "AddressDTO [addressLine=" + addressLine + ", number=" + number + ", district=" + district + ", city="
				+ city + ", state=" + state + ", zipCode=" + zipCode + ", country=" + country + ", additionalDetails="
				+ additionalDetails + ", addressType=" + addressType + "]";
	}
}