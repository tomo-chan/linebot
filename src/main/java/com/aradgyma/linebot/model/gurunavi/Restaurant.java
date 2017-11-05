package com.aradgyma.linebot.model.gurunavi;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Restaurant {

    private String id;
    private String name;
    private String address;
    private String tel;
    private String line;
    private String station;
    private int walk;
    private String imageUrl;
    private String siteUrl;
    private Double lat;
    private Double lon;
}
