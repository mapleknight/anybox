package com.anybox.beans;

public class CardAnybox {
	
	/*
	 * The card number, as a string without any separators
	 */
	private String number;
	
	/*
	 * Two digit number representing the card's expiration month
	 */
	private int exp_month;
	
	/*
	 * Two or four digit number representing the card's expiration year
	 */
	private int exp_year;
	
	/*
	 * Card security code
	 */
	private String cvc;
	
	/*
	 * address zip
	 */
	private String address_zip;
	
	/*
	 * card id in stripe
	 */
	private String card_id;
	
	/*
	 * customer id in stripe
	 */
	private String cus_id;
	
	/*
	 * user id in anybox
	 */
	private String user_id;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getExp_month() {
		return exp_month;
	}

	public void setExp_month(int exp_month) {
		this.exp_month = exp_month;
	}

	public int getExp_year() {
		return exp_year;
	}

	public void setExp_year(int exp_year) {
		this.exp_year = exp_year;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public String getAddress_zip() {
		return address_zip;
	}

	public void setAddress_zip(String address_zip) {
		this.address_zip = address_zip;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCus_id() {
		return cus_id;
	}

	public void setCus_id(String cus_id) {
		this.cus_id = cus_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
