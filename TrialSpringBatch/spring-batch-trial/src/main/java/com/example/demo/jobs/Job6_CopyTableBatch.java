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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Person;

@Configuration
@EnableBatchProcessing
public class Job6_CopyTableBatch {
    public static final String JOB_NAME=Job6_CopyTableBatch.class.getSimpleName();
    public static final String STEP_NAME=JOB_NAME.replace("Job", "Step");

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // @Autowired
    // private Chunk6 chunk6;

    @Bean
    public Job job6_ChunkJob(JobCompletionNotificationListener listener, Step step6) {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step6)
            .end()
            .build();
    }

    
    @Bean("step6")
    @Qualifier("personWriter6")
    public Step step6(
            ItemReader<Person> personCursorItemReader6,
            ItemProcessor<Person, Person> personProcessor6,
            ItemWriter<Person> personWriter6) {
        return stepBuilderFactory.get(STEP_NAME)
            .<Person, Person> chunk(3)
            .reader(personCursorItemReader6)
            .processor(personProcessor6)
            .writer(personWriter6)
            .build();
    }

    // OKだが、Stepの引数の方がよい
    // @Bean("step6")
    // @Qualifier("personWriter6")
    // public Step step6(ItemWriter<Person> personWriter6) {
    //     return stepBuilderFactory.get(STEP_NAME)
    //         .<Person, Person> chunk(3)
    //         .reader(chunk6.personCursorItemReader6())
    //         .processor(chunk6.personProcessor6())
    //         .writer(personWriter6)
    //         .build();
    // }
    
    // NG
    // @Bean("step6")
    // public Step step6() {
    //     return stepBuilderFactory.get(STEP_NAME)
    //         .<Person, Person> chunk(3)
    //         .reader(chunk6.personCursorItemReader6())
    //         .processor(chunk6.personProcessor6())
    //         .writer(chunk6.personWriter6())
    //         .build();
    // }
}
