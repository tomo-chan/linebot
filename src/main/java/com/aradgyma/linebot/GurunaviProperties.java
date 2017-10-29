package com.aradgyma.linebot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "gurunavi")
public class GurunaviProperties {

    private String accesskey;
}
