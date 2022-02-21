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
class PrimeGenControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
    void getPrimesWithPage() {
		//Test API call with some valid parameters
		HashMap<String, String> params = new HashMap<>();
		params.put("number", "50");
		params.put("pageNumber", "1");
		params.put("itemsPerPage", "15");
		ResponseEntity<ResponseData>res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber={number}&pageNumber={pageNumber}&itemsPerPage={itemsPerPage}",ResponseData.class , params);
		assertAll(
				() -> assertEquals(HttpStatus.OK, res.getStatusCode(),"We didn't get a status code of 200"),
				() -> assertEquals(15,res.getBody().getPrimes().size(),"Number of primes returned was not what was expected"),
				() -> assertEquals("ok",res.getBody().getStatus(),"Status should have been 'ok'"),
				() -> assertEquals(2,res.getBody().getPrimes().get(0),"Should have recieved Prime number of 2"),
				() -> assertEquals(47,res.getBody().getPrimes().get(14),"Should have recieved Prime number of 47")
				
		);
	}
	
	@Test
	void getPrimesWithoutPage() {
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
	void getPrimesWithLargeFromNumber() {
		//Test API call with some valid parameters
		HashMap<String, String> params = new HashMap<>();
		params.put("number", "1000000");
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
	void getPrimesWithLargeFromNumberAndPageNumber() {
		//Test API call with some valid parameters
		HashMap<String, String> params = new HashMap<>();
		params.put("number", "2145390523");
		params.put("pageNumber", "1050000");
		params.put("itemsPerPage", "100");
		ResponseEntity<ResponseData>res = testRestTemplate.getForEntity("/rest/getprimes?fromNumber={number}&pageNumber={pageNumber}&itemsPerPage={itemsPerPage}",ResponseData.class , params);
		assertAll(
				() -> assertEquals(HttpStatus.OK, res.getStatusCode(),"We didn't get a status code of 200"),
				() -> assertEquals(100,res.getBody().getPrimes().size(),"Number of primes returned was not what was expected"),
				() -> assertEquals("ok",res.getBody().getStatus(),"Status should have been 'ok'"),
				() -> assertEquals(2145388261,res.getBody().getPrimes().get(0),"Should have recieved Prime number of 2145388261"),
				() -> assertEquals(2145390523,res.getBody().getPrimes().get(99),"Should have recieved Prime number of 2145390523")
				
		);
	}
	
	@Test
	void getPrimesValidation() {
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
						
	}
}
