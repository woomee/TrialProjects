package com.example.demo.jobs;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Person;

@Configuration
@EnableBatchProcessing
public class Job5_CopyTable {
    public static final String JOB_NAME=Job5_CopyTable.class.getSimpleName();
    public static final String STEP_NAME=JOB_NAME.replace("Job", "Step");

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // @Autowired
    // private Chunk5 chunk5;

    @Bean
    public Job job5_ChunkJob(JobCompletionNotificationListener listener, Step step5_1) {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step5_1)
            .end()
            .build();
    }

    @Bean
    public Step step5_1(
            JdbcCursorItemReader<Person> personCursorItemReader5,
            ItemProcessor<Person, Person> personProcessor5,
            JdbcBatchItemWriter<Person> personWriter5,
            DataSource dataSource) {
        return stepBuilderFactory.get(STEP_NAME)
            .<Person, Person> chunk(2)
            .reader(personCursorItemReader5)
            .processor(personProcessor5)
            .writer(personWriter5)      // OK
            // .writer(chunk5.personWriter5(dataSource))   // NG
            .build();
    }
}
