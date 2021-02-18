/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "DISTRIBUIDOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distribuidor.findAll", query = "SELECT d FROM Distribuidor d")
    , @NamedQuery(name = "Distribuidor.findById", query = "SELECT d FROM Distribuidor d WHERE d.id = :id")
    , @NamedQuery(name = "Distribuidor.findByCategoria", query = "SELECT d FROM Distribuidor d WHERE d.categoria = :categoria")
    , @NamedQuery(name = "Distribuidor.findBySubcategoria", query = "SELECT d FROM Distribuidor d WHERE d.subcategoria = :subcategoria")
    , @NamedQuery(name = "Distribuidor.findByCodigocategoria", query = "SELECT d FROM Distribuidor d WHERE d.codigocategoria = :codigocategoria")})
public class Distribuidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    
    @Column(name = "CATEGORIA")
    private String categoria;
    
    @Column(name = "SUBCATEGORIA")
    private String subcategoria;
    
    @Column(name = "CODIGOCATEGORIA")
    private String codigocategoria;

    public Distribuidor() {
    }

    public Distribuidor(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getCodigocategoria() {
        return codigocategoria;
    }

    public void setCodigocategoria(String codigocategoria) {
        this.codigocategoria = codigocategoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distribuidor)) {
            return false;
        }
        Distribuidor other = (Distribuidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autoservicio.puntoventajar.dto.Distribuidor[ id=" + id + " ]";
    }
    
}
