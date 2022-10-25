package com.example.demo.jobs;

import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Person;

@Component
public class Chunk6 {

    @Bean
    public JdbcCursorItemReader<Person> personCursorItemReader6() {

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
            .name("personReader6")
            .dataSource(srcSource)
            .beanRowMapper(Person.class)
            .sql(sql)
            .build();

        return reader;
    }

    @Bean
    public ItemProcessor<Person, Person> personProcessor6() {
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

    // @Bean("personWriter6")
    @Bean
    public ItemWriter<Person> personWriter6() {
        // return new JdbcBatchItemWriterBuilder<Person>()
        //     .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
        //     .sql("INSERT INTO people_dist (first_name, last_name) VALUES (:firstName, :lastName)")
        //     .dataSource(dataSource)
        //     .build();
        System.out.println("personWriter6");
        return new PersonItemJdbcWriter();
    }
    @Component
    private class PersonItemJdbcWriter implements ItemWriter<Person> {

        @Autowired
        private DataSource dataSource;

        private static final String SQL = "INSERT INTO people_dist (first_name, last_name) VALUES (?, ?)";

        @Override
        public void write(List<? extends Person> items) throws Exception {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL);
            for(Person p : items) {
                System.out.println("Person=" + p);
                preparedStatement.setString(1, p.getFirstName());
                preparedStatement.setString(2, p.getLastName());
                preparedStatement.addBatch();
            }
            int count[] = preparedStatement.executeBatch();
            preparedStatement.close();
            System.out.print("count=");
            for (int i : count) {
                System.out.print(i + ",");
            }
            System.out.println("");
        }
    }
}
