package com.autoservicio.puntoventa.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.autoservicio.puntoventa.dto.Productos;
import com.autoservicio.puntoventa.models.AuthenticationRequest;
import com.autoservicio.puntoventa.models.ProductSoldPeriodRequest;
import com.autoservicio.puntoventa.models.ProductSoldPeriodResponse;
import com.autoservicio.puntoventa.services.JwtBlacklistService;
import com.autoservicio.puntoventa.services.ProductosService;
import com.autoservicio.puntoventa.util.JwtUtil;
import com.autoservicio.puntoventa.util.RegexpUtil;

import io.jsonwebtoken.JwtException;


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
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authRequest,HttpServletResponse httpResponse)throws Exception{
		Set<ConstraintViolation<AuthenticationRequest>> violations = validator.validate(authRequest);
		
		if(violations.size()==0) {
			try {
				authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
				
				final UserDetails userDetails=userDetailsService.loadUserByUsername(authRequest.getUsername());
				final String jwt=jwtTokenUtil.generateToken(userDetails);
				
				Cookie cookie=new Cookie("token",jwt);
				cookie.setHttpOnly(true);
				cookie.setMaxAge(60*60*2);
				httpResponse.addCookie(cookie);
				
				String header=httpResponse.getHeader("Set-Cookie")+"; SameSite=strict;";
				httpResponse.setHeader("Set-Cookie", header);
				httpResponse.setStatus(204);
			}catch(BadCredentialsException e) {
				httpResponse.sendError(401);
			}
		}else {
			httpResponse.sendError(400,"Username/password in bad format");
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse)throws Exception{
		Optional<String>optionalJwt=Arrays.stream(httpRequest.getCookies())
				.filter(cookie->cookie.getName().equals("token")).map(Cookie::getValue)
				.findAny();
		if(optionalJwt.isPresent()) {
			try {
				jwtTokenUtil.extractExpiration(optionalJwt.get());
				jwtBlacklistService.addJwtToBlacklist(optionalJwt.get());
				
				Cookie cookie=new Cookie("token","");
				cookie.setHttpOnly(true);
				cookie.setMaxAge(0);
				
				httpResponse.addCookie(cookie);
				httpResponse.setStatus(204);
			}catch(JwtException c) {
				httpResponse.sendError(403);
			}
			
		}else {
			httpResponse.sendError(403);
		}
	}
	
	@GetMapping(value={"/producto/{barcode}"})
	Productos getProductByCode(@PathVariable("barcode")String barcode,HttpServletResponse response) throws IOException {
		Productos p=new Productos();
		
		if(barcode.matches(RegexpUtil.BARCODE)) {
			p=productosService.getByCode(barcode);
		}else {
			response.sendError(400);
		}
		
		return p;
	}
	
	@GetMapping(value={"/productos/{coincidences}"})
	List<Productos> getListProducts(@PathVariable("coincidences")String concidences,HttpServletResponse response) throws IOException {
		List<Productos> productos=null;
		
		if(concidences.matches(RegexpUtil.DESCRIPTION)) {
			productos=productosService.getProductByCoincidences(concidences);
		}else {
			response.sendError(400);
		}
		
		return productos;
	}
	
	@PutMapping("/producto/update")
	void updateProduct(@RequestBody Productos product,HttpServletResponse httpResponse) {
		productosService.updateProduct(product);
		httpResponse.setStatus(204);
	}
	
	@PostMapping("/producto/sold")
	ProductSoldPeriodResponse getProductSoldPeriod(@RequestBody ProductSoldPeriodRequest pRequest,HttpServletResponse response) throws IOException{
		Productos product=null;
		ProductSoldPeriodResponse pResponse=null;
		
		if(pRequest.getCodigobarra().matches(RegexpUtil.BARCODE) && pRequest.getFechainicio().matches(RegexpUtil.DATE) && pRequest.getFechafin().matches(RegexpUtil.DATE)) {
			product=productosService.getByCode(pRequest.getCodigobarra());
			pResponse=new ProductSoldPeriodResponse();
		}else {
			response.sendError(400);
		}
		
		if(product!=null) {
			pResponse=new ProductSoldPeriodResponse(product.getCodigobarra(), product.getDescripcion());
			pResponse.setProductsoldperiod(productosService.getProductsSoldPeriod(pRequest.getFechainicio(), pRequest.getFechafin(), pRequest.getCodigobarra()));
		}
		
		return pResponse;
	}
}
