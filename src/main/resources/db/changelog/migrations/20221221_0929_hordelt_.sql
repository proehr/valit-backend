-- liquibase formatted sql

-- changeset liquibase:20221221_0929_hordelt_.sql
create sequence evaluation_course_id_seq;

alter sequence evaluation_course_id_seq owner to postgres;

create type account_type as enum ('LECTURER', 'ADMIN', 'STUDENT');

alter type account_type owner to postgres;

create type evaluation_type as enum ('REGULAR', 'FINAL');

alter type evaluation_type owner to postgres;

create type question_type as enum ('TEXT', 'SCALE');

alter type question_type owner to postgres;

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

alter table account
    owner to postgres;

create table lecturer
(
    first_name  text,
    last_name   text,
    title       text,
    lecturer_id bigserial
        constraint lecturer_pk
            primary key
);

alter table lecturer
    owner to postgres;

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

alter table course
    owner to postgres;

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

alter table evaluation
    owner to postgres;

alter sequence evaluation_course_id_seq owned by evaluation.evaluation_id;

create table question
(
    type        question_type,
    question_id bigserial
        constraint question_pk
            primary key,
    evaluation  bigint
        constraint question_evaluation_evaluation_id_fk
            references evaluation
);

alter table question
    owner to postgres;

create table "user"
(
    user_id    bigserial
        constraint user_pk
            primary key,
    identifier text
);

alter table "user"
    owner to postgres;

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

alter table int_answer
    owner to postgres;

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

alter table string_answer
    owner to postgres;


