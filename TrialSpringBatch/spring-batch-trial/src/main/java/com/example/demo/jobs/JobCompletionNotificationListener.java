package com.example.demo.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobInstance;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  @Override
  public void afterJob(JobExecution jobExecution) {
    JobInstance jobInstance = jobExecution.getJobInstance();
    log.info("After Job. JobName=" + jobInstance.getJobName());

    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("Job COMPLETED! Time to verify the results");
    }
    
  }
}