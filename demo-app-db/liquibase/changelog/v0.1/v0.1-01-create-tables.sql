--liquibase formatted sql
--changeset username:1
--preconditions onFail:MARK_RAN
create table objects
(
    id            uuid not null primary key,
    type_id       bigint,
    region        varchar(255),
    string_field  varchar(255),
    int_field     bigint,
    decimal_field double precision,
    json_field    jsonb,
    created_at    timestamp,
    updated_at    timestamp,
    deleted       boolean
)