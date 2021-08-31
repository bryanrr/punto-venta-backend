package com.autoservicio.puntoventa;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.autoservicio.puntoventa.models.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class PuntoventaApplicationTests {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext
			) throws ParseException {
		this.mockMvc=MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.build();
	}

	@ParameterizedTest
	@ValueSource(strings= {"HUE!#!","HUE$1000","HUE_1000","HUE.1000","HUE 1000","HUE,1000,",
			" HUE1000 ","LONGBARCODEHUE100HUE1000HUE1000","H"," ","PAÑAL","\t","\n","\u0001\u0002\u0003",
			"\""})
	void testBadBarcodeFormat(String malformedBarcode) throws Exception {
		mockMvc.perform(get("/producto/{barcode}",malformedBarcode)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is(400));
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void testAllTypesBlanksBarcode(String blankBarcode) throws Exception {
		mockMvc.perform(get("/producto/{barcode}",blankBarcode)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is(404));
	}
	
	@ParameterizedTest
	@CsvFileSource(resources="/reservedStringsBLNS.csv")
	void testReservedStringsBarcode(String reservedStringBarcode) throws Exception {
		mockMvc.perform(get("/producto/{barcode}",reservedStringBarcode)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is(200))
			.andReturn();
	}
	
	@ParameterizedTest
	@ValueSource(strings= {"don..azu","$don azu","don %$&"})
	void testBadCoincidencesProduct(String badString) throws Exception {
		mockMvc.perform(get("/productos/{coincidence}",badString)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is(400));
	}
	
	@ParameterizedTest
	@MethodSource("getBadCredentials")
	void testAuthentication(AuthenticationRequest authRequest) throws Exception {
		String authString=new ObjectMapper().writeValueAsString(authRequest);
		mockMvc.perform(post("/authenticate")
				.content(authString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is(400));
	}
	
	private static Stream<AuthenticationRequest>getBadCredentials() {
		List<AuthenticationRequest>authRequests=new ArrayList<>();
		authRequests.add(new AuthenticationRequest("user1$","pass"));
		authRequests.add(new AuthenticationRequest("user1","pass word"));
		authRequests.add(new AuthenticationRequest("user1","password--"));
		authRequests.add(new AuthenticationRequest("",""));
		
	    return authRequests.stream();
	}
}
