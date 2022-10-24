-- DROP TABLE people IF EXISTS;

CREATE TABLE IF NOT EXISTS people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


-- DROP TABLE people_dist IF EXISTS;

CREATE TABLE IF NOT EXISTS people_dist  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);
