package com.carless.prime.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class PrimeIndex {

	private SortedMap<Integer, Integer> primeIndexMap = new TreeMap<>();

	@Value("classpath:index/idx.dat")
	Resource resourceFile;

	/**
	 * Loads Prime Number index into a sorted map
	 * 
	 * @return The Map that contains index data for prime numbers in the range of 32
	 *         bit integer.
	 */
	public SortedMap<Integer, Integer> loadIndex() {
		if (primeIndexMap.isEmpty()) {
			try (BufferedReader br = new BufferedReader(new FileReader(resourceFile.getFile()))) {
				String line;
				while ((line = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line, ":");
					Integer index = Integer.parseInt(st.nextToken()); // Index location
					Integer prime = Integer.parseInt(st.nextToken()); // Prime at idx
					primeIndexMap.put(index, prime);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return primeIndexMap;
	}

	/**
	 * Finds the nearest index value in the map that is lower than the toFind value.
	 * 
	 * @param toFind The index value we are trying know nearest known prime number
	 *               for
	 * @return PrimeIndex.PrimeIdxData object that represents a known index location
	 *         of a prime number.
	 */
	public PrimeIndex.PrimeIdxData findCloseIndexLocation(Integer toFind) {
		loadIndex();
		int previousKey = -1;
		for (Integer key : primeIndexMap.keySet()) {
			if (key > toFind) {
				break;
			}
			previousKey = key;
		}
		return new PrimeIndex.PrimeIdxData(previousKey, primeIndexMap.get(previousKey));
	}

	public class PrimeIdxData {
		Integer indexLoc;
		Integer prime;

		public PrimeIdxData(Integer indexLoc, Integer prime) {
			this.indexLoc = indexLoc;
			this.prime = prime;
		}

		public Integer getIndexLoc() {
			return indexLoc;
		}

		public Integer getPrime() {
			return prime;
		}

	}
}
