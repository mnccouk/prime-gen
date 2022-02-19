package com.carless.prime.data;

import java.util.List;


/**
 * Class that holds data ready to be sent back to the user. 
 * @author mnc
 *
 */
public class ResponseData {
	
	private List<Integer> primes;
	private String status;
	private String msg;
	
	public List<Integer> getPrimes() {
		return primes;
	}
	public void setPrimes(List<Integer> primes) {
		this.primes = primes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	

}
