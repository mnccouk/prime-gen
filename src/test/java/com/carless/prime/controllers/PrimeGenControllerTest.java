package com.carless.prime.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.carless.prime.data.ResponseData;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PrimeGenControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	//
	public void getPrimesWithPage() {
		//Test API call with some valid parameters
		HashMap<String, String> params = new HashMap<>();
		params.put("number", "50");
		params.put("pageNumber", "1");
		params.put("itemsPerPage", "15");
		ResponseEntity<ResponseData>res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber={number}&pageNumber={pageNumber}&itemsPerPage={itemsPerPage}",ResponseData.class , params);
		assertAll(
				() -> assertEquals(HttpStatus.OK, res.getStatusCode(),"We didn't get a status code of 200"),
				() -> assertEquals(15,res.getBody().getPrimes().size(),"Number of primes returned was not what was expected"),
				() -> assertEquals("ok",res.getBody().getStatus(),"Status should have been 'ok'")
				
		);
	}
	
	@Test
	//
	public void getPrimesWithoutPage() {
		//Test API call with some valid parameters
		HashMap<String, String> params = new HashMap<>();
		params.put("number", "100");
		
		ResponseEntity<ResponseData>res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber={number}",ResponseData.class , params);
		assertAll(
				() -> assertEquals(HttpStatus.OK, res.getStatusCode(),"We didn't get a status code of 200"),
				() -> assertEquals(25,res.getBody().getPrimes().size(),"Number of primes returned was not what was expected"),
				() -> assertEquals("ok",res.getBody().getStatus(),"Status should have been 'ok'")
				
		);
	}
	
	@Test
	public void getPrimesValidation() {
		//Test fromNumber not being specified
		ResponseEntity<String> res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=",String.class);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		assertEquals(true, res.getBody().contains("Validation error"));
		
		//Test fromNumber min value
		res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=1",String.class);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		assertEquals(true, res.getBody().contains("minimum"));
		
		//Test fromNumber max value
		res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=2147483648",String.class);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		assertEquals(true, res.getBody().contains("maximum"));
		
		//Test itemsPerPage min value
		res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=100&pageNumber=1&itemsPerPage=5",String.class);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		assertEquals(true, res.getBody().contains("minimum") & res.getBody().contains("itemsPerPage"));
		
		//Test itemsPerPage as non integer value
		res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=100&pageNumber=1&itemsPerPage=5.5",String.class);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		assertEquals(true, res.getBody().contains("whole") & res.getBody().contains("itemsPerPage"));
				
		//Test  max value
		//res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber=2147483648",String.class);
		//assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode(),"We didn't get a status code of 400");	
		//assertEquals(true, res.getBody().contains("maximum"));

		
		
		
		
	}
}
