package com.EDTECH.QUIZ.GAME;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QuizGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizGameApplication.class, args);
	}

}
