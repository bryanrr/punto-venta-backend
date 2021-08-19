package com.autoservicio.puntoventa.models;

public class ProductSoldPeriodRequest {

	private String codigobarra;
	private String fechainicio;
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
