package com.anybox.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anybox.beans.CardAnybox;
import com.anybox.beans.ReturnObject;
import com.anybox.common.StateCode;
import com.anybox.service.CardService;

import net.sf.json.JSONObject;

public class CardServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6115876601190159189L;
	
	CardService cardService = new CardService();
	
	/**
	 * create card
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String requestUrl = request.getRequestURL().toString();
		
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=utf-8");
		
		if(requestUrl.endsWith("create"))
		{
			createCard(request, response);
		}
		else if(requestUrl.endsWith("delete"))
		{
			deleteCard(request, response);
		} else if(requestUrl.endsWith("query"))
		{
			queryCards(request, response);
		}
		
	}
	
	private void queryCards(HttpServletRequest request,
			HttpServletResponse response) {
		
		PrintWriter out = null;
		JSONObject result = null;
		
		try {
			out = response.getWriter();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			
			
			String acceptjson = sb.toString();  
            if (acceptjson != "") {  
                JSONObject jo = JSONObject.fromObject(acceptjson);
                
                String id = jo.getString("id");
        	    String name = jo.getString("name");
                
    			result = cardService.queryCards(id, name);
            }
			
		} catch (Exception e) {
			//e.printStackTrace();
			result = JSONObject.fromObject(new ReturnObject(StateCode.FAILED_SYSTEM));
		} finally {
			result.remove("content");
			out.append(result.toString());
			
			if (out != null) {
				out.close();
			}
		}
		
	}

	private void createCard(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		JSONObject result = null;

		try {
			out = response.getWriter();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			
			
			String acceptjson = sb.toString();
            if (acceptjson != "") {  
                JSONObject jo = JSONObject.fromObject(acceptjson);
                
                String id = jo.getString("id");
        	    String name = jo.getString("name");
        	    String number = jo.getString("number");
        	    int exp_month = jo.getInt("exp_month");
        	    int exp_year = jo.getInt("exp_year");
        	    String cvc = jo.getString("cvc");
        	    String address_zip = jo.getString("address_zip");
                
        	    CardAnybox card = new CardAnybox();
        	    card.setNumber(number);
        	    card.setExp_year(exp_year);
        	    card.setExp_month(exp_month);
        	    card.setCvc(cvc);
        	    card.setAddress_zip(address_zip);
    			
    			result = cardService.createCard(id, name, card);
            }

		} catch (Exception e) {
			//e.printStackTrace();
			result = JSONObject.fromObject(new ReturnObject(StateCode.FAILED_SYSTEM));
		} finally {
			result.remove("content");
			out.append(result.toString());
			
			if (out != null) {
				out.close();
			}
		}
	}
	
	private void deleteCard(HttpServletRequest request, HttpServletResponse response) {
		
	    PrintWriter out = null;
		JSONObject result = null;
		
		
		try {
			out = response.getWriter();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			
			
			String acceptjson = sb.toString();  
            if (acceptjson != "") {  
                JSONObject jo = JSONObject.fromObject(acceptjson);
                
                String id = jo.getString("id");
        	    String name = jo.getString("name");
        	    String card_id = jo.getString("card_id");
                
    			result = cardService.deleteCard(id, name, card_id);
            }
			
		} catch (Exception e) {
			//e.printStackTrace();
			result = JSONObject.fromObject(new ReturnObject(StateCode.FAILED_SYSTEM));
		} finally {
			result.remove("content");
			out.append(result.toString());
			
			if (out != null) {
				out.close();
			}
		}
		
	}

}
