package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.ActionResponse;

public class Unban implements Request {
	public Unban(String player, String admin){
		this.info.put("player", player);
		this.info.put("admin", admin);
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
