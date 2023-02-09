-- liquibase formatted sql

-- changeset liquibase:20230209_1641_proehr_add_summer_semester.sql
insert into semester(semester_name, start_date, end_date)
values ('SoSe23', TO_DATE('01/04/2023', 'DD/MM/YYYY'), TO_DATE('05/08/2023', 'DD/MM/YYYY'));
