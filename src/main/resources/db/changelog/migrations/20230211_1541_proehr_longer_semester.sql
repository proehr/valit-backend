-- liquibase formatted sql

-- changeset liquibase:20230211_1541_proehr_longer_semester.sql
UPDATE semester SET end_date='2023-03-31' WHERE semester_id=1;
