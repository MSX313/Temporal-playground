package com.misc.sample.review.workflow;

import com.misc.sample.model.ReviewState;

import com.misc.sample.model.ReviewStatus;
import io.temporal.workflow.Workflow;

public class ReviewWorkflowImpl implements ReviewWorkflow {

    private String requestId;
    private String assignee;

    private ReviewStatus status = ReviewStatus.WAITING_FOR_REVIEW;

    @Override
    public String start(String requestId, String initialAssignee) {
        this.requestId = requestId;
        // اگر Signal خیلی زود رسیده باشد، مسئول جدید را بازنویسی نکن.
        if (this.assignee == null) {
            this.assignee = initialAssignee;
        }
        Workflow.await(() -> status != ReviewStatus.WAITING_FOR_REVIEW);
        return status.name();
    }

    @Override
    public void reassign(String newAssignee) {
        if (status == ReviewStatus.WAITING_FOR_REVIEW) {
            this.assignee = newAssignee;
        }
    }

    @Override
    public void approve() {

        if (status == ReviewStatus.WAITING_FOR_REVIEW) {
            this.status = ReviewStatus.APPROVED;
        }
    }

    @Override
    public void reject() {

        if (status == ReviewStatus.WAITING_FOR_REVIEW) {
            this.status = ReviewStatus.REJECTED;
        }
    }

    @Override
    public ReviewState getState() {
        return new ReviewState(requestId, assignee, status);
    }
}