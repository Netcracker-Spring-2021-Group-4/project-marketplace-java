DROP TABLE IF EXISTS auth_user;
DROP TABLE IF EXISTS confirmation_token;
DROP TABLE IF EXISTS permission_role;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS permission;


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE auth_user
(
    user_id UUID DEFAULT uuid_generate_v1 (),
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(96) NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT auth_user_pk PRIMARY KEY (user_id)
);

CREATE INDEX idx_auth_user_email
ON auth_user(email);

CREATE TABLE confirmation_token
(
    token UUID DEFAULT uuid_generate_v1 (),
    created_at TIMESTAMPTZ NOT NULL,
    expired_at TIMESTAMPTZ NOT NULL,
    activated BOOLEAN NOT NULL,
    patch_password BOOLEAN NOT NULL,
    CONSTRAINT confirmation_token_pk PRIMARY KEY (token)
);

CREATE TABLE user_role
(
    role_id SERIAL NOT NULL,
    role_name VARCHAR(32) NOT NULL,
    CONSTRAINT user_role_pk PRIMARY KEY (role_id)
);

CREATE TABLE permission
(
    permission_id SERIAL NOT NULL,
    permission_name VARCHAR(32) NOT NULL,
    CONSTRAINT permission_pk PRIMARY KEY (permission_id)
);

CREATE TABLE permission_role
(
    permission_id SERIAL NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT permission_role_pk PRIMARY KEY (permission_id, role_id),
    CONSTRAINT permission_role_role_id_fk
        foreign key (role_id) REFERENCES user_role
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT permission_role_permission_id_fk
        foreign key (permission_id) REFERENCES permission
            ON UPDATE CASCADE ON DELETE CASCADE
);