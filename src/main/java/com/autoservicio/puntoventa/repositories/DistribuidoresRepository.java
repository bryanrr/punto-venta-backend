/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.repositories;

import com.autoservicio.puntoventa.dto.Distribuidor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class DistribuidoresRepository {
    @PersistenceContext
    private EntityManager em;
    
    public List<Distribuidor>  getDistribuidorBySubcategory(String subcategoria){
         return em.createNamedQuery("Distribuidor.findBySubcategoria").setParameter("subcategoria",subcategoria).getResultList();
    }
}
