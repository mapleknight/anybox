package com.anybox.database;

public class SqlStatement {
	
	public static String GetUserInfoByNamePwd = "SELECT id,name FROM anybox.user where name=? and password=?;";
	
	public static String GetUserInfoByName = "SELECT name FROM anybox.user where name=?;";
	
	public static String InsertUserInfo = "INSERT INTO anybox.user (name,password,cus_id) VALUES (?,?,?);";
	
	public static String GetUserInfoByIdName = "SELECT id FROM anybox.user where id=? and name=?;";
	
	public static String GetAllUserInfoByIdName = "SELECT * FROM anybox.user where id=? and name=?;";
	
	public static String GetCusIdByIdName = "SELECT cus_id FROM anybox.user where id=? and name=?;";
	
	public static String UpdateUserInfoById = "UPDATE anybox.user SET ";
	
	public static String InsertCard = "INSERT INTO anybox.card (number,exp_month,exp_year,cvc,address_zip,card_id,cus_id,user_id) VALUES (?,?,?,?,?,?,?,?);";
	
	public static String DeleteCardByIdUserId = "DELETE FROM anybox.card where id=? and user_id=?;";
	
	public static String GetCardInfoByIdUserId = "SELECT * FROM anybox.card where id=? and user_id=?;";
	
	public static String GetCardListByUserId = "SELECT * FROM anybox.card where user_id=?;";
}
