package com.mcbans.firestar.api.requests;

import com.mcbans.firestar.api.responses.PlayerLookupResponse;

public class LookupPlayer implements Request {
	public LookupPlayer(String player, String playerUUID, String admin){
		Request.info.put("admin", admin);
		Request.info.put("player", player);
		Request.info.put("player_uuid", playerUUID);
	}
	@Override
	public String getExecution() {
		return "playerLookup";
	}

	@Override
	public Class getResponse() {
		return PlayerLookupResponse.class;
	}

}
