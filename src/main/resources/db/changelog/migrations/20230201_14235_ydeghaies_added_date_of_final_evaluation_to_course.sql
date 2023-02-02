-- liquibase formatted sql

-- changeset liquibase:20230201_14235_ydeghaies_added_date_of_final_evaluation_to_course.sql
ALTER TABLE course
    ADD final_eval_date date;

