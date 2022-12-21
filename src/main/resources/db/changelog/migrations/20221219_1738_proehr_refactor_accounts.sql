-- liquibase formatted sql

-- changeset liquibase:20221219_1738_proehr_refactor_accounts.sql
alter table account drop column password_salt;

insert into account(email, password_hash, type)
values ('test@test.com', '$2a$04$QA79Kjq2aIczTfoohsEtg.w1eDxYSZC6cw9y5ueRo3ljUR5Ufkan.', 'ADMIN')
