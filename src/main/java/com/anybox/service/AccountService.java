package com.anybox.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.anybox.beans.ReturnObject;
import com.anybox.beans.User;
import com.anybox.common.StateCode;
import com.anybox.common.StripeUtil;
import com.anybox.database.DBConnectionPool;
import com.anybox.database.SqlStatement;

import net.sf.json.JSONObject;

public class AccountService {
	
	StripeUtil su = new StripeUtil();
	
	/**
	 * get user info by name and password can be used as signin
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public JSONObject checkUser(String name, String password) {

		ReturnObject ret = new ReturnObject();

		String sql = SqlStatement.GetUserInfoByNamePwd;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<User> list = (List<User>) qRunner.query(sql,
					new BeanListHandler(User.class), name, password);

			// user exists, state = success
			if (list.size() > 0) {
				User user = list.get(0);
				ret.setState(StateCode.SUCCESS);
				JSONObject userJson = new JSONObject();
				userJson.put("id", user.getId());
				userJson.put("name", user.getName());
				ret.setContent(userJson);
			}
			// user not exists, state = failed
			else {
				ret.setState(StateCode.FAILED_USER_NOT_EXIST);
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		} catch (Exception e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_SYSTEM);
		}

		return JSONObject.fromObject(ret);
	}

	/**
	 * register user by name, password
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public JSONObject registerUser(String name, String password) {

		ReturnObject ret = new ReturnObject();

		// check whether user name exists in database
		// if exist, can not register
		if (checkUserName(name)) {
			ret.setState(StateCode.FAILED_USER_NAME_EXISTS);
			return JSONObject.fromObject(ret);
		}
		
		String cus_id = su.createCustomer(name);
		
		if(null == cus_id)
		{
			ret.setState(StateCode.FAILED_SYSTEM);
			ret.getContent().put("status", "stripe_failed");
			return JSONObject.fromObject(ret);
		}

		// if not exist, continue to register user
		String sql = SqlStatement.InsertUserInfo;

		try {

			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			int result = qRunner.update(sql, name, password, cus_id);

			// insert success
			if (result > 0) {
				ret.setState(StateCode.SUCCESS);
			}
			// user not exists, state = 0
			else {
				ret.setState(StateCode.FAILED_DATABASE);
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		} catch (Exception e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_SYSTEM);
		}

		return JSONObject.fromObject(ret);
	}
	
	/**
	 * get all user info by id and name
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public JSONObject queryUser(String id, String name) {

		ReturnObject ret = new ReturnObject();

		String sql = SqlStatement.GetAllUserInfoByIdName;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<User> list = (List<User>) qRunner.query(sql,
					new BeanListHandler(User.class), Integer.valueOf(id), name);

			// user exists, state = success
			if (list.size() > 0) {
				User user = list.get(0);
				ret.setState(StateCode.SUCCESS);
				ret.setContent(JSONObject.fromObject(user));
			}
			// user not exists, state = failed
			else {
				ret.setState(StateCode.FAILED_USER_NOT_EXIST);
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		} catch (Exception e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_SYSTEM);
		}

		return JSONObject.fromObject(ret);
	}

	/**
	 * check if name exists in database
	 * 
	 * @param name
	 * @return
	 */
	private boolean checkUserName(String name) {

		Connection conn = null;
		String sql = SqlStatement.GetUserInfoByName;
		try {
			conn = DBConnectionPool.getInstance().getDataSource()
					.getConnection();

			QueryRunner qRunner = new QueryRunner();

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<User> list = (List<User>) qRunner.query(conn, sql,
					new BeanListHandler(User.class), name);

			// user exists
			if (list.size() > 0) {
				return true;
			}
			// user not exists
			else {
				return false;
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			return true;
		}

	}

	/**
	 * update user info
	 * @param user
	 * @return
	 */
	public JSONObject updateUser(User user) {
		String partSql = "";
		
		partSql = "email='" + user.getEmail() + "',";
		partSql = partSql + "first_name='" + user.getFirst_name() + "',";
		partSql = partSql + "last_name='" + user.getLast_name() + "'";
		
		partSql = partSql + " where id=? and name=?";
		
		ReturnObject ret = new ReturnObject();

		String sql = SqlStatement.UpdateUserInfoById + partSql;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			int result = qRunner.update(sql, user.getId(), user.getName());

			// insert success
			if (result > 0) {
				ret.setState(StateCode.SUCCESS);
			}
			// user not exists, state = 0
			else {
				ret.setState(StateCode.FAILED_DATABASE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		} catch (Exception e) {
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_SYSTEM);
		}

		return JSONObject.fromObject(ret);
	}
	
	/**
	 * get costomer id by id and name
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public String getCusIdByIdName(String id, String name) {

		String sql = SqlStatement.GetCusIdByIdName;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<User> list = (List<User>) qRunner.query(sql,
					new BeanListHandler(User.class), Integer.valueOf(id), name);

			// user exists, state = success
			if (list.size() > 0) {
				User user = list.get(0);
				return user.getCus_id();
			}
			

		} catch (SQLException e) {
			 e.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}

		return null;
	}
	

}
