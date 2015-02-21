package com.anybox.beans;

import net.sf.json.JSONObject;

public class ReturnObject {
	
	private int state;
	
	private JSONObject content;
	
	public ReturnObject(){}
	
	public ReturnObject(int state)
	{
		this.state = state;
	}
	
	public ReturnObject(int state, JSONObject content)
	{
		this.state = state;
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}
	
	

}
