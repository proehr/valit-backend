-- liquibase formatted sql

-- changeset liquibase:20230207_1411_proehr_add_question_visual_types.sql
ALTER TYPE question_type RENAME VALUE 'SCALE' TO 'CHOICE';
CREATE TYPE visualization_type as enum ('SCALE', 'PIE', 'BAR');

alter table question
    add visualization_type visualization_type;
