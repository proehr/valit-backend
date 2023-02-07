-- liquibase formatted sql

-- changeset liquibase:20230206_1844_proehr_change_shortcode_type.sql
ALTER TABLE evaluation ALTER COLUMN shortcode TYPE text;

ALTER TABLE evaluation ADD COLUMN active bool;
