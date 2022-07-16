package com.musicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MusicLoudApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicLoudApplication.class, args);
	}

}
