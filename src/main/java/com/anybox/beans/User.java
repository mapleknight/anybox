package com.anybox.beans;


public class User {
	
	private int id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String first_name;
	
	private String last_name;
	
	/*
	 * customer id in stripe
	 */
	private String cus_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	@Override
	public String toString() {
		return 	"id:" + id + ";" + 
				"name:" + name + ";"+
				"address:" + email + ";" +
				"password:" + password + ";" + 
				"first_name:" + first_name + ";" +
				"last_name:" + last_name + ";";
	}
	public String getCus_id() {
		return cus_id;
	}
	public void setCus_id(String cus_id) {
		this.cus_id = cus_id;
	}
}