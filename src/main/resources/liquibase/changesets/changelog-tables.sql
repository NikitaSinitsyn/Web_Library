--changeset Nikita:1 tags=position rollbackTag=position_rollback
CREATE TABLE position (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

--changeset Nikita:2 tags=employee rollbackTag=employee_rollback
CREATE TABLE newEmployee (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          salary DOUBLE PRECISION,
                          position_id INT,
                          department_id INT
);

--changeset Nikita:3 tags=users rollbackTag=users_rollback
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       isenabled BOOLEAN DEFAULT TRUE
);

--changeset Nikita:4 tags=authority rollbackTag=authority_rollback
CREATE TABLE authority (
                           id BIGSERIAL PRIMARY KEY,
                           role VARCHAR(255) NOT NULL,
                           user_id bigint
);