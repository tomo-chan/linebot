package com.aradgyma.linebot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Component
@Validated
@Configuration
@ConfigurationProperties(prefix = "gurunavi")
public class GurunaviProperties {

    @NotNull
    private String accesskey;
}
