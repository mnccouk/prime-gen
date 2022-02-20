package com.carless.prime.logic;

import org.apache.commons.math3.primes.Primes;

/**
 * Implementation of the IPrimes interfaces that utilised the Apache Math library to generate Prime Numbers.
 * @author mnc
 *
 */
public class PrimeCalc implements IPrimes {
	
	@Override
	public boolean isPrime(int num) {
		return Primes.isPrime(num);
	}

	@Override
	public int nextPrime(int number) {
		return Primes.nextPrime(number);
	}

}
