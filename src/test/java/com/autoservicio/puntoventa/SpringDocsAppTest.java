package com.autoservicio.puntoventa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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
import static org.springframework.restdocs.snippet.Attributes.key;
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
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.autoservicio.puntoventa.dto.Productos;
import com.autoservicio.puntoventa.models.AuthenticationRequest;
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
	void testAuthentication() throws Exception {
		AuthenticationRequest authentication=new AuthenticationRequest("bryanrr13", "Future21");
		String authString=new ObjectMapper().writeValueAsString(authentication);
		
		ConstraintDescriptions authenticationConstraints = new ConstraintDescriptions(AuthenticationRequest.class);
		
		mockMvc.perform(post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(authString))
			.andDo(print())
			.andExpect(status().is(204))
			.andDo(document("{methodName}",preprocessRequest(prettyPrint())
					,requestFields(
							fieldWithPath("username").description("Username").attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(authenticationConstraints.descriptionsForProperty("username"), "."))),
							fieldWithPath("password").description("Username password").attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(authenticationConstraints.descriptionsForProperty("password"), ".")))
					),responseHeaders( 
							headerWithName("Set-Cookie").description(
									"Instruction that will set a cookie, which its content is a JWT token")
					)));
	}
	
	@Test
	void testLogout() throws Exception {
		mockMvc.perform(post("/logout")
				.header("Cookie", "token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicnlhbnJyMTMiLCJleHAiOjE2Mjk1NzAyMDYsImlhdCI6MTYyOTU2MzAwNn0.FjclidSGS7ZoYRHjcr5RoaJUAsvc0U_pIGM7dJ9Ie8VUQKAX1j0vTPj8WGJGD7lT__MxGAXNk70BvZplcrBmGQ")
				)
			.andDo(print())
			.andExpect(status().is(204))
			.andDo(document("{methodName}"
					,requestHeaders( headerWithName("Cookie").description(
									"\"token=jwtToken\" format, it's a session identifier"))));
	}
	
	@Test
	void testGetProduct() throws Exception {
		
		mockMvc.perform(get("/producto/{barcode}","HUE1000")
				.accept(MediaType.APPLICATION_JSON)
				.header("Cookie", "token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicnlhbnJyMTMiLCJleHAiOjE2Mjk1NzAyMDYsImlhdCI6MTYyOTU2MzAwNn0.FjclidSGS7ZoYRHjcr5RoaJUAsvc0U_pIGM7dJ9Ie8VUQKAX1j0vTPj8WGJGD7lT__MxGAXNk70BvZplcrBmGQ"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.codigobarra",equalTo("HUE1000")))
			.andDo(document("{methodName}",preprocessResponse(prettyPrint())
					,pathParameters(parameterWithName("barcode").description("The product barcode"))
					,requestHeaders( headerWithName("Cookie").description(
							"\"token=jwtToken\" format, it's a session identifier"))));
	}
	
	@Test
	void testGetProductCoincidences() throws Exception {
		
		mockMvc.perform(get("/productos/{matchingString}"," don azu ")
				.accept(MediaType.APPLICATION_JSON)
				.header("Cookie", "token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicnlhbnJyMTMiLCJleHAiOjE2Mjk1NzAyMDYsImlhdCI6MTYyOTU2MzAwNn0.FjclidSGS7ZoYRHjcr5RoaJUAsvc0U_pIGM7dJ9Ie8VUQKAX1j0vTPj8WGJGD7lT__MxGAXNk70BvZplcrBmGQ"))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{methodName}"
					,preprocessResponse(prettyPrint())
					,pathParameters(parameterWithName("matchingString").description("All the products that have this string value on its \"description\" field"))
					,requestHeaders( headerWithName("Cookie").description(
							"\"token=jwtToken\" format, it's a session identifier"))));
	}
	
	@Test
	void testGetProductSold() throws Exception {
		ProductSoldPeriodRequest pr=new ProductSoldPeriodRequest("CIG","2018-09-20","2018-09-22");
		String soldPeriodRequest=new ObjectMapper().writeValueAsString(pr);
		
		ConstraintDescriptions productSoldConstraints = new ConstraintDescriptions(ProductSoldPeriodRequest.class);
		
		mockMvc.perform(post("/producto/sold")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(soldPeriodRequest)
				.header("Cookie", "token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicnlhbnJyMTMiLCJleHAiOjE2Mjk1NzAyMDYsImlhdCI6MTYyOTU2MzAwNn0.FjclidSGS7ZoYRHjcr5RoaJUAsvc0U_pIGM7dJ9Ie8VUQKAX1j0vTPj8WGJGD7lT__MxGAXNk70BvZplcrBmGQ"))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{methodName}",preprocessRequest(prettyPrint())
					,preprocessResponse(prettyPrint())
					,requestFields(
							fieldWithPath("codigobarra").description("The barcode of the product")
							.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(productSoldConstraints.descriptionsForProperty("codigobarra"), "."))),
							fieldWithPath("fechainicio").description("The starting date of purchases of this product")
							.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(productSoldConstraints.descriptionsForProperty("fechainicio"), "."))),
							fieldWithPath("fechafin").description("The ending date of purchases of this product")
							.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(productSoldConstraints.descriptionsForProperty("fechafin"), ".")))
							)
					,responseFields(
							fieldWithPath("codigobarra").description("Barcode product"),
							fieldWithPath("descripcion").description("Description product"),
							fieldWithPath("productsoldperiod[]").description("Array showing the date and quantity of a product purchased"),
							fieldWithPath("productsoldperiod[].fechacompra").ignored(),
							fieldWithPath("productsoldperiod[].cantidad").ignored()
							)
					,requestHeaders( headerWithName("Cookie").description(
							"\"token=jwtToken\" format, it's a session identifier"))
					));
	}
	
	@Test
	void testUpdateProduct() throws Exception {
		String productJson="{\"precioventa\":25,\"preciocompra\":19,\"codigobarra\":\"HUE1000\",\"descripcion\":\"Huevo 1 kg\",\"existencia\":-26,\"fraccion\":\"Y\",\"promocion\":\"N\",\"lastupdatedtime\":\"2021-04-06T16:39:08.482+00:00\",\"distribuidorid\":{\"id\":41,\"categoria\":\"HUEVO\",\"subcategoria\":\"Huevo\",\"codigocategoria\":\"HUE\"}}";
		
		ConstraintDescriptions updateConstraints = new ConstraintDescriptions(Productos.class);
		
		mockMvc.perform(put("/producto/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productJson)
				.header("Cookie", "token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicnlhbnJyMTMiLCJleHAiOjE2Mjk1NzAyMDYsImlhdCI6MTYyOTU2MzAwNn0.FjclidSGS7ZoYRHjcr5RoaJUAsvc0U_pIGM7dJ9Ie8VUQKAX1j0vTPj8WGJGD7lT__MxGAXNk70BvZplcrBmGQ"))
			.andDo(print())
			.andExpect(status().is(204))
			.andDo(document("{methodName}",preprocessRequest(prettyPrint())
					,requestFields(fieldWithPath("codigobarra").description("Barcode product")
							.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(updateConstraints.descriptionsForProperty("codigobarra"), "."))),
							fieldWithPath("descripcion").description("Description product")
								.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(updateConstraints.descriptionsForProperty("descripcion"), "."))),
							fieldWithPath("preciocompra").description("Purchased price")
								.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(updateConstraints.descriptionsForProperty("preciocompra"), "."))),
							fieldWithPath("precioventa").description("Selling price")
								.attributes(key("constraints")
									.value(StringUtils.collectionToDelimitedString(updateConstraints.descriptionsForProperty("precioventa"), "."))),
							fieldWithPath("existencia").description("Quantity in stock.Default 0")
								.attributes(key("constraints").value("No required")),
							fieldWithPath("fraccion").description("Y/N. If the product can be fractional sold")
								.attributes(key("constraints").value("No required")),
							fieldWithPath("promocion").description("Y/N. If there is a promotion")
								.attributes(key("constraints").value("No required")),
							fieldWithPath("lastupdatedtime").description("The date the request is sent")
								.attributes(key("constraints").value("No required")),
							fieldWithPath("distribuidorid").description("Keep previous values")
								.attributes(key("constraints").value("No required")),
							fieldWithPath("distribuidorid.id").ignored(),
							fieldWithPath("distribuidorid.categoria").ignored(),
							fieldWithPath("distribuidorid.subcategoria").ignored(),
							fieldWithPath("distribuidorid.codigocategoria").ignored()
							)
					,requestHeaders( headerWithName("Cookie").description(
							"\"token=jwtToken\" format, it's a session identifier"))
					));
	}

}
