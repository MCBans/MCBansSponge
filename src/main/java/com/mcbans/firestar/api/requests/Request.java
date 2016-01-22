package com.mcbans.firestar.api.requests;

import java.util.HashMap;
import java.util.Map;

import com.mcbans.firestar.api.responses.Response;

public interface Request<T> {
	Map<String,String> info = new HashMap<String,String>();
	public String getExecution();
	public Class<T> getResponse();
}
