package com.aradgyma.linebot.service;

import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public interface GurunaviService {

    ArrayList<Restaurant> getRestaurantList(@NotNull  String message, @NotNull Double lat, @NotNull Double lon) throws BotException;
}
