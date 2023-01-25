-- liquibase formatted sql

-- changeset liquibase:20230124_1348_hordelt_add_evaluation_title_and_key_value_for_question.sql
create table answer
(
    answer_id   bigserial
        primary key,
    question_fk bigint
        constraint answer_question_question_id_fk
            references question,
    user_fk     bigint
        constraint answer_user_user_id_fk
            references "user"
);

alter table evaluation
    add title text;

alter table question
    rename column value to question_key;

alter table question
    add question_value integer;

alter table int_answer
    drop column question;
alter table int_answer
    drop column "user";
alter table int_answer
    add answer_id bigint;
alter table int_answer
    add constraint int_answer_answer_answer_id_fk
        foreign key (answer_id) references answer (answer_id);
alter table int_answer
    drop constraint int_answer_pk;
alter table int_answer
    drop column int_answer_id;
alter table int_answer
    add constraint int_answer_pk
        primary key (answer_id);

alter table string_answer
    drop column question;
alter table string_answer
    drop column "user";
alter table string_answer
    add answer_id bigint;
alter table string_answer
    add constraint string_answer_answer_answer_id_fk
        foreign key (answer_id) references answer (answer_id);
alter table string_answer
    drop constraint string_answer_pk;
alter table string_answer
    drop column string_answer_id;
alter table string_answer
    add constraint string_answer_pk
        primary key (answer_id);

create table date
(
    date_id bigserial
        primary key,
    date    date
);

alter table course
    drop column dates;

alter table date
    add course_fk bigint;

alter table date
    add constraint date_course_course_id_fk
        foreign key (course_fk) references course (course_id);

