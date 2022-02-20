package com.carless.prime.controllers;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carless.prime.data.ResponseData;
import com.carless.prime.logic.IPrimes;
import com.carless.prime.logic.Paging;
import com.carless.prime.logic.PrimeCalc;

@RestController
@Validated
public class PrimeGenController {

	Logger logger = LoggerFactory.getLogger(PrimeGenController.class);

	@GetMapping("/rest/getprimes")
	public ResponseEntity<ResponseData> getPrimes(
			@RequestParam(value = "fromNumber") @NotBlank(message = "is a required value, please supply a value for this parameter") @Min(message = "minimum value to list primes from must be greater then 1", value = 2) @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String fromNumberStr,
			@RequestParam(value = "pageNumber", defaultValue = "1") @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String pageNumber,
			@RequestParam(value = "itemsPerPage", defaultValue = "2147483647") @Min(message = "Has a minimum value of 10", value = 10) @Max(message = "The maximum value this parameter can be set to is 2147483647", value = 2147483647) @javax.validation.constraints.Pattern(regexp = "^[0-9]*$", message = "Must be specified as a numeric whole value") String itemsPerPage) {

		// Setup new ResonseData object that will hold the data to be returned from this
		// call.
		ResponseData responseData = new ResponseData();
		logger.debug("Entered getPrimes API call with params - fromNumber:{} pageNumber:{} itemsPerPage:{}",
				fromNumberStr, pageNumber, itemsPerPage);

		int fromNumber = Integer.parseInt(fromNumberStr);

		int getPrimeFromHere = 2; // Start at the first prime number
		Paging page = new Paging(Integer.valueOf(pageNumber), Integer.valueOf(itemsPerPage));

		IPrimes primeCalc = new PrimeCalc();
		while (getPrimeFromHere <= fromNumber) { // Only process up to our target number
			int nextPrime = primeCalc.nextPrime(getPrimeFromHere);
			if (nextPrime <= fromNumber) { // Make sure the prime just found is only added if less than or equal to our
											// target (fromNumber) value, as that is the requirement.
				if (page.addPrime(nextPrime) == 1) { // check to see if we have populated our page with data.
					logger.debug("Filled the requested page with data so bailing out of loop");
					break; // if the current value being added falls above the upperBound of the page index
							// break out the loop because we are done for now, lets reserves some cpu cycles
							// for something else..
				}
			}
			getPrimeFromHere = nextPrime + 1; // jump to the prime just found and add 1 ready for next attempt
		}
		// Load our response object with our page or data.
		responseData.setPrimes(page.getPageData());
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
