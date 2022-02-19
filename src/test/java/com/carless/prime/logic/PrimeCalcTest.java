package com.carless.prime.logic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimeCalcTest {

	@Test
	void testIsPrime() {
		IPrimes primeCalc = new PrimeCalc();
		assertTrue(primeCalc.isPrime(7),"Expected to be true as 7 is a primeNumber");
		assertFalse(primeCalc.isPrime(8), "Expected to be false as 8 is not a prime number");
	}
	
	@Test
	void testNextPrime() {
		IPrimes primeCalc = new PrimeCalc();
		int nextPrime = primeCalc.nextPrime(1100);
		System.out.println("Next Prime:" + nextPrime);
	}

}
