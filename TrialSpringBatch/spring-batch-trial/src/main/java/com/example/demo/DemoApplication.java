package com.example.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	// @Value("${spring.batch.job.names}")
	// private static String jobNames;

	static ConfigurableApplicationContext ctx;

	public static void main(String[] args) 
	throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException
	{
		ctx = SpringApplication.run(DemoApplication.class, args);

		// OK.ただ、jobNameは固定値
		// Jobの順番を制御
		// JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		// Job job2= (Job) ctx.getBean("sampleJob2");
		// Job job1= (Job) ctx.getBean("sampleJob");
		// jobLauncher.run(job2, new JobParameters());
		// jobLauncher.run(job1, new JobParameters());

		// NG jobNamesがnull値
		// String jobs[] = jobNames.split(",");
		// for (String job : jobs) {
		// 	Job jobObj = (Job) ctx.getBean(job);
		// 	jobLauncher.run(jobObj, new JobParameters());
		// }
	}

	// public DemoApplication() 
	// throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException
	// {
	// 	JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
	// 	String jobs[] = jobNames.split(",");
	// 	for (String job : jobs) {
	// 		Job jobObj = (Job) ctx.getBean(job);
	// 		jobLauncher.run(jobObj, new JobParameters());
	// 	}
	// }

	@Bean
	public BatchConfigurer batchConfigurer(DataSource dataSource) 
	{
		return new DefaultBatchConfigurer(dataSource) {
			// @Override
			// public JobLauncher getJobLauncher() {
			// 	// JobLauncher jobLauncher = super.getJobLauncher();
			// 	SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
			// 	System.out.println("JobNames=" + jobNames);
			// 	String jobs[] = jobNames.split(",");
			// 	for (String job : jobs) {
			// 		// Job jobObj = (Job) ctx.getBean(job);
			// 		Job jobObj = new SimpleJob(job);
			// 		try {
			// 			jobLauncher.run(jobObj, new JobParameters());
			// 		}
			// 		catch (Exception ex) {
			// 			ex.printStackTrace();
			// 		}
			// 	}
			// 	return jobLauncher;
			// }
			// @Override
			// protected JobLauncher createJobLauncher() throws Exception {
			// 	SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
			// 	jobLauncher.setJobRepository(createJobRepository());
			// 	jobLauncher.afterPropertiesSet();
			// 	String jobs[] = jobNames.split(",");
			// 	for (String job : jobs) {
			// 		// Job jobObj = (Job) ctx.getBean(job);
			// 		Job jobObj = new SimpleJob(job);
			// 		try {
			// 			jobLauncher.run(jobObj, new JobParameters());
			// 		}
			// 		catch (Exception ex) {
			// 			ex.printStackTrace();
			// 		}
			// 	}	
			// 	return jobLauncher;
			// }
		};
	}
}
