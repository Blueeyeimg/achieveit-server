package com.ecnu.achieveit;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.ecnu.achieveit.dao")
public class AchieveitApplication {

	public static void main(String[] args) {
		SpringApplication.run(AchieveitApplication.class, args);
	}

}
