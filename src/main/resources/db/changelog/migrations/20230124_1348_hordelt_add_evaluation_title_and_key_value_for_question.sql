-- liquibase formatted sql

-- changeset liquibase:20230124_1348_hordelt_add_evaluation_title_and_key_value_for_question.sql
alter table evaluation
    add title text;

alter table question
    rename column value to question_key;

alter table question
    add question_value integer;

create table date
(
    date_id bigserial
        primary key,
    date    date
);

alter table date
    owner to postgres;

alter table course
    add date_fk       bigint;

alter table course
    add constraint course_date_date_id_fk
        foreign key (date_fk) references date (date_id);

alter table evaluation
    add date_fk bigint;

alter table evaluation
    add constraint evaluation_date_date_id_fk
        foreign key (date_fk) references date (date_id);

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

alter table answer
    owner to postgres;

alter table int_answer
drop column question;

alter table int_answer
drop column "user";

alter table int_answer
    add answer_fk bigint;

alter table int_answer
    add constraint int_answer_answer_answer_id_fk
        foreign key (answer_fk) references answer (answer_id);

alter table string_answer
drop column question;

alter table string_answer
drop column "user";

alter table string_answer
    add answer_fk bigint;

alter table string_answer
    add constraint string_answer_answer_answer_id_fk
        foreign key (answer_fk) references answer (answer_id);

alter table course
drop column dates;

alter table evaluation
drop column date;


alter table course
drop column date_fk;

alter table date
    add course_fk bigint;

alter table date
    add constraint date_course_course_id_fk
        foreign key (course_fk) references course (course_id);

alter table evaluation
    drop column date_fk;

alter table evaluation
    add date date;
