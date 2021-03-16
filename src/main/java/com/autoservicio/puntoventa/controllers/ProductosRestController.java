package com.autoservicio.puntoventa.controllers;

import java.net.http.HttpResponse;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.autoservicio.puntoventa.models.AuthenticationResponse;
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
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity <String>createAuthenticationToken(@RequestBody AuthenticationRequest authRequest,HttpServletResponse httpResponse)throws Exception{
		try {
		authManager.authenticate(
			new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}catch(BadCredentialsException e) {
			return new ResponseEntity<String>("incorrect",HttpStatus.OK);
		}
		
		final UserDetails userDetails=userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt=jwtTokenUtil.generateToken(userDetails);
		
		Cookie cookie=new Cookie("token",jwt);
		cookie.setHttpOnly(true);
		httpResponse.addCookie(cookie);
		
		return new ResponseEntity<String>("",HttpStatus.OK);
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
