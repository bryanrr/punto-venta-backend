package com.autoservicio.puntoventa.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.autoservicio.puntoventa.util.RegexpUtil;

public class AuthenticationRequest {
	@Pattern(regexp=RegexpUtil.USERNAME)
	private String username;
	@Pattern(regexp=RegexpUtil.PASSWORD)
	private String password;
	
	public AuthenticationRequest() {
		
	}
	
	
	public AuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
