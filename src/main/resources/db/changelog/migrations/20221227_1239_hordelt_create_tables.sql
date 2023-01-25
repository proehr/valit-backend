-- liquibase formatted sql

-- changeset liquibase:20221227_1239_hordelt_create_tables
create sequence evaluation_course_id_seq;

create type evaluation_type as enum ('REGULAR', 'FINAL');

create type question_type as enum ('TEXT', 'SCALE');

create table lecturer
(
    first_name  text,
    last_name   text,
    title       text,
    lecturer_id bigserial
        constraint lecturer_pk
            primary key,
    account     bigint
        constraint lecturer_account_account_id_fk
            references account
);

create table course
(
    name        text,
    degree      text,
    time_start  time,
    time_end    time,
    course_id   bigserial
        constraint course_pk
            primary key,
    dates       date[],
    lecturer_fk bigint
        constraint course_lecturer_lecturer_id_fk
            references lecturer
);

create table evaluation
(
    type          evaluation_type,
    date          date,
    course_fk     bigint
        constraint evaluation_course_course_id_fk
            references course,
    evaluation_id bigint default nextval('evaluation_course_id_seq'::regclass) not null
        constraint evaluation_pk
            primary key
);

alter sequence evaluation_course_id_seq owned by evaluation.evaluation_id;

create table question
(
    type        question_type,
    question_id bigserial
        constraint question_pk
            primary key,
    evaluation  bigint
        constraint question_evaluation_evaluation_id_fk
            references evaluation,
    value       text
);

create table "user"
(
    user_id    bigserial
        constraint user_pk
            primary key,
    identifier text
);

create table int_answer
(
    value         integer,
    question      bigint
        constraint int_answer_question_question_id_fk
            references question,
    "user"        bigint
        constraint int_answer_user_user_id_fk
            references "user",
    int_answer_id bigserial
        constraint int_answer_pk
            primary key
);

create table string_answer
(
    value            text,
    question         bigint
        constraint string_answer_question_question_id_fk
            references question,
    "user"           bigint
        constraint string_answer_user_user_id_fk
            references "user",
    string_answer_id bigserial
        constraint string_answer_pk
            primary key
);

