package com.carless.prime.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a single page of prime items data. This is used to
 * capture a single page worth of prime data
 * 
 * @author mnc
 *
 */
public class Paging {

	// page 2
	// = 11 - 20
	// lowerBound = (requestedPage * primesPerPage) - (primesPerPage-1)
	// upperBound = (requestedPage * primesPerPage)
	private int lowerBound; // lower index value of the requested page
	private int upperBound; // upper index value of the requested page

	/* Items Per page */
	private int primesPerPage = 10;

	/* Requested page */
	private int requestedPage;

	/* List holding the Prime numbers to retbe urned for the requested page */
	private List<Integer> pageItems = new ArrayList<>();

	/* Holds the current index number of the prime being added */
	private int primeIdx = 0;

	/**
	 * Creates a paging object used for storing values for a given page of data with
	 * default of 10 prime numbers per page
	 * 
	 * @param requestedPage Sets the page number to store page data for
	 */
	public Paging(int requestedPage) {
		this.requestedPage = requestedPage;
		calcPageBounds();
	}

	/**
	 * Creates a paging object used for storing values for a given page of data.
	 * 
	 * @param requestedPage Sets the page number to store page data for
	 * @param primesPerPage Sets the number of items stored per page.
	 */
	public Paging(int requestedPage, int primesPerPage) {
		this.requestedPage = requestedPage;
		this.primesPerPage = primesPerPage;
		calcPageBounds();
	}

	/**
	 * Calculates the lowerbound and upperbound indexes for the requestedPage, prime
	 * values will only be stored if their index falls between these bounds.
	 */
	private void calcPageBounds() {
		lowerBound = (this.primesPerPage * this.requestedPage) - this.primesPerPage + 1;
		upperBound = (this.primesPerPage * this.requestedPage);
	}

	/**
	 * Stores a prime number data item if it falls on the page this object
	 * represents. Every attempt to store a data item value increments an internal
	 * index. If the index number of the prime data item being added falls within
	 * the upper and lower bounds it is added to the list of items representing a
	 * page.
	 * 
	 * @param prime The value to store.
	 * @return Returns a -1 if the value being added is below the lowerBound index
	 *         of the page, 1 if the index is above the upperBound index of the page
	 *         and 0 if the value being added falls within our page.
	 * 
	 * 
	 */
	public short addPrime(Integer prime) {
		primeIdx++;
		if ((primeIdx < lowerBound)) {
			return -1;
		}

		if (primeIdx > upperBound) {
			return 1;
		}

		pageItems.add(prime);
		return 0;

	}

	/**
	 * Gets a page of prime data items, the page of data that is returned is based
	 * on the page number index which is set in the constructor of this object.
	 * 
	 * @return Returns a list of page items. If the page number index is
	 */
	public List<Integer> getPageData() {
		return pageItems;
	}

	public int getPrimesPerPage() {
		return primesPerPage;
	}

}
