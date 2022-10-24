package com.example.demo.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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