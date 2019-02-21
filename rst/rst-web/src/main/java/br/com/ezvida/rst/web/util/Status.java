package br.com.ezvida.rst.web.util;

import java.io.Serializable;

public class Status implements Serializable {
	
	private static final long serialVersionUID = -2817876159638011618L;
	
	private String status;
	
	private String version;
	
	public Status(String status, String version) {
		this.status = status;
		this.version = version;
	}
	
	public Status() {
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
