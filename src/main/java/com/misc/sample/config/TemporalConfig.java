package com.misc.sample.config;

import com.misc.sample.review.workflow.ReviewWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    @Bean(destroyMethod = "shutdown")
    public WorkflowServiceStubs workflowServiceStubs(@Value("${spring.temporal.connection.target}") String target) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder().setTarget(target).build();
        return WorkflowServiceStubs.newServiceStubs(options);
    }

    @Bean
    public WorkflowClient workflowClient(
            WorkflowServiceStubs workflowServiceStubs, @Value("${spring.temporal.namespace}") String namespace) {
        WorkflowClientOptions options = WorkflowClientOptions.newBuilder().setNamespace(namespace).build();
        return WorkflowClient.newInstance(workflowServiceStubs, options);
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(TemporalQueues.REVIEW);
        worker.registerWorkflowImplementationTypes(ReviewWorkflowImpl.class);
        return factory;
    }
}