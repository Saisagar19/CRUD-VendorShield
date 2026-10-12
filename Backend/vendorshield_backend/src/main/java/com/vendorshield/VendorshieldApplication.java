package com.vendorshield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class VendorshieldApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendorshieldApplication.class, args);
	}

}
