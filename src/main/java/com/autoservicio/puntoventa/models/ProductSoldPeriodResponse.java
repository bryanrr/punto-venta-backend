package com.autoservicio.puntoventa.models;

import java.util.List;

import com.autoservicio.puntoventa.dto.ProductsSoldPeriodMapper;

public class ProductSoldPeriodResponse {
	private String codigobarra;
	private String descripcion;
	private List<ProductsSoldPeriodMapper> productsoldperiod;
	
	public ProductSoldPeriodResponse() {
		super();
	}

	public ProductSoldPeriodResponse(String codigobarra, String descripcion) {
		this.codigobarra = codigobarra;
		this.descripcion = descripcion;
	}
	
	public String getCodigobarra() {
		return codigobarra;
	}
	public void setCodigobarra(String codigobarra) {
		this.codigobarra = codigobarra;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<ProductsSoldPeriodMapper> getProductsoldperiod() {
		return productsoldperiod;
	}
	public void setProductsoldperiod(List<ProductsSoldPeriodMapper> productsoldperiod) {
		this.productsoldperiod = productsoldperiod;
	}
	
}
