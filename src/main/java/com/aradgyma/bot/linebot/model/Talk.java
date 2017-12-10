package com.aradgyma.bot.linebot.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class Talk {

//    private TalkState state;
    private final HashMap<String, Object> variables = new HashMap<>();
}
