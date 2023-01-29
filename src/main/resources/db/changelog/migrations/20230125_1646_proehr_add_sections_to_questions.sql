-- liquibase formatted sql

-- changeset liquibase:20230125_1646_proehr_add_sections_to_questions.sql
alter table question
    add section_number int;
