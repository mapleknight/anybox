package com.anybox.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anybox.beans.ReturnObject;
import com.anybox.beans.User;
import com.anybox.common.StateCode;
import com.anybox.service.AccountService;

import net.sf.json.JSONObject;

public class UserInfoServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4736933731884498887L;
	
	private AccountService accountService = new AccountService();
	
	/**
	 * get user info
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=utf-8");
		
	    String id = request.getParameter("id");
	    String name = request.getParameter("name");
	    
	    
	    JSONObject result = accountService.queryUser(id, name);
	    PrintWriter out = null;
	    try {
			out = response.getWriter();
			out.append(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		
	}
	
	/**
	 * update user info
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=utf-8");
		
	    PrintWriter out = null;
		JSONObject result = null;

		try {
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
        	    String first_name = jo.getString("first_name");
        	    String last_name = jo.getString("last_name");
        	    String email = jo.getString("email");
                
        	    User user = new User();
        	    user.setId(Integer.valueOf(id));
        	    user.setName(name);
        	    user.setEmail(email);
        	    user.setFirst_name(first_name);
        	    user.setLast_name(last_name);
    			
    			result = accountService.updateUser(user);
            }

            out = response.getWriter();
			
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
