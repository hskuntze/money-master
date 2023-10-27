package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import br.com.kuntzedev.moneymaster.enums.AddressType;

@Embeddable
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	private String addressLine; //Logradouro
	private String number;
	private String district; //bairro
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String additionalDetails; //complemento
	private AddressType addressType;
	
	public Address() {
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
	public int hashCode() {
		return Objects.hash(addressLine, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(addressLine, other.addressLine) && Objects.equals(zipCode, other.zipCode);
	}

	@Override
	public String toString() {
		return "Address [addressLine=" + addressLine + ", number=" + number + ", district=" + district + ", city="
				+ city + ", state=" + state + ", zipCode=" + zipCode + "]";
	}
}