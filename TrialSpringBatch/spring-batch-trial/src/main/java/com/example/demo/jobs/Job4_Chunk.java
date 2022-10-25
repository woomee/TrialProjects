package com.example.demo.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Person;

@Configuration
@EnableBatchProcessing
public class Job4_Chunk {
    public static final String JOB_NAME=Job4_Chunk.class.getSimpleName();

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // @Autowired
    // private Chunk4 chunk4;

    // @Autowired 
    // DataSource dataSource;

    // @Autowired
    // @Qualifier("personWriter4")
    // JdbcBatchItemWriter<Person> personWriter4;
    
    @Bean
    public Job Job4_ChunkJob(JobCompletionNotificationListener listener, Step step4_1) {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step4_1)
            .end()
            .build();
    }

    @Bean
    public Step step4_1(
        ItemReader<Person> personItemReader4,
        ItemProcessor<Person, Person> personProcessor4,
        ItemWriter<Person> personWriter4
    ) {
            return stepBuilderFactory.get("step4_1")
            .<Person, Person> chunk(2)
            .reader(personItemReader4)
            .processor(personProcessor4)
            .writer(personWriter4)           // OK
            // .writer(chunk4.personWriter4(dataSource))   // NG
            .build();
    }

    // @Bean
    // // public Step step4_1(JdbcBatchItemWriter<Person> personWriter4) {
    // public Step step4_1() {
    //         return stepBuilderFactory.get("step4_1")
    //         .<Person, Person> chunk(2)
    //         .reader(chunk4.personItemReader4())
    //         .processor(chunk4.personProcessor4())
    //         .writer(personWriter4)           // OK
    //         // .writer(chunk4.personWriter4(dataSource))   // NG
    //         .build();
    // }
}
