/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.repositories;

import com.autoservicio.puntoventa.dto.Distribuidor;
import java.math.BigDecimal;
import org.springframework.data.repository.CrudRepository;


public interface DistribuidoresCrudRepository extends CrudRepository<Distribuidor, BigDecimal> {
    
}
