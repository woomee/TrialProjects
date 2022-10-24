package com.example.demo.jobs;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Person;

@Component
public class Chunk5 {

    @Bean
    public JdbcCursorItemReader personCursorItemReader5() {

        // 読み込み元のDataSoruceを取得
        DataSource srcSource = DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:tcp://127.0.1.1:9092/./data/h2")
            .username("sa")
            .password("")
            .build();
        String sql = "SELECT FIRST_NAME, LAST_NAME FROM PEOPLE";

        // var reader = new JdbcCursorItemReader<Person>();
        // reader.setDataSource(srcSource);

        // // 読み込むSQLとRowMapper
        // var srcRowMapper = new BeanPropertyRowMapper<>(Person.class);
        // reader.setRowMapper(srcRowMapper);
        // reader.setSql("SELECT FIRST_NAME, LAST_NAME FROM PEOPLE");
    
        var reader = 
            new JdbcCursorItemReaderBuilder<Person>()
            .name("personReader5")
            .dataSource(srcSource)
            .beanRowMapper(Person.class)
            .sql(sql)
            .build();

        return reader;
    }

    @Bean
    public ItemProcessor<Person, Person> personProcessor5() {
        return new ItemProcessor<Person,Person>() {
            @Override
            public Person process(final @NonNull Person person) throws Exception {
                /* 小文字に置換する */
                final String firstName = person.getFirstName().toLowerCase();
                final String lastName = person.getLastName().toLowerCase();
                final Person transformedPerson = new Person(firstName, lastName);
                return transformedPerson;
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<Person> personWriter5(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
            .sql("INSERT INTO people_dist (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build();
    }
}
