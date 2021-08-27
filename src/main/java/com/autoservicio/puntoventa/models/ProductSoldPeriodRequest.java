package com.autoservicio.puntoventa.models;

import javax.validation.constraints.Pattern;

public class ProductSoldPeriodRequest {

	@Pattern(regexp="^(?=.{2,30}$)(?![ ])[a-zA-Z0-9]+(?<![_.])$")
	private String codigobarra;
	@Pattern(regexp="^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$")
	private String fechainicio;
	@Pattern(regexp="^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$")
	private String fechafin;
	
	public ProductSoldPeriodRequest() {
		
	}

	public ProductSoldPeriodRequest(String codigobarra, String fechainicio, String fechafin) {
		this.codigobarra = codigobarra;
		this.fechainicio = fechainicio;
		this.fechafin = fechafin;
	}
	
	public String getCodigobarra() {
		return codigobarra;
	}
	public void setCodigobarra(String codigobarra) {
		this.codigobarra = codigobarra;
	}
	public String getFechainicio() {
		return fechainicio;
	}
	public void setFechainicio(String fechainicio) {
		this.fechainicio = fechainicio;
	}
	public String getFechafin() {
		return fechafin;
	}
	public void setFechafin(String fechafin) {
		this.fechafin = fechafin;
	}
	
}
