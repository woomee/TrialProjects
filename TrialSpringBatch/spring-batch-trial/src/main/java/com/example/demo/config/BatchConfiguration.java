package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  // @Bean
  // public BatchConfigurer batchConfigurer(DataSource dataSource) {
  //   return new DefaultBatchConfigurer(dataSource) {

  //     // @Override
  //     // protected JobRepository createJobRepository() throws Exception {
  //     //     JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
  //     //     factory.setDataSource(dataSource);
  //     //     // factory.setTransactionManager(transactionManager);
  //     //     factory.setTransactionManager(getTransactionManager());
  //     //     // factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
  //     //     factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
          
  //     //     factory.setTablePrefix("BATCH_");
  //     //     factory.setMaxVarCharLength(1000);
  //     //     return factory.getObject();
  //     // }

  //     // @Override
  //     // public PlatformTransactionManager getTransactionManager() {
  //     //   return new JdbcTransactionManager(dataSource);
  //     //   // return new MyTransactionManager();
  //     // }
  //   };
  // }
}