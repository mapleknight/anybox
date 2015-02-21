package com.anybox.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.anybox.service.AccountService;

public class SignInServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1511635158379530762L;
	
	private AccountService accountService = new AccountService();
	
	/**
	 * response to /signin -post 
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=utf-8");
	    
	    String name = request.getParameter("name");
	    String password = request.getParameter("password");
	    
	    
	    JSONObject result = accountService.checkUser(name, password);
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
	
	

}
