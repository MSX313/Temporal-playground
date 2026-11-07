package com.misc.sample.review.api;

import com.misc.sample.config.TemporalQueues;
import com.misc.sample.review.workflow.ReviewWorkflow;
import com.misc.sample.model.ReviewState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REVIEWS Controller API")

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final WorkflowClient workflowClient;

    @Operation(summary = "start flow API", description = "This web service is used to start flow normaly")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "start flow successfully",
                            content = {@Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StartResponse.class))
                            })})
    @PostMapping(name = "start", path = "/flow/start")
    public StartResponse start(@RequestBody StartRequest request) {

        String workflowId = "review-" + request.requestId();

        ReviewWorkflow workflow =
                workflowClient.newWorkflowStub(ReviewWorkflow.class,
                        WorkflowOptions.newBuilder().setWorkflowId(workflowId).setTaskQueue(TemporalQueues.REVIEW).build());
        WorkflowClient.start(workflow::start, request.requestId(), request.assignee());
        return new StartResponse(workflowId);
    }

    public ReviewState getState(@PathVariable String workflowId) {
        return existingWorkflow(workflowId).getState();
    }

    @PostMapping("/{workflowId}/reassign")
    public void reassign(@PathVariable String workflowId, @RequestBody ReassignRequest request) {
        existingWorkflow(workflowId).reassign(request.assignee());
    }

    @PostMapping("/{workflowId}/approve")
    public void approve(@PathVariable String workflowId) {
        existingWorkflow(workflowId).approve();
    }

    @PostMapping("/{workflowId}/reject")
    public void reject(@PathVariable String workflowId) {
        existingWorkflow(workflowId).reject();
    }

    private ReviewWorkflow existingWorkflow(String workflowId) {
        return workflowClient.newWorkflowStub(ReviewWorkflow.class, workflowId);
    }

    public record StartRequest(
            String requestId,
            String assignee
    ) {
    }

    public record ReassignRequest(String assignee) {
    }

    public record StartResponse(String workflowId) {
    }
}