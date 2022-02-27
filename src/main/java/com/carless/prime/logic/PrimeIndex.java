package com.carless.prime.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Class that loads a prime index file with a method to find the nearest next
 * lowest closest match to an index value passed in to the
 * findCloseIndexLocation. It is used to increase performance when paging prime
 * data with high values. Using the index helps paging as we need to only
 * process primes around the area we need to return data for.
 * 
 * The format of the index file is
 * 
 * idx:prime
 * 
 * for example
 * 
 * 1:2 //represents the first entry in the index, 1 being the index value 2
 * being the prime number at that index. 1000000:15485863 // this is 1 millionth
 * prime ... ... ... 9000000:160481183 //9th millionth prime.
 * 
 * @author mnc
 *
 */
@Component
public class PrimeIndex {

	Logger logger = LoggerFactory.getLogger(PrimeIndex.class);

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
			logger.debug("Loading Prime index from the {}", resourceFile.getFilename());
			try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceFile.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line, ":");
					Integer index = Integer.parseInt(st.nextToken()); // Index location
					Integer prime = Integer.parseInt(st.nextToken()); // Prime at idx
					primeIndexMap.put(index, prime);
				}
				logger.debug("Prime index file loaded.");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error loading prime index file {}. The error was {} ", resourceFile.getFilename(),
						e.getMessage());
				;
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
		logger.debug("Looking for nearest(next lowest) index number in index cache using value of: {}", toFind);

		Optional<Integer> nearestKey = primeIndexMap.keySet().stream().filter(a -> a <= toFind) // filter list to values
																								// up to and including
																								// toFind
				.reduce((Integer::max)); // Return the max value from the filtered list. This is the nearest lowest
											// number to our toFind value.
		logger.debug("Using stream Found index match. Using index value: {} with prime {}", nearestKey.get(),
				primeIndexMap.get(nearestKey.get()));

		return new PrimeIndex.PrimeIdxData(nearestKey.get(), primeIndexMap.get(nearestKey.get()));
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
