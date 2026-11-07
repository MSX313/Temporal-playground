package com.misc.sample.review.workflow;

import com.misc.sample.model.ReviewState;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ReviewWorkflow {

    @WorkflowMethod
    String start(String requestId, String initialAssignee);

    @SignalMethod
    void reassign(String newAssignee);

    @SignalMethod
    void approve();

    @SignalMethod
    void reject();

    @QueryMethod
    ReviewState getState();
}