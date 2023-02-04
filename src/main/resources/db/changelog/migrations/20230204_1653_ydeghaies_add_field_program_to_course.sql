-- liquibase formatted sql

-- changeset liquibase:20230204_1653_ydeghaies_add_field_program_to_course
ALTER TABLE course
    ADD program VARCHAR(255);

