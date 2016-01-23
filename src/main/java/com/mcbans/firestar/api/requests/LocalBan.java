package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.BanResponse;

public class LocalBan implements Request {
	public LocalBan(String player, String playerIP, String playerUUID, String reason, String admin){
		Request.info.put("admin", admin);
		Request.info.put("player", player);
		Request.info.put("player_ip", playerIP);
		Request.info.put("player_uuid", playerUUID);
		Request.info.put("reason", reason);
	}
	@Override
	public String getExecution() {
		return "localBan";
	}

	@Override
	public Class getResponse() {
		return BanResponse.class;
	}

}
