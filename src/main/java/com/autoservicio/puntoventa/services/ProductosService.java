/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.services;

import com.autoservicio.puntoventa.repositories.DistribuidoresCrudRepository;
import com.autoservicio.puntoventa.repositories.ProductosCrudRepository;
import com.autoservicio.puntoventa.dto.CambiosPreciosMapper;
import com.autoservicio.puntoventa.dto.Distribuidor;
import com.autoservicio.puntoventa.dto.Productos;
import com.autoservicio.puntoventa.dto.ProductsSoldPeriodMapper;
import com.autoservicio.puntoventa.mappers.PuntoVentaMappers;
import com.autoservicio.puntoventa.repositories.DistribuidoresRepository;
import com.autoservicio.puntoventa.repositories.ProductosRepository;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author Usuario
 */
@Service
public class ProductosService {
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private DistribuidoresRepository distribuidoresRepository;
    @Autowired 
    ProductosCrudRepository productosCrudRepository;
    @Autowired
    DistribuidoresCrudRepository distribuidoresCrudRepository;
    @Autowired
    PuntoVentaMappers puntoVentaMappers;
    
    public Productos getByCode(String code){
    	Optional<Productos> productOpt=productosCrudRepository.findById(code.trim().toUpperCase());
    	return productOpt.isPresent()?productOpt.get():null;
    }
    
    public Iterable<Distribuidor> getAllDistribuidores(){
        
        return distribuidoresCrudRepository.findAll();
    }
    
    public List<CambiosPreciosMapper> getPricesProduct(String barcode){
        return puntoVentaMappers.getAllPricesChanges(barcode.trim().toUpperCase());
    }
    
    public List<Productos> getProductosSoldPerCategory(String fecha,String subcategoria) throws ParseException{
        String fechaInicio;
        List<Productos> productos;
        Distribuidor distribuidor=distribuidoresRepository.getDistribuidorBySubcategory(subcategoria).get(0);
        productos=puntoVentaMappers.getProductosSoldPerCategory(fecha,distribuidor.getId().intValue());
        
        return productos;
    }
    
    public List<Productos> getProductByCoincidences(String completeToken){
        List<Productos> productos=new ArrayList<>();
        
         String []tokens=completeToken.trim().split("#");
        List<String> tokens1=new ArrayList();
        //Elimina los vacios
        for(int i=0;i<tokens.length;i++){
            if(!tokens[i].trim().equals("")){
                tokens1.add(tokens[i]);
            }
        }
        
        List<String> words;
        if(tokens1.size()==1){
            words=getTokensForProduct(tokens1.get(0));
            if(words.size()==1){
                productos=puntoVentaMappers.getProductosLikeToken(".*", tokens1.get(0));
            }else if(words.size()==2){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(".*", words.get(0), words.get(1), ".*", ".*");
            }else if(words.size()==3){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(".*", words.get(0), words.get(1), words.get(2), ".*");
            }else if(words.size()==4){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(".*", words.get(0), words.get(1), words.get(2), words.get(4));
            }
            
            words.clear();
            
        }else if(tokens1.size()==2){
            words=getTokensForProduct(tokens1.get(1));
            if(words.size()==1){
                productos=puntoVentaMappers.getProductosLikeToken(tokens1.get(0), tokens1.get(1));
            }else if(words.size()==2){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(tokens1.get(0), words.get(0), words.get(1), ".*", ".*");
            }else if(words.size()==3){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(tokens1.get(0), words.get(0), words.get(1), words.get(2), ".*");
            }else if(words.size()==4){
                productos=puntoVentaMappers.getProductosLikeTokenLevel3(tokens1.get(0), words.get(0), words.get(1), words.get(2), words.get(4));
            }
            words.clear();
            
        }
        
        return productos;
    }
    
    private List<String> getTokensForProduct(String text){
        List<String> words=new ArrayList();
        String[] tokens=text.split(" ");
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if(!token.trim().equals("")){
                words.add(token);
            }
        }
        
        return words;
    }
    
    public Productos updateProduct(Productos product) {
    	return productosCrudRepository.save(product);
    }
    
    /**
     *Get the date and the amount of pieces of an specific product/barcode, sold 
     *during an specific period of time. 
     * @param fechaInicio
     * @param fechaFin
     * @param codigobarra
     * @return List<ProductsSoldPeriodMapper>
     */
    public List<ProductsSoldPeriodMapper> getProductsSoldPeriod(String fechaInicio, String fechaFin, String codigobarra) {
    	return puntoVentaMappers.getProductsSoldPeriod(fechaInicio, fechaFin, codigobarra);
    }
    
}
