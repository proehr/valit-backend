-- liquibase formatted sql

-- changeset liquibase:20230207_1218_proehr_change_degree_to_enum.sql
create type degree_type as enum ('MASTER', 'BACHELOR');
alter table course
    alter column degree type degree_type using degree::degree_type;
