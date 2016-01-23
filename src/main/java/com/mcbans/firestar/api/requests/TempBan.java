package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.BanResponse;

public class TempBan implements Request {
	public TempBan(String player, String playerIP, String playerUUID, String number, String units, String reason, String admin){
		Request.info.put("admin", admin);
		Request.info.put("player", player);
		Request.info.put("player_ip", playerIP);
		Request.info.put("player_uuid", playerUUID);
		Request.info.put("reason", reason);
		Request.info.put("duration", number);
		Request.info.put("measure", units);
	}

	@Override
	public String getExecution() {
		return "tempBan";
	}

	@Override
	public Class getResponse() {
		return BanResponse.class;
	}
}
