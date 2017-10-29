package com.aradgyma.linebot.controller;

import com.aradgyma.linebot.GurunaviProperties;
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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@LineMessageHandler
public class LinebotController {


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        String message = event.getMessage().getText();
        if (message.contains("ぐるなび")) {
            System.out.println("Contains: ぐるなび");
            GurunaviService guruService = new GurunaviServiceImpl(new GurunaviProperties());
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
