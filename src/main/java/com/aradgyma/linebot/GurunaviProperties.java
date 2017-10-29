package com.aradgyma.linebot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "gurunavi")
public class GurunaviProperties {

    @NotNull
    private String accesskey;
}
