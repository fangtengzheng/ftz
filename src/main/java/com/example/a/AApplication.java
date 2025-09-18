package com.example.a;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.a.mapper")
public class AApplication {
	public static void main(String[] args) {
		SpringApplication.run(AApplication.class, args);
	}
}