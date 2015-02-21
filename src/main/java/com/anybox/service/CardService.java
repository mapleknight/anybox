package com.anybox.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.anybox.beans.CardAnybox;
import com.anybox.beans.ReturnObject;
import com.anybox.common.StateCode;
import com.anybox.common.StripeUtil;
import com.anybox.database.DBConnectionPool;
import com.anybox.database.SqlStatement;
import com.stripe.exception.CardException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CardService {

	StripeUtil su = new StripeUtil();

	AccountService as = new AccountService();

	/**
	 * create card for customer/user
	 * 
	 * @param id
	 * @param name
	 * @param card
	 * @return
	 */
	public JSONObject createCard(String id, String name, CardAnybox card) {

		ReturnObject ret = new ReturnObject();

		// step 1. get customer id in stripe
		String cus_id = as.getCusIdByIdName(id, name);

		if (null == cus_id) {
			ret.setState(StateCode.FAILED_DATABASE);
			return JSONObject.fromObject(ret);
		}

		String card_id = null;
		try {
			card_id = su.createCard(cus_id, card);
			if (null == card_id) {
				ret.setState(StateCode.FAILED_SYSTEM);
				ret.getContent().put("status", "stripe_failed");
				return JSONObject.fromObject(ret);
			}

			String sql = SqlStatement.InsertCard;

			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());
			int result = qRunner
					.update(sql, card.getNumber(), card.getExp_month(),
							card.getExp_year(), card.getCvc(),
							card.getAddress_zip(), card_id, cus_id,
							Integer.valueOf(id));

			// insert success
			if (result > 0) {
				ret.setState(StateCode.SUCCESS);
			}
			// user not exists, state = 0
			else {
				ret.setState(StateCode.FAILED_DATABASE);
			}

		} catch (CardException e) {
			// Since it's a decline, CardException will be caught
			System.out.println("Status is: " + e.getCode());
			System.out.println("Message is: " + e.getParam());

			ret.setState(StateCode.CARD_ERROR);
			ret.getContent().put("status", e.getCode());
			ret.getContent().put("message", e.getParam());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		}

		return JSONObject.fromObject(ret);
	}

	/**
	 * delete card in stripe and anybox
	 * 
	 * @param user_id
	 *            : user_id in table card
	 * @param name
	 * @param card_anybox_id
	 *            : id in table card
	 * @return
	 */
	public JSONObject deleteCard(String user_id, String name,
			String card_anybox_id) {

		ReturnObject ret = new ReturnObject();

		CardAnybox card = getCardInfoByIdName(user_id, card_anybox_id);

		if (null == card) {
			ret.setState(StateCode.FAILED_DATABASE);
			return JSONObject.fromObject(ret);
		}

		try {
			boolean del = su.deleteCard(card.getCus_id(), card.getCard_id());

			if (!del) {
				ret.setState(StateCode.FAILED_SYSTEM);
				ret.getContent().put("status", "stripe_failed");
				return JSONObject.fromObject(ret);
			}

			String sql = SqlStatement.DeleteCardByIdUserId;

			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());
			int result = qRunner.update(sql, Integer.valueOf(card_anybox_id),
					Integer.valueOf(user_id));

			// insert success
			if (result > 0) {
				ret.setState(StateCode.SUCCESS);
			}
			// user not exists, state = 0
			else {
				ret.setState(StateCode.FAILED_DATABASE);
			}

		} catch (CardException e) {
			// Since it's a decline, CardException will be caught
			System.out.println("Status is: " + e.getCode());
			System.out.println("Message is: " + e.getParam());

			ret.setState(StateCode.CARD_ERROR);
			ret.getContent().put("status", e.getCode());
			ret.getContent().put("message", e.getParam());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		}

		return null;
	}

	public CardAnybox getCardInfoByIdName(String user_id, String id) {

		String sql = SqlStatement.GetCardInfoByIdUserId;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<CardAnybox> list = (List<CardAnybox>) qRunner.query(sql,
					new BeanListHandler(CardAnybox.class), Integer.valueOf(id),
					Integer.valueOf(user_id));

			// user exists, state = success
			if (list.size() > 0) {
				CardAnybox card = list.get(0);

				return card;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public JSONObject queryCards(String user_id, String name) {
		
		ReturnObject ret = new ReturnObject();
		
		String sql = SqlStatement.GetCardListByUserId;
		try {
			QueryRunner qRunner = new QueryRunner(DBConnectionPool
					.getInstance().getDataSource());

			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<CardAnybox> list = (List<CardAnybox>) qRunner.query(sql,
					new BeanListHandler(CardAnybox.class), Integer.valueOf(user_id));

			JSONArray cardArr = new JSONArray();
			
			for(CardAnybox card : list){
				cardArr.add(JSONObject.fromObject(card));
			}
			
			ret.setState(StateCode.SUCCESS);
			JSONObject cardsJson = new JSONObject();
			cardsJson.put("cards", cardArr);
			ret.setContent(cardsJson);
			

		} catch (SQLException e) {
			e.printStackTrace();
			ret.setState(StateCode.FAILED_DATABASE);
		} catch (Exception e) {
			e.printStackTrace();
			ret.setState(StateCode.FAILED_SYSTEM);
		}

		return JSONObject.fromObject(ret);
	}

	public static void main(String[] args) {
		CardService ps = new CardService();
		ps.createCard(null, null, null);
	}

}
