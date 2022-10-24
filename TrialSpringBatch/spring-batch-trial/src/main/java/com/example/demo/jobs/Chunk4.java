package com.example.demo.jobs;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Person;

@Component
public class Chunk4 {

    @Bean
    public FlatFileItemReader<Person> personItemReader4() {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personItemReader4")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }})
            .build();
    }

    @Bean
    public ItemProcessor<Person, Person> personProcessor4() {
        return new ItemProcessor<Person,Person>() {
            @Override
            public Person process(final @NonNull Person person) throws Exception {
                final String firstName = person.getFirstName().toUpperCase();
                final String lastName = person.getLastName().toUpperCase();
                final Person transformedPerson = new Person(firstName, lastName);
                return transformedPerson;
            }
        };
    }

    @Bean("personWriter4")
    public JdbcBatchItemWriter<Person> personWriter4(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build();
    }
}
