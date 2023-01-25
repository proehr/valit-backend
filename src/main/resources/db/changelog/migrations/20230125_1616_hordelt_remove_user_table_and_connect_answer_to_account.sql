-- liquibase formatted sql

-- changeset liquibase:20230125_1616_hordelt_remove_user_table_and_connect_answer_to_account.sql
alter table answer
    drop column user_fk;

alter table answer
    add account_fk bigint;

alter table answer
    add constraint answer_account_account_id_fk
        foreign key (account_fk) references account;

drop table "user";