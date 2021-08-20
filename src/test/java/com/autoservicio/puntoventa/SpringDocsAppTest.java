package com.autoservicio.puntoventa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.autoservicio.puntoventa.models.ProductSoldPeriodRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class,SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class SpringDocsAppTest {
	
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) throws ParseException {
		this.mockMvc=MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
				.build();
	}
	
	@Test
	void testGetProduct() throws Exception {
		
		mockMvc.perform(get("/producto/{barcode}","HUE1000")
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.codigobarra",equalTo("HUE1000")))
			.andDo(document("{methodName}",preprocessResponse(prettyPrint())
					,pathParameters(parameterWithName("barcode").description("The product barcode"))));
	}
	
	@Test
	void testGetProductCoincidences() throws Exception {
		
		mockMvc.perform(get("/productos/{matchingString}"," don azu ")
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{methodName}"
					,preprocessResponse(prettyPrint())
					,pathParameters(parameterWithName("matchingString").description("All the products that have this string value on its \"description\" field"))
					));
	}
	
	@Test
	void testGetProductSold() throws Exception {
		ProductSoldPeriodRequest pr=new ProductSoldPeriodRequest("CIG","2018-09-20","2018-09-22");
		String soldPeriodRequest=new ObjectMapper().writeValueAsString(pr);
		
		mockMvc.perform(post("/producto/sold")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(soldPeriodRequest))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{methodName}",preprocessRequest(prettyPrint())
					,preprocessResponse(prettyPrint())
					,requestFields(
							fieldWithPath("codigobarra").description("The barcode of the product"),
							fieldWithPath("fechainicio").description("The starting date of purchases of this product"),
							fieldWithPath("fechafin").description("The ending date of purchases of this product")
							)
					,responseFields(
							fieldWithPath("codigobarra").description("Barcode product"),
							fieldWithPath("descripcion").description("Description product"),
							fieldWithPath("productsoldperiod[]").description("Array showing the date and quantity of a product purchased"),
							fieldWithPath("productsoldperiod[].fechacompra").ignored(),
							fieldWithPath("productsoldperiod[].cantidad").ignored()
							)
					));
	}
	
	@Test
	void testUpdateProduct() throws Exception {
		String productJson="{\"precioventa\":25,\"preciocompra\":19,\"codigobarra\":\"HUE1000\",\"descripcion\":\"Huevo 1 kg\",\"existencia\":-26,\"fraccion\":\"Y\",\"promocion\":\"N\",\"lastupdatedtime\":\"2021-04-06T16:39:08.482+00:00\",\"distribuidorid\":{\"id\":41,\"categoria\":\"HUEVO\",\"subcategoria\":\"Huevo\",\"codigocategoria\":\"HUE\"}}";
		
		mockMvc.perform(put("/producto/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson))
			.andDo(print())
			.andExpect(status().is(204))
			.andDo(document("{methodName}",preprocessRequest(prettyPrint())
					,requestFields(fieldWithPath("codigobarra").description("Barcode product"),
							fieldWithPath("descripcion").description("Description product"),
							fieldWithPath("preciocompra").description("Purchased price"),
							fieldWithPath("precioventa").description("Selling price"),
							fieldWithPath("existencia").description("Quantity in stock.Default 0"),
							fieldWithPath("fraccion").description("Y/N. If the product can be fractional sold"),
							fieldWithPath("promocion").description("Y/N. If there is a promotion"),
							fieldWithPath("lastupdatedtime").description("The date the request is sent"),
							fieldWithPath("distribuidorid").description("Keep previous values"),
							fieldWithPath("distribuidorid.id").ignored(),
							fieldWithPath("distribuidorid.categoria").ignored(),
							fieldWithPath("distribuidorid.subcategoria").ignored(),
							fieldWithPath("distribuidorid.codigocategoria").ignored()
							)
					));
	}

}
