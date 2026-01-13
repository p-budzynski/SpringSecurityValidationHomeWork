--liquibase formatted sql
--changeset SpringSecurityValidationHomeWork:1


CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    producer VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    vin_number VARCHAR(17) NOT NULL UNIQUE,
    year_of_production INTEGER NOT NULL,
    registration_number VARCHAR(10) NOT NULL UNIQUE
);