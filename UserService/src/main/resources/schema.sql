drop database if exists userServiceDB;

create database userServiceDB;

drop table if exists users;

create table users(
                      id bigserial primary key,
                      name varchar unique,
                      tgid bigint unique,
                      role varchar default 'GUEST',
                      score int default 0,
                      token varchar,
                      expires_at timestamp
);