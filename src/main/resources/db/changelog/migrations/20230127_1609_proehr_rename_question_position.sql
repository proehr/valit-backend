-- liquibase formatted sql

-- changeset liquibase:20230127_1609_proehr_rename_question_position.sql
alter table question
    rename column question_value to question_position;

