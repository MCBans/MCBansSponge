package com.mcbans.firestar.api.responses;

import java.util.ArrayList;

public class PlayerLookupResponse {
	public String total;
	public double reputation;
	public ArrayList<String> local;
	public ArrayList<String> global;
	public ArrayList<String> other;
	public int pid;
	public String uuid;
	public String player;
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public double getReputation() {
		return reputation;
	}
	public void setReputation(double reputation) {
		this.reputation = reputation;
	}
	public ArrayList<String> getLocal() {
		return local;
	}
	public void setLocal(ArrayList<String> local) {
		this.local = local;
	}
	public ArrayList<String> getGlobal() {
		return global;
	}
	public void setGlobal(ArrayList<String> global) {
		this.global = global;
	}
	public ArrayList<String> getOther() {
		return other;
	}
	public void setOther(ArrayList<String> other) {
		this.other = other;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
}
