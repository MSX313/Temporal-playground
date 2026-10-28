package com.misc.sample.config;


import com.misc.sample.greeting.activity.GreetingActivitiesImpl;
import com.misc.sample.greeting.workflow.GreetingWorkflowImpl;
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
        return WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());
    }

    @Bean
    public WorkflowClient workflowClient(
            WorkflowServiceStubs workflowServiceStubs, @Value("${spring.temporal.namespace}") String namespace) {
        return WorkflowClient.newInstance(
                workflowServiceStubs, WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public WorkerFactory workerFactory(WorkflowClient workflowClient, GreetingActivitiesImpl greetingActivities) {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(TemporalQueues.GREETING);
        worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
        worker.registerActivitiesImplementations(greetingActivities);
        return factory;
    }
}