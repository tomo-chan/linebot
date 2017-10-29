package com.aradgyma.linebot.service;

import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;

import java.util.ArrayList;

public interface GurunaviService {

    ArrayList<Restaurant> getRestaurantList() throws BotException;
}
