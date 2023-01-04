-- liquibase formatted sql

-- changeset liquibase:20230102_1837_proehr_refactor_accounts.sql
alter table account
    rename column email to username;


