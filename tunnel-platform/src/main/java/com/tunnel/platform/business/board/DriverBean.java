package com.tunnel.platform.business.board;

import java.util.HashMap;
import java.util.Map;

public class DriverBean {

	private static final long serialVersionUID = 1L;
	
	private String vender_version = null;
	private Map<String, String> commands = new HashMap<String, String>();
	private Map<String, String> rules = new HashMap<String, String>();

	public DriverBean() {
	}

	public String getVender_version() {
		return vender_version;
	}

	public void setVender_version(String vender_version) {
		this.vender_version = vender_version;
	}

	public Map<String, String> getCommands() {
		return commands;
	}

	public void setCommands(Map<String, String> commands) {
		this.commands = commands;
	}

	public Map<String, String> getRules() {
		return rules;
	}

	public void setRules(Map<String, String> rules) {
		this.rules = rules;
	}

}