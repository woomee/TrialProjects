package com.example.demo.controller;

import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jobs.Job1;
import com.example.demo.jobs.Job2;
import com.example.demo.jobs.Tasklet1_1;
import com.example.demo.jobs.Tasklet2_1;
import com.example.demo.jobs.Tasklet2_2;
import com.example.demo.jobs.JobCompletionNotificationListener;

@RestController
// @RequestMapping("/job/")
public class JobController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job1 sampleJobConfig1;
    @Autowired
    Tasklet1_1 tasklet1_1;

    @Autowired
    Job2 sampleJobConfig2;
    @Autowired
    Tasklet2_1 tasklet2_1;
    @Autowired
    Tasklet2_2 tasklet2_2;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    JobCompletionNotificationListener jobListener;


    // @GetMapping("/launch/{jobName}")
    @GetMapping("/launchJob1")
    public String launchJob1() throws Exception{
        Step step1_1 = sampleJobConfig1.step1_1(tasklet1_1);
        Job job = sampleJobConfig1.sampleJob1(jobListener, step1_1);

        return executeJob(job);
    }

    @GetMapping("/launchJob2")
    public String launchJob2() throws Exception{
        Step step2_1 = sampleJobConfig2.step2_1(tasklet2_1);
        Step step2_2 = sampleJobConfig2.step2_2(tasklet2_2);
        Job job = sampleJobConfig2.sampleJob2(step2_1, step2_2);

        return executeJob(job);
    }

    @GetMapping("/stopJob/{jobName}")
    public String stopJon(@PathVariable String jobName) {
        try {
            Set<Long> executions = jobOperator.getRunningExecutions(jobName);
            boolean isSuccess = jobOperator.stop(executions.iterator().next());
            return "Stop job. Success=" + isSuccess + ", jobName=" + jobName;
        }
        catch (NoSuchJobException nsje) {
            return "No such job. jobName=" + jobName;
        }
        catch (NoSuchJobExecutionException | JobExecutionNotRunningException  jenre) {
            return "Not runnning job. jobName=" + jobName;
        }
    }


    private String executeJob(Job job) throws Exception {
        JobParameters jobParam = new JobParametersBuilder()
            .addString(job.getName(), String.valueOf(System.currentTimeMillis())).toJobParameters();
        JobExecution jobExec = jobLauncher.run(job, jobParam);
        return "Finish " + job.getName() + " <br/>" +
            "JobExec=" + jobExec.toString().replaceAll(",", ",<br/>");
    }

}
