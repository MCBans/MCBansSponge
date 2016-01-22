package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.BanResponse;

public class GlobalBan implements Request {
	public GlobalBan(String player, String playerIP, String playerUUID, String reason, String admin){
		this.info.put("admin", admin);
		this.info.put("player", player);
		this.info.put("player_ip", playerIP);
		this.info.put("player_uuid", playerUUID);
		this.info.put("reason", reason);
	}
	@Override
	public String getExecution() {
		return "globalBan";
	}

	@Override
	public Class getResponse() {
		return BanResponse.class;
	}
	
}
