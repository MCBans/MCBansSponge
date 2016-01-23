package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.ActionResponse;

public class Unban implements Request {
	public Unban(String player, String admin){
		Request.info.put("player", player);
		Request.info.put("admin", admin);
	}
	@Override
	public String getExecution() {
		return "unBan";
	}

	@Override
	public Class getResponse() {
		return ActionResponse.class;
	}

}
