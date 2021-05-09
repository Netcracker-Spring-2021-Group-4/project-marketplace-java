drop TABLE IF EXISTS auth_user;
drop TABLE IF EXISTS confirmation_token;
drop TABLE IF EXISTS permission_role;
drop TABLE IF EXISTS user_role;
drop TABLE IF EXISTS permission;
drop TABLE IF EXISTS user_status;

create EXTENSION IF NOT EXISTS "uuid-ossp";

create table auth_user
(
    user_id uuid default uuid_generate_v1 (),
    email varchar(128) not null unique,
    password varchar(96) not null,
    role_id integer not null,
    status_id integer not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    constraint auth_user_pk primary key (user_id)
);

create table user_status
(
    status_id serial not null,
    status_name varchar(32) unique,
    constraint user_status_pk primary key (status_id)
);

create index idx_auth_user_email
on auth_user(email);

-- UUID is not intended to be secure
--CREATE TABLE confirmation_token
--(
--    token UUID DEFAULT uuid_generate_v1 (),
--    created_at TIMESTAMPTZ NOT NULL,
--    expired_at TIMESTAMPTZ NOT NULL,
--    activated BOOLEAN NOT NULL,
--    patch_password BOOLEAN NOT NULL,
--    CONSTRAINT confirmation_token_pk PRIMARY KEY (token)
--);

create table confirmation_token
(
    token varchar(512),
    expired_at TIMESTAMPTZ not null,
    user_email varchar(128) not null,
    is_activated boolean default false not null,
    constraint confirmation_token_pk primary key (token)
);

create table user_role
(
    role_id serial not null,
    role_name varchar(32) not null,
    constraint user_role_pk primary key (role_id)
);

create table permission
(
    permission_id serial not null,
    permission_name varchar(32) not null,
    constraint permission_pk primary key (permission_id)
);

create table permission_role
(
    permission_id serial not null,
    role_id integer not null,
    constraint permission_role_pk primary key (permission_id, role_id),
    constraint permission_role_role_id_fk
        foreign key (role_id) references user_role
        on update CASCADE ON delete CASCADE,
    CONSTRAINT permission_role_permission_id_fk
        foreign key (permission_id) REFERENCES permission
            ON update CASCADE ON delete CASCADE
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nc_course;