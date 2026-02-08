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

CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL
);


CREATE TABLE users_roles (
    user_fk BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_fk) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_fk, role)
);