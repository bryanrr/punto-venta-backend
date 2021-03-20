package com.autoservicio.puntoventa.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.autoservicio.puntoventa.dto.Productos;
import com.autoservicio.puntoventa.models.AuthenticationRequest;
import com.autoservicio.puntoventa.services.JwtBlacklistService;
import com.autoservicio.puntoventa.services.ProductosService;
import com.autoservicio.puntoventa.util.JwtUtil;


@RestController
public class ProductosRestController {

	@Autowired
	ProductosService productosService;
	
	@Autowired 
	AuthenticationManager authManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtTokenUtil;
	
	@Autowired
	JwtBlacklistService jwtBlacklistService;
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authRequest,HttpServletResponse httpResponse)throws Exception{
		try {
		authManager.authenticate(
			new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}catch(BadCredentialsException e) {
			httpResponse.sendError(401);
		}
		
		final UserDetails userDetails=userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt=jwtTokenUtil.generateToken(userDetails);
		
		Cookie cookie=new Cookie("token",jwt);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(-1);
		
		httpResponse.addCookie(cookie);
		httpResponse.setStatus(204);
	}
	
	@GetMapping(value={"/producto/{barcode}"})
	Productos getProductByCode(@PathVariable("barcode")String barcode) {
		Productos p=productosService.getByCode(barcode);
		
		return p;
	}
	
	@GetMapping(value={"/productos/{coincidences}"})
	List<Productos> getListProducts(@PathVariable("coincidences")String concidences) {
		List<Productos> productos=productosService.getProductByCoincidences(concidences);
		
		return productos;
	}
}
