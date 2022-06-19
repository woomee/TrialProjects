DROP TABLE people IF EXISTS;

CREATE TABLE IF NOT EXISTS people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


CREATE TABLE people  (
    person_id integer GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1 MAXVALUE 999999999 NOCYCLE) NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


declare
begin
  execute immediate 'create table "TABELLA" ("ID" number not null)';
  exception when others then
    if SQLCODE = -955 then null; else raise; end if;
end;