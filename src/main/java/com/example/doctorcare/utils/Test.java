package com.example.doctorcare.utils;

import java.time.LocalDateTime;

public class Test {

	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime latet = now.plusMinutes(10);
		
		System.out.println(now.isBefore(latet));
	}

}
