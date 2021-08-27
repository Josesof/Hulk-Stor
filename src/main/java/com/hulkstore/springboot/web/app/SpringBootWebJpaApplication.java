package com.hulkstore.springboot.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hulkstore.springboot.web.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootWebJpaApplication implements CommandLineRunner{

	@Autowired
	IUploadFileService  uploadFileService;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebJpaApplication.class, args);
	}
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 

	@Override
	public void run(String... args) throws Exception {		
		uploadFileService.deletAll();
		uploadFileService.init();
		
		String password = "12345";
		
		for(int i=0; i<2; i++) {
			
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}

}
