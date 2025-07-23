package org.example;

import org.example.controller.TGBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableKafka
@SpringBootApplication
public class TelegramGatewayApplication {

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext context = SpringApplication.run(TelegramGatewayApplication.class, args);
			TGBot bot = context.getBean(TGBot.class);
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(bot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
