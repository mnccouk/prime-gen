package com.carless.prime.logic;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class PagingTest {

	/**
	 * Populates Paging object with simulated page item data.
	 * 
	 * @param pageNumber  The page number Paging object stores items for
	 * @param numberItems The number of simulated items that is being pumped into
	 *                    the paging object
	 * @return A new Paging object populated with simulated data that represents a
	 *         page of data.
	 */
	private Paging populatePageItems(int pageNumber, int numberItems) {
		Paging paging = new Paging(pageNumber);
		for (int i = 1; i <= numberItems; i++) {
			paging.addPrime(i);
		}
		return paging;
	}

	/**
	 * 
	 * Populates Paging object with simulated page item data.
	 * 
	 * @param pageNumber     The page number the Paging object stores items for.
	 * @param numberItems    The number of simulated items that is being pumped into
	 *                       the paging object
	 * @param noItemsPerPage The Number of items to collect for a page of data.
	 * @return A new Paging object populated with simulated data that represents a
	 *         page of data.
	 * 
	 */
	private Paging populatePageItems(int pageNumber, int numberItems, int noItemsPerPage) {
		Paging paging = new Paging(pageNumber, 15);
		for (int i = 1; i <= numberItems; i++) {
			paging.addPrime(i);
		}
		return paging;
	}

	@Test
	/**
	 * Test getting page data items based on default 10 items per page
	 */
	void testGetPage() {
		// Test page 2 data items
		Paging paging = populatePageItems(2, 100);
		List<Integer> page2Items = paging.getPageData();
		assertEquals(page2Items.size(), paging.getPrimesPerPage());
		assertEquals(true, page2Items.contains(11));
		assertEquals(true, page2Items.contains(20));
		assertEquals(true, page2Items.contains(15));

		// Test page 5 data items
		paging = populatePageItems(5, 100);
		page2Items.clear();
		page2Items = paging.getPageData();
		assertEquals(page2Items.size(), paging.getPrimesPerPage());
		assertEquals(true, page2Items.contains(41));
		assertEquals(true, page2Items.contains(45));
		assertEquals(true, page2Items.contains(50));

		// Request a page that;s out of bounds
		paging = populatePageItems(2, 15);
		page2Items.clear();
		page2Items = paging.getPageData();
		assertEquals(5, page2Items.size());
		assertEquals(true, page2Items.contains(11));
		assertEquals(true, page2Items.contains(12));
		assertEquals(true, page2Items.contains(15));

		// Request a page that's out of bounds
		paging = populatePageItems(5, 20);
		page2Items.clear();
		page2Items = paging.getPageData();
		assertEquals(0, page2Items.size());
	}

	@Test
	/**
	 * Test getting page data items based on 15 items per page
	 */
	void testGetPageWithNoPageItems() {
		// Test page 2 data items
		Paging paging = populatePageItems(2, 100, 15);
		List<Integer> page2Items = paging.getPageData();
		assertEquals(page2Items.size(), paging.getPrimesPerPage());
		assertEquals(true, page2Items.contains(16));
		assertEquals(true, page2Items.contains(21));
		assertEquals(true, page2Items.contains(30));

		// Test page 5 data items
		paging = populatePageItems(4, 100, 15);
		page2Items.clear();
		page2Items = paging.getPageData();
		assertEquals(15, page2Items.size());
		assertEquals(true, page2Items.contains(46));
		assertEquals(true, page2Items.contains(51));
		assertEquals(true, page2Items.contains(60));

		// Request a page that;s out of bounds
		paging = populatePageItems(5, 10, 15);
		page2Items.clear();
		page2Items = paging.getPageData();
		assertEquals(0, page2Items.size());

	}

}
