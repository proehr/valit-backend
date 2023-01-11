-- liquibase formatted sql

-- changeset liquibase:20230111_1348_hordelt_add_student_count.sql
alter table course
    add student_count integer;