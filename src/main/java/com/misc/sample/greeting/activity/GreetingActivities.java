package com.misc.sample.greeting.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface GreetingActivities {

    String createGreeting(String name);

}
