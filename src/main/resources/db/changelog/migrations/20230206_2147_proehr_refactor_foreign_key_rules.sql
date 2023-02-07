-- liquibase formatted sql

-- changeset liquibase:20230206_2147_proehr_refactor_foreign_key_rules.sql
alter table evaluation
    drop constraint evaluation_course_course_id_fk;

alter table evaluation
    add constraint evaluation_course_course_id_fk
        foreign key (course_fk) references course
            on delete cascade;

alter table question
    drop constraint question_evaluation_evaluation_id_fk;

alter table question
    add constraint question_evaluation_evaluation_id_fk
        foreign key (evaluation) references evaluation
            on delete cascade;

alter table answer drop constraint answer_question_question_id_fk;

alter table answer
    add constraint answer_question_question_id_fk
        foreign key (question_fk) references question
            on delete cascade;

alter table int_answer drop constraint int_answer_answer_answer_id_fk;

alter table int_answer
    add constraint int_answer_answer_answer_id_fk
        foreign key (answer_id) references answer
            on delete cascade;

alter table string_answer drop constraint string_answer_answer_answer_id_fk;

alter table string_answer
    add constraint string_answer_answer_answer_id_fk
        foreign key (answer_id) references answer
            on delete cascade;

alter table date drop constraint date_course_course_id_fk;

alter table date
    add constraint date_course_course_id_fk
        foreign key (course_fk) references course
            on delete cascade;
