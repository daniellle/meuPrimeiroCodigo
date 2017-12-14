package br.com.ezvida.rst.dao;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResDAO {
	
	private String token;
	private String refresh;
	
	public void updateToken(String token){
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	public String getRefresh() {
		return refresh;
	}
	
	public void updateRefresh(String token) {
		this.refresh = token;
	}

}
