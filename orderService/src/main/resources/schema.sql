drop database if exists orderServiceDB;

create database orderServiceDB;

drop table if exists orders;
drop table if exists shop_items;

create table shop_items (
                            id serial primary key,
                            type varchar(64),
                            description varchar(256),
                            price int
);

create table orders(
                       id bigserial primary key,
                       item_id int references shop_items(id),
                       user_id bigint,
                       data timestamp,
                       status varchar(64)
);