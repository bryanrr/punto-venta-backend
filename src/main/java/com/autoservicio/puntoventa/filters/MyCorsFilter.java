package com.autoservicio.puntoventa.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class MyCorsFilter extends CorsFilter{

	public MyCorsFilter(CorsConfigurationSource configSource) {
		super(configSource);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(isPreFlight(request)) {
			response.setHeader("Access-Control-Allow-Credentials", "true");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
	        response.setHeader("Access-Control-Max-Age", String.valueOf(60*60));
	        response.setHeader("Access-Control-Allow-Headers","Content-Type");
		}

        response.setHeader("Access-Control-Allow-Origin", "*");
        
        filterChain.doFilter(request, response);
	}
	
	private Boolean isPreFlight(HttpServletRequest httpServletRequest) {
		String httpMethod=httpServletRequest.getMethod();
		String originHeader=httpServletRequest.getHeader("origin");
		String requestMethod=httpServletRequest.getHeader("access-control-request-method");
		
		if(httpMethod.equalsIgnoreCase("options") && originHeader!=null && requestMethod!=null) {
			return true;
		}
		
		return false;
	}
}
