package com.misc.sample.greeting.api;

import com.misc.sample.config.TemporalQueues;
import com.misc.sample.greeting.workflow.GreetingWorkflow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Greeting Controller API")

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    private final WorkflowClient workflowClient;


    @Operation(
            summary = "start flow API",
            description = "This web service is used to start flow normaly"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "start flow successfully",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(
                                                    implementation = StartGreetingResponse.class
                                            )
                                    )
                            }
                    )
            }
    )
    @PostMapping(name = "start", path = "/flow/start")
    public ResponseEntity<StartGreetingResponse> start(@RequestBody StartGreetingRequest request) {
        String workflowId = "greeting-" + UUID.randomUUID();
        GreetingWorkflow workflow =
                workflowClient.newWorkflowStub(
                        GreetingWorkflow.class,
                        WorkflowOptions.newBuilder().setWorkflowId(workflowId).setTaskQueue(TemporalQueues.GREETING).build());
        WorkflowExecution execution = WorkflowClient.start(workflow::greet, request.name());
        return ResponseEntity.accepted().body(new StartGreetingResponse(execution.getWorkflowId(),execution.getRunId()));
    }

    public record StartGreetingRequest(String name) {
    }

    public record StartGreetingResponse(
            String workflowId,
            String runId
    ) {
    }
}