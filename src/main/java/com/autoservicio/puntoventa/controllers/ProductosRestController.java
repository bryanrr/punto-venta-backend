package com.autoservicio.puntoventa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.autoservicio.puntoventa.dto.Productos;
import com.autoservicio.puntoventa.services.ProductosService;

@RestController
public class ProductosRestController {

	@Autowired
	ProductosService productosService;
	
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
