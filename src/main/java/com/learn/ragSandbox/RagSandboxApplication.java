package com.learn.ragSandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration.class
})
public class RagSandboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagSandboxApplication.class, args);
	}

}
