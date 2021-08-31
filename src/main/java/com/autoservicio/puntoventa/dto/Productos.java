/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import com.autoservicio.puntoventa.util.RegexpUtil;

@Entity
@Table(name = "PRODUCTOS")
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p")
    , @NamedQuery(name = "Productos.findByCodigobarra", query = "SELECT p FROM Productos p WHERE p.codigobarra = :codigobarra")
    , @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Productos.findByPrecioventa", query = "SELECT p FROM Productos p WHERE p.precioventa = :precioventa")
    , @NamedQuery(name = "Productos.findByPreciocompra", query = "SELECT p FROM Productos p WHERE p.preciocompra = :preciocompra")
    , @NamedQuery(name = "Productos.findByExistencia", query = "SELECT p FROM Productos p WHERE p.existencia = :existencia")
    , @NamedQuery(name = "Productos.findByFraccion", query = "SELECT p FROM Productos p WHERE p.fraccion = :fraccion")
    , @NamedQuery(name = "Productos.findByPromocion", query = "SELECT p FROM Productos p WHERE p.promocion = :promocion")
    , @NamedQuery(name = "Productos.findByLastupdatedtime", query = "SELECT p FROM Productos p WHERE p.lastupdatedtime = :lastupdatedtime")})

public class Productos implements Serializable {
	@Column(name = "PRECIOVENTA")
    private BigDecimal precioventa;
    
	@Column(name = "PRECIOCOMPRA")
    private BigDecimal preciocompra;
    
    private static final long serialVersionUID = 1L;
    
    @Pattern(regexp=RegexpUtil.BARCODE)
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGOBARRA",nullable = false,length = 30)
    private String codigobarra;
    @Pattern(regexp=RegexpUtil.DESCRIPTION)
    @Basic(optional = false)
    @Column(name = "DESCRIPCION",nullable = false,length = 80)
    private String descripcion;
    @Column(name = "EXISTENCIA")
    private BigDecimal existencia;
    @Column(name = "FRACCION")
    private Character fraccion;
    @Column(name = "PROMOCION")
    private Character promocion;
    @Column(name = "LASTUPDATEDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdatedtime;
    @JoinColumn(name = "DISTRIBUIDORID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Distribuidor distribuidorid;

    public Productos() {
    }

    public Productos(String codigobarra) {
        this.codigobarra = codigobarra;
    }

    public Productos(String codigobarra, String descripcion) {
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


    public BigDecimal getExistencia() {
        return existencia;
    }

    public void setExistencia(BigDecimal existencia) {
        this.existencia = existencia;
    }

    public Character getFraccion() {
        return fraccion;
    }

    public void setFraccion(Character fraccion) {
        this.fraccion = fraccion;
    }

    public Character getPromocion() {
        return promocion;
    }

    public void setPromocion(Character promocion) {
        this.promocion = promocion;
    }

    public Date getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public Distribuidor getDistribuidorid() {
        return distribuidorid;
    }

    public void setDistribuidorid(Distribuidor distribuidorid) {
        this.distribuidorid = distribuidorid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigobarra != null ? codigobarra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.codigobarra == null && other.codigobarra != null) || (this.codigobarra != null && !this.codigobarra.equals(other.codigobarra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autoservicio.puntoventajar.dto.Productos[ codigobarra=" + codigobarra + " ]";
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


    
}
