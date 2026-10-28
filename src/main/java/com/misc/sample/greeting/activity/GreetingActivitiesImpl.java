package com.misc.sample.greeting.activity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GreetingActivitiesImpl implements GreetingActivities {


    @Override
    public String createGreeting(String name) {
        log.info("Creating greeting for: {}", name);
        return "سلام " + name + "، به Temporal خوش آمدی!";
    }
}
