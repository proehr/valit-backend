-- liquibase formatted sql

-- changeset liquibase:20221122_1627_proehr_project_setup.sql
create type account_type as enum ('LECTURER', 'ADMIN', 'STUDENT');
create table account
(
    account_id    bigserial
        constraint account_pk
            primary key,
    password_hash text,
    password_salt text,
    email         text,
    created_at    timestamp,
    type          account_type
);
