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
 * Job2
 * 
 * Tasklet2_1, Tasklet2_2を実行する
 */
@Component
@EnableBatchProcessing
public class Job2 {
  
  public static final String JOB_NAME="sampleJob2";

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job sampleJob2(Step step2_1, Step step2_2) {
    return jobBuilderFactory.get(JOB_NAME).start(step2_1).next(step2_2).build();
  }

  @Bean
  public Step step2_1(Tasklet2_1 tasklet) {
    return stepBuilderFactory.get("step2_1").tasklet(tasklet).build();
  }

  @Bean
  public Step step2_2(Tasklet2_2 tasklet) {
    return stepBuilderFactory.get("step2_2").tasklet(tasklet).build();
  }
  
}
