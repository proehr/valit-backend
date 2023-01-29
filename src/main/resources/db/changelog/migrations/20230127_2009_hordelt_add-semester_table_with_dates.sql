-- liquibase formatted sql

-- changeset liquibase:20230127_2009_hordelt_add-semester_table_with_dates.sql
create table semester
(
    semester_name text,
    start_date  date,
    end_date    date,
    semester_id bigserial
        primary key
);

insert into semester(semester_name, start_date, end_date)
values ('WiSe2223', TO_DATE('01/10/2022', 'DD/MM/YYYY'), TO_DATE('11/02/2023', 'DD/MM/YYYY'));

create type interval_type as enum ('WEEKLY', 'BIWEEKLY', 'MONTHLY');
alter table course
    add interval interval_type;

alter table course
    add semester_fk bigint;

alter table course
    add constraint course_semester_semester_id_fk
        foreign key (semester_fk) references semester (semester_id);

create type weekday_type as enum ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');

alter table course
    add weekday weekday_type;
