drop database if exists reportservicedb;

create database reportservicedb;

drop table if exists report_activity;

create table report_activity(
                                id serial primary key,
                                type varchar,
                                description varchar,
                                base_price int,
                                coefficient boolean
);

drop table if exists reports;

create table reports (
                         id bigserial primary key,
                         data date,
                         description varchar,
                         url varchar,
                         user_id bigint,
                         status varchar default 'NEW',
                         cost int default 1,
                         report_activity_id int references report_activity(id)

);
