package com.aradgyma.bot.gurunavi.service;

import com.aradgyma.bot.linebot.exception.BotException;
import com.aradgyma.bot.gurunavi.model.Restaurant;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public interface GurunaviService {

    ArrayList<Restaurant> getRestaurantList(@NotNull  String message, @NotNull Double lat, @NotNull Double lon) throws BotException;
}
