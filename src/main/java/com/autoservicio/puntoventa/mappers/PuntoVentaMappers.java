/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoservicio.puntoventa.mappers;

import com.autoservicio.puntoventa.dto.CambiosPreciosMapper;
import com.autoservicio.puntoventa.dto.Productos;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author Usuario
 */
@Mapper
public interface PuntoVentaMappers {
    final String getCambiosPrecios = "SELECT CP.CODIGOBARRA,"
            + "CP.PRECIOCOMPRA,CP.PRECIOVENTA,CP.FECHACAMBIO,P.DESCRIPCION"
            + ",ROUND((CP.PRECIOVENTA-CP.PRECIOCOMPRA)*100/CP.PRECIOCOMPRA,2)AS PORCENTAJE" +
                " FROM CAMBIOSPRECIOS CP JOIN PRODUCTOS P ON CP.CODIGOBARRA=P.CODIGOBARRA " +
                " WHERE CP.CODIGOBARRA=#{barcode}";
    
    final String getProductsTokens3="SELECT* FROM( " +
"            SELECT * FROM( " +
"              SELECT* FROM ( " +
"                SELECT  P.* " +
"                    FROM PRODUCTOS P JOIN DISTRIBUIDOR D ON P.DISTRIBUIDORID=D.ID " +
"                    WHERE  REGEXP_LIKE(D.CODIGOCATEGORIA,#{category,javaType=String,jdbcType=VARCHAR},'i') AND " +
"                           REGEXP_LIKE(P.DESCRIPCION,#{token1,javaType=String,jdbcType=VARCHAR},'i')    ) " +
"              WHERE REGEXP_LIKE(DESCRIPCION,#{token2,javaType=String,jdbcType=VARCHAR},'i'))  " +
"            WHERE REGEXP_LIKE(DESCRIPCION,#{token3,javaType=String,jdbcType=VARCHAR},'i')) " +
"        WHERE REGEXP_LIKE(DESCRIPCION,#{token4,javaType=String,jdbcType=VARCHAR},'i')";
    
    final String getProductsTokens="SELECT  P.*  " +
"        FROM PRODUCTOS P JOIN DISTRIBUIDOR D ON P.DISTRIBUIDORID=D.ID  " +
"        WHERE  REGEXP_LIKE(D.CODIGOCATEGORIA,#{category,javaType=String,jdbcType=VARCHAR},'i') AND " +
"               REGEXP_LIKE(P.DESCRIPCION,#{token,javaType=String,jdbcType=VARCHAR},'i')";
    
    
    
    @Select(getCambiosPrecios)
   @Results(value = {
      @Result(property = "codigobarra", column = "CODIGOBARRA"),
      @Result(property = "precioventa", column = "PRECIOVENTA"),
      @Result(property = "preciocompra", column = "PRECIOCOMPRA"),
      @Result(property = "fechacambio", column = "FECHACAMBIO"),       
      @Result(property = "descripcion", column = "DESCRIPCION"),
      @Result(property = "porcentaje", column = "PORCENTAJE")
   })
   List<CambiosPreciosMapper> getAllPricesChanges(@Param("barcode")String barcode);
    
    
   
   @Select(getProductsTokens3)
   @Results(value = {
      @Result(property = "codigobarra", column = "CODIGOBARRA"),
      @Result(property = "precioventa", column = "PRECIOVENTA"),
      @Result(property = "preciocompra", column = "PRECIOCOMPRA"),
      @Result(property = "existencia", column = "existencia"),       
      @Result(property = "descripcion", column = "descripcion"),
      @Result(property = "fraccion", column = "fraccion"),
      @Result(property = "promocion", column = "promocion"),
      @Result(property = "distribuidorid.id", column = "distribuidorid")
   })
   List<Productos> getProductosLikeTokenLevel3(@Param("category")String category,
           @Param("token1")String token1,@Param("token2")String token2,@Param("token3")String token3,
           @Param("token4")String token4);
   
   
   
   @Select(getProductsTokens)
   @Results(value = {
      @Result(property = "codigobarra", column = "CODIGOBARRA"),
      @Result(property = "precioventa", column = "PRECIOVENTA"),
      @Result(property = "preciocompra", column = "PRECIOCOMPRA"),
      @Result(property = "existencia", column = "existencia"),       
      @Result(property = "descripcion", column = "descripcion"),
      @Result(property = "fraccion", column = "fraccion"),
      @Result(property = "promocion", column = "promocion"),
      @Result(property = "distribuidorid.id", column = "distribuidorid")
   })
   List<Productos> getProductosLikeToken(@Param("category")String category,
           @Param("token")String token);

   
   final String getProductsSoldCategory="SELECT SUM(CANTIDAD)AS CANTIDAD,CODIGOBARRA,DESCRIPCION FROM( " +
        "  SELECT P.DESCRIPCION,CI.CODIGOBARRA AS CODIGOBARRA,CI.CANTIDAD AS CANTIDAD,P.DISTRIBUIDORID AS DISTRIBUIDORID FROM PRODUCTOS P JOIN ( " +
        "    SELECT ci.codigobarra,ci.cantidad " +
        "    FROM compraclientes cc join compraindividual ci on cc.ID=ci.compraid  " +
        "    WHERE fechacompra >TO_DATE(#{fecha,javaType=String,jdbcType=VARCHAR}, 'yyyy-mm-dd'))   " +
        "  CI ON P.CODIGOBARRA=CI.CODIGOBARRA)  " +
        "WHERE DISTRIBUIDORID=#{distribuidorid, javaType=int ,jdbcType=INTEGER} " +
        "GROUP BY CODIGOBARRA,DESCRIPCION " +
        "ORDER BY CANTIDAD DESC";
   @Select(getProductsSoldCategory)
   @Results(value = {
      @Result(property = "codigobarra", column = "CODIGOBARRA"),
      @Result(property = "preciocompra", column = "cantidad"),
      @Result(property = "descripcion", column = "descripcion")
   })
   List<Productos> getProductosSoldPerCategory(@Param("fecha")String fecha, @Param("distribuidorid")int distribuidorid
           );
   
}
