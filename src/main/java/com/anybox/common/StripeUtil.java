package com.anybox.common;

import java.util.HashMap;
import java.util.Map;

import com.anybox.beans.CardAnybox;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.Token;

/**
 * 
 * @author wangke
 *
 */
public class StripeUtil {

	private static final String KEY = "sk_test_Z4ww2uWAUNnPoh0qi5MAU6Ox";

	/**
	 * 
	 * @param name
	 * @return customer id in stripe
	 */
	public String createCustomer(String name) {
		Stripe.apiKey = KEY;

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("description", name);

		try {
			// Use Stripe's bindings...
			Customer cus = Customer.create(customerParams);

			return cus.getId();
		} catch (InvalidRequestException e) {
			// Invalid parameters were supplied to Stripe's API
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// Authentication with Stripe's API failed
			// (maybe you changed API keys recently)
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// Network communication with Stripe failed
			e.printStackTrace();
		} catch (StripeException e) {
			// Display a very generic error to the user, and maybe send
			// yourself an email
			e.printStackTrace();
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param cus_id
	 * @param cd
	 * @return card id in stripe
	 * @throws CardException
	 */
	public String createCard(String cus_id, CardAnybox cd) throws CardException {
		Stripe.apiKey = KEY;

		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("number", cd.getNumber());
		cardParams.put("exp_month", cd.getExp_month());
		cardParams.put("exp_year", cd.getExp_year());
		cardParams.put("cvc", cd.getCvc());
		cardParams.put("address_zip", cd.getAddress_zip());
		tokenParams.put("card", cardParams);

		try {
			// Use Stripe's bindings...
			Token token = Token.create(tokenParams);

			Customer cu = Customer.retrieve(cus_id);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("card", token.getId());
			Card card = cu.createCard(params);
			String card_id = card.getId();
			System.out.println(card.getId());

			return card_id;
		}
		// catch (CardException e) {
		// // Since it's a decline, CardException will be caught
		// System.out.println("Status is: " + e.getCode());
		// System.out.println("Message is: " + e.getParam());
		//
		// }
		catch (InvalidRequestException e) {
			// Invalid parameters were supplied to Stripe's API
		} catch (AuthenticationException e) {
			// Authentication with Stripe's API failed
			// (maybe you changed API keys recently)
		} catch (APIConnectionException e) {
			// Network communication with Stripe failed
		} catch (StripeException e) {
			// Display a very generic error to the user, and maybe send
			// yourself an email
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
		}

		return null;
	}

	public boolean deleteCard(String cus_id, String card_id)  throws CardException {
		Stripe.apiKey = KEY;
		
		try {
			Customer cu = Customer.retrieve(cus_id);
			for (Card card : cu.getCards().getData()) {
				if (card.getId().equals(card_id)) {
					card.delete();
					break;
				}
			}
			return true;
		} catch (InvalidRequestException e) {
			// Invalid parameters were supplied to Stripe's API
		} catch (AuthenticationException e) {
			// Authentication with Stripe's API failed
			// (maybe you changed API keys recently)
		} catch (APIConnectionException e) {
			// Network communication with Stripe failed
		} catch (StripeException e) {
			// Display a very generic error to the user, and maybe send
			// yourself an email
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
		}
		return false;
	}

	public static void main(String[] args) {
		StripeUtil su = new StripeUtil();

		String id = su.createCustomer("testaaa");

		System.out.println(id);

	}

}
