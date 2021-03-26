package com.autoservicio.puntoventa.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoservicio.puntoventa.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class JwtBlacklistService {
	private List<String> blacklist;
	private Timer timer;
	private TimerTask timerTask;
	
	@Autowired
	JwtUtil jwtUtil;
	
	public JwtBlacklistService() {
		super();
		blacklist=new ArrayList<>();
		timerTask= new TimerTask() {
	        public void run() {
	            blacklist.removeIf(jwt->{
	            	try {
	            		jwtUtil.extractExpiration(jwt);
	            		return false;
	            	}catch(ExpiredJwtException expiredException) {
	            		return true;
	            	}
	            });
	        }
	    };
	    timer=new Timer("Timer");
	    
	    LocalDateTime ldt=LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 50));
		Date date=Date.from(ldt.toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())));
		long delay=1000*60*60*24L;
	    
	    timer.schedule(timerTask, date, delay);
	}

	public void addJwtToBlacklist(String jwt) {
		blacklist.add(jwt);
	}
	
	public boolean isBlacklisted(String jwt) {
		
		return blacklist.stream().filter(jwt1->jwt1.equals(jwt)).count()==1l;
	}

}
