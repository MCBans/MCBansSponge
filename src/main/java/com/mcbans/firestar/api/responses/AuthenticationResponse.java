package com.mcbans.firestar.api.responses;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticationResponse extends Response{
	public String banStatus;
	public String ban;
	public int banId;
	public String banAdmin;
	public String banType;
	public double playerRep;
	public int disputeCount;
	public String connectMessage;
	public ArrayList<HashMap<String, String>> bans;
	public ArrayList<String> nameChanges;
	public ArrayList<String> dnsbl;
	public ArrayList<String> altList;
	public int altCount;
	public boolean premium;
	public String banReason;
	public String is_mcbans_mod;
	public String getBanStatus() {
		return banStatus;
	}
	public void setBanStatus(String banStatus) {
		this.banStatus = banStatus;
	}
	public String getBan() {
		return ban;
	}
	public void setBan(String ban) {
		this.ban = ban;
	}
	public int getBanId() {
		return banId;
	}
	public void setBanId(int banId) {
		this.banId = banId;
	}
	public String getBanAdmin() {
		return banAdmin;
	}
	public void setBanAdmin(String banAdmin) {
		this.banAdmin = banAdmin;
	}
	public String getBanType() {
		return banType;
	}
	public void setBanType(String banType) {
		this.banType = banType;
	}
	public double getPlayerRep() {
		return playerRep;
	}
	public void setPlayerRep(double playerRep) {
		this.playerRep = playerRep;
	}
	public int getDisputeCount() {
		return disputeCount;
	}
	public void setDisputeCount(int disputeCount) {
		this.disputeCount = disputeCount;
	}
	public String getConnectMessage() {
		return connectMessage;
	}
	public void setConnectMessage(String connectMessage) {
		this.connectMessage = connectMessage;
	}
	public ArrayList<HashMap<String, String>> getBans() {
		return bans;
	}
	public void setBans(ArrayList<HashMap<String, String>> bans) {
		this.bans = bans;
	}
	public ArrayList<String> getNameChanges() {
		return nameChanges;
	}
	public void setNameChanges(ArrayList<String> nameChanges) {
		this.nameChanges = nameChanges;
	}
	public ArrayList<String> getDnsbl() {
		return dnsbl;
	}
	public void setDnsbl(ArrayList<String> dnsbl) {
		this.dnsbl = dnsbl;
	}
	public ArrayList<String> getAltList() {
		return altList;
	}
	public void setAltList(ArrayList<String> altList) {
		this.altList = altList;
	}
	public int getAltCount() {
		return altCount;
	}
	public void setAltCount(int altCount) {
		this.altCount = altCount;
	}
	public boolean isPremium() {
		return premium;
	}
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	public String getBanReason() {
		return banReason;
	}
	public void setBanReason(String banReason) {
		this.banReason = banReason;
	}
	public String getIs_mcbans_mod() {
		return is_mcbans_mod;
	}
	public void setIs_mcbans_mod(String is_mcbans_mod) {
		this.is_mcbans_mod = is_mcbans_mod;
	}
}
