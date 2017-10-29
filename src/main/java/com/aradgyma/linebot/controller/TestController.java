package com.aradgyma.linebot.controller;

import com.aradgyma.linebot.GurunaviProperties;
import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;
import com.aradgyma.linebot.service.GurunaviServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/test")
public class TestController {

    private final GurunaviServiceImpl gurunaviService;

    @Autowired
    public TestController(GurunaviServiceImpl gurunaviService) {
        this.gurunaviService = gurunaviService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ArrayList<Restaurant> get() throws BotException {
        return gurunaviService.getRestaurantList();
    }
}
