package com.aradgyma.linebot;

import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;
import com.aradgyma.linebot.service.GurunaviService;
import com.aradgyma.linebot.service.GurunaviServiceImpl;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
@LineMessageHandler
public class LinebotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinebotApplication.class, args);
	}

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        String message = event.getMessage().getText();
        if (message.contains("ぐるなび")) {
            GurunaviService guruService = new GurunaviServiceImpl();
            try {
                ArrayList<Restaurant> restaurantList = guruService.getRestaurantList();
                if(restaurantList.size() > 1) {
                    return new TextMessage(restaurantList.get(0).toString());
                } else {
                    return new TextMessage("ヒットせず・・・");
                }
            } catch (BotException e) {
                e.printStackTrace();
                return new TextMessage("エラーでしたよ");
            }
        } else {
            return new TextMessage(event.getMessage().getText());
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
