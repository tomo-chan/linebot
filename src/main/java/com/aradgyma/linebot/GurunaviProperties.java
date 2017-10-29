package com.aradgyma.linebot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gurunavi")
public class GurunaviProperties {

    public String accesskey;
}
