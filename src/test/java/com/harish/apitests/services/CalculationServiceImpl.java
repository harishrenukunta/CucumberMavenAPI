package com.harish.apitests.services;

import org.springframework.stereotype.Service;

@Service
public class CalculationServiceImpl  implements CalculationService{

	@Override
	public int add(int a, int b) {
		
		return a+b;
	}

	@Override
	public int minus(int a, int b) {
		// TODO Auto-generated method stub
		return a-b;
	}

	@Override
	public int product(int a, int b) {
		// TODO Auto-generated method stub
		return a * b;
	}

	@Override
	public float divide(int a, int b) {
		// TODO Auto-generated method stub
		return ((float)a/b);
	}

}
