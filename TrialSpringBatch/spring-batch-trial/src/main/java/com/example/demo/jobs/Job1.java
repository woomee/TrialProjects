package com.example.demo.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Job1
 * 
 * Tasklet1_1だけ実行する
 */
@Component
@EnableBatchProcessing
public class Job1 {

    public static final String JOB_NAME="sampleJob1";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
  
    @Bean
    public Job sampleJob1(JobCompletionNotificationListener listener, Step step1_1) {
      return jobBuilderFactory.get(JOB_NAME)
        .listener(listener)
        .start(step1_1)
        .build();
    }

    @Bean
    public Step step1_1(Tasklet1_1 tasklet) {
      return stepBuilderFactory.get("step1_1").tasklet(tasklet).build();
    }
  
}
