package com.misc.sample.greeting.workflow;


import com.misc.sample.greeting.activity.GreetingActivities;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class GreetingWorkflowImpl implements GreetingWorkflow {

    private final GreetingActivities activities =
            Workflow.newActivityStub(
                    GreetingActivities.class, ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10)).build());

    @Override
    public String greet(String name) {
        return activities.createGreeting(name);
    }
}