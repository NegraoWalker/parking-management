package com.walker.parkingmanagement.configuration;

import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

public class SpringTimezoneConfiguration {
    @PostConstruct
    public void timezoneConfiguration(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
