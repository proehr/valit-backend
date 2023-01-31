-- liquibase formatted sql

-- changeset yasinedeghaies:1675173885846-21
CREATE TABLE file_data
(
    id          BIGINT NOT NULL,
    name        VARCHAR(255),
    type        VARCHAR(255),
    file_path   VARCHAR(255),
    lecturer_fk BIGINT,
    CONSTRAINT pk_file_data PRIMARY KEY (id)
);

-- changeset yasinedeghaies:1675173885846-22
ALTER TABLE file_data
    ADD CONSTRAINT FK_FILE_DATA_ON_LECTURER_FK FOREIGN KEY (lecturer_fk) REFERENCES lecturer (lecturer_id);

