package com.carless.prime.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class PrimeCalcTest {
	
	Logger logger = LoggerFactory.getLogger(PrimeCalcTest.class);

	@Test
	void testIsPrime() {
		IPrimes primeCalc = new PrimeCalc();
		assertTrue(primeCalc.isPrime(7),"Expected to be true as 7 is a primeNumber");
		assertFalse(primeCalc.isPrime(8), "Expected to be false as 8 is not a prime number");
	}
	
	@Test
	void testNextPrime() {
		IPrimes primeCalc = new PrimeCalc();
		Random rn = new Random();
		int nextPrime = 4;
		for (int i = 0;i < 100;i++) {
			int primeRndSeed = rn.nextInt(Integer.MAX_VALUE)+1;
			logger.debug("Generated random number: {}", primeRndSeed);
			nextPrime = primeCalc.nextPrime(primeRndSeed);
			logger.debug("Generated prime number: {}", nextPrime);
			assertTrue(primeCalc.isPrime(nextPrime),"Generated number did not pass prime number check, the number was:" + nextPrime);
		}
		
	}

}
