/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.repositories;

import com.autoservicio.puntoventa.dto.Productos;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class ProductosRepository {
    @PersistenceContext
    private EntityManager em;
    
    public List<Productos>  getProductosByPrice(int price){
         return em.createNamedQuery("Productos.findByPrecioventa").setParameter("precioventa",new BigDecimal(price)).getResultList();
    }
}
