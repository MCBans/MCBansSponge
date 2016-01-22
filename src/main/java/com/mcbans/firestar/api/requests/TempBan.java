package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.BanResponse;

public class TempBan implements Request {
	public TempBan(String player, String playerIP, String playerUUID, String number, String units, String reason, String admin){
		this.info.put("admin", admin);
		this.info.put("player", player);
		this.info.put("player_ip", playerIP);
		this.info.put("player_uuid", playerUUID);
		this.info.put("reason", reason);
		this.info.put("duration", number);
		this.info.put("measure", units);
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
