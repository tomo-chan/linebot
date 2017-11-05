package com.aradgyma.linebot.controller;

import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;
import com.aradgyma.linebot.model.Talk;
import com.aradgyma.linebot.service.GurunaviServiceImpl;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@Slf4j
@LineMessageHandler
public class LinebotController {

    // TODO: Manage into HttpSession
    private HashMap<String, Talk> talkMap = new HashMap<>();

    private final LineMessagingClient lineMessagingClient;

    private final GurunaviServiceImpl gurunaviService;

    @Autowired
    public LinebotController(GurunaviServiceImpl gurunaviService, LineMessagingClient lineMessagingClient) {
        this.gurunaviService = gurunaviService;
        this.lineMessagingClient = lineMessagingClient;
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        String senderId = event.getSource().getSenderId();
        Talk talk = talkMap.get(senderId);
        TextMessageContent textContent = event.getMessage();
        String message = textContent.getText();
        if (talk != null && talk.getVariables().get("lat") != null && talk.getVariables().get("lon") != null) {
            try {
                String lat = (String) talk.getVariables().get("lat");
                String lon = (String) talk.getVariables().get("lon");
                ArrayList<Restaurant> restaurantList = gurunaviService.getRestaurantList(message, lat, lon);
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

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        System.out.println(event);
        String senderId = event.getSource().getSenderId();
        Talk talk = talkMap.get(senderId);
        if(talk == null) {
            talk = new Talk();
            talkMap.put(senderId, talk);
        }
        LocationMessageContent locationMessage = event.getMessage();
        talk.getVariables().put("lat", locationMessage.getLatitude());
        talk.getVariables().put("lon", locationMessage.getLongitude());
        reply(event.getReplyToken(), new TextMessage("何が食べたい？"));
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
//            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
