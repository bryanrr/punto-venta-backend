package com.autoservicio.puntoventa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.autoservicio.puntoventa.dto.Productos;

@Controller
public class MainController {

	@GetMapping(value={"/main"})
	String mainPage() {
		return "main.html";
	}
	
	@GetMapping(value="/login")
    public String loginPage(){
        return "login.html";
    }
}
