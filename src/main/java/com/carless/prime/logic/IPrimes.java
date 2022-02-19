package com.carless.prime.logic;
/**
 * Interface for defining behaviour for checking and obtaining prime number
 * @author mnc
 *
 */
public interface IPrimes {

	/**
	 * Determines if the  argument passed into this function is a prime number
	 * @param num
	 * 	The number to test to see whether it is a Prime number or not
	 * @return
	 *  True if the number is a prime, otherwise false
	 */
	boolean isPrime(int num);

	/**
	 * Get the next prime number from the number passed into the function. 
	 * @param number
	 * 	  The starting point from where to look for the next prime number
	 * @return
	 * 	The next prime number  
	 */
	int nextPrime(int number);

}