-- liquibase formatted sql

-- changeset liquibase:20230128_2326_ydeghaies_create_file_data_table.sql
CREATE TABLE file_data
(
    file_data_id bigserial
        constraint file_data_pk
            primary key,
    name         VARCHAR(255),
    type         VARCHAR(255),
    file_path    VARCHAR(255),
    lecturer_fk  BIGINT
);

ALTER TABLE file_data
    ADD CONSTRAINT FK_FILE_DATA_ON_LECTURER_FK FOREIGN KEY (lecturer_fk) REFERENCES lecturer (lecturer_id);

