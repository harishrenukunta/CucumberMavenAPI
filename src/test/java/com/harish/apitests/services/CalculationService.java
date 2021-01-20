package com.harish.apitests.services;

import org.springframework.stereotype.Service;


public interface CalculationService {
	
	public int add(int a, int b);
	public int minus(int a, int b);
	public int product(int a, int b);
	public float divide(int a, int b);

}
