package com.autoservicio.puntoventa.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductsSoldPeriodMapper implements Serializable{

	private BigDecimal cantidad;
	@Id
	private Date fechacompra;
	
	public BigDecimal getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	
	public Date getFechacompra() {
		return fechacompra;
	}
	
	public void setFechacompra(Date fechacompra) {
		this.fechacompra = fechacompra;
	}
	
}
