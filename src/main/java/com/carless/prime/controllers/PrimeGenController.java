package com.carless.prime.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.math3.primes.Primes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carless.prime.data.ResponseData;
import com.carless.prime.logic.PrimeIndex;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST Prime controller responsible for handling incoming http requests to
 * service prime number generation.
 * 
 * @author mnc
 *
 */
@RestController
@Validated
public class PrimeGenController {

	Logger logger = LoggerFactory.getLogger(PrimeGenController.class);

	@Autowired
	PrimeIndex primeIndex;

	@Operation(summary = "Generates a list of prime numbers that are less than or equal to the fromNumber parameter.") // Open
																														// API
																														// Doc
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lists prime numbers", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class)) }),
			@ApiResponse(responseCode = "400", description = "One or more invalid parameters supplied", content = @Content) })
	@GetMapping("/rest/getprimes")
	public ResponseEntity<ResponseData> getPrimes(

			@Parameter(description = "The numerical input for which to return prime numbers that are less than or equal to this number") @RequestParam(value = "fromNumber") @NotBlank(message = "is a required value, please supply a value for this parameter") @Min(message = "minimum value to list primes from must be greater then 1", value = 2) @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String fromNumberStr,
			@Parameter(description = "The page number for which to return prime numbers for. This is optional but defaults to 1 if not supplied") @RequestParam(value = "pageNumber", defaultValue = "1") @Min(message = "minimum value must is 1", value = 1) @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String pageNumber,
			@Parameter(description = "The number of prime numbers to return on each page. This is optional but will default to max int (2147483647). ") @RequestParam(value = "itemsPerPage", defaultValue = "2147483647") @NotBlank(message = "is a required value, please supply a value for this parameter") @Min(message = "Has a minimum value of 10", value = 10) @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String itemsPerPage) {

		// Setup new ResonseData object that will hold the data to be returned from this
		// call.
		ResponseData responseData = new ResponseData();
		logger.debug("Entered getPrimes API call with params - fromNumber:{} pageNumber:{} itemsPerPage:{}",
				fromNumberStr, pageNumber, itemsPerPage);

		int fromNumber = Integer.parseInt(fromNumberStr);

		// If the client requested a large primeNumber together with a high page number
		// it could take a while to process through all the prime numbers
		// up to the point we need data for the page. Instead lookup a primeNumber from
		// an index so as
		// we can jump to that point.
		// This will minimise time for processing requests with large
		// fromNumbers\pageNumbers
		int startIdxAtPage = (Integer.valueOf(pageNumber) * Integer.valueOf(itemsPerPage))
				- Integer.valueOf(itemsPerPage) + 1;

		PrimeIndex.PrimeIdxData primeIdxData = primeIndex.findCloseIndexLocation(startIdxAtPage);
		int getPrimeFromHere = primeIdxData.getPrime(); // Start at the prime number found from the index

		Stream<Integer> in = IntStream.rangeClosed(getPrimeFromHere, fromNumber).filter(Primes::isPrime).boxed(); // Stream of prime numbers, using index as start point and fromNumber as limit
																													
		List<Integer> primeList = in.skip(startIdxAtPage - primeIdxData.getIndexLoc()) // Move to the index at the start
																						// of the page
				.limit(Integer.valueOf(itemsPerPage)).collect(Collectors.toList()); // Only return the amount of Prime
																					// numbers for a page worth of data.

		responseData.setPrimes(primeList);

		responseData.setStatus("ok");
		return ResponseEntity.ok(responseData); // return the data back to the calling client.
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	ResponseEntity<ResponseData> handleConstraintViolationException(ConstraintViolationException e) {
		ResponseData responseData = new ResponseData();
		responseData.setMsg("Validation error: " + e.getMessage());
		responseData.setStatus("error");
		return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
	}
}
