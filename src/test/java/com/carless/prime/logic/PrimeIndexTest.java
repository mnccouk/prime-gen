package com.carless.prime.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrimeIndexTest {
	
	@Autowired 
	PrimeIndex primeIndex;
	
	@Test
	void testLoadIndex() {
		SortedMap<Integer, Integer> primeIndexMap = primeIndex.loadIndex();
		assertEquals(106, primeIndexMap.size());
	}
	
  @Test
  void testFindCloseIndexLocation() {
	  PrimeIndex.PrimeIdxData data =  primeIndex.findCloseIndexLocation(1234);
	  assertEquals(true, data.indexLoc == 1);
	  
	  data =  primeIndex.findCloseIndexLocation(1012321);
	  assertEquals(true, data.indexLoc == 1000000);
	  
	  data =  primeIndex.findCloseIndexLocation(105000010);
	  assertEquals(true, data.indexLoc == 105000000); 
  }

}
