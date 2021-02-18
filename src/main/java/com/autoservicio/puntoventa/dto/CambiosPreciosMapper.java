/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Usuario
 */

@Entity
public class CambiosPreciosMapper implements Serializable{
    @Id
    private String codigobarra;
    @Id
    private BigDecimal precioventa;
    @Id
    private BigDecimal preciocompra;
    @Id
    private Date fechacambio;
    private String descripcion;
    private BigDecimal porcentaje;

    public String getCodigobarra() {
        return codigobarra;
    }

    public void setCodigobarra(String codigobarra) {
        this.codigobarra = codigobarra;
    }

    public BigDecimal getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(BigDecimal precioventa) {
        this.precioventa = precioventa;
    }

    public BigDecimal getPreciocompra() {
        return preciocompra;
    }

    public void setPreciocompra(BigDecimal preciocompra) {
        this.preciocompra = preciocompra;
    }

    public Date getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(Date fechacambio) {
        this.fechacambio = fechacambio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
  
    
}
