-- liquibase formatted sql

-- changeset liquibase:20230207_1713_proehr_add_timestamp_to_answer.sql
alter table answer
    add created_at timestamp;
