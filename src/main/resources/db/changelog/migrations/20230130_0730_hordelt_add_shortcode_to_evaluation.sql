-- liquibase formatted sql

-- changeset liquibase:20230130_0730_hordelt_add_shortcode_to_evaluation.sql
alter table evaluation
    add shortcode integer;

