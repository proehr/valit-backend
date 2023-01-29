-- liquibase formatted sql

-- changeset liquibase:20230125_1545_proehr_refactor_evaluation_structure.sql
create table question_choice
(
    question_choice_id bigserial
        constraint question_choice_pk
            primary key,
    question_id        bigint
        constraint choice_question_question_question_id_fk
            references question
            on update cascade on delete cascade,
    choice_key         text,
    choice_value       integer
);
