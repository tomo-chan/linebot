package com.aradgyma.linebot.model.gurunavi;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Restaurant {

    private String id;
    private String name;
    private String line;
    private String station;
    private String walk;
}
