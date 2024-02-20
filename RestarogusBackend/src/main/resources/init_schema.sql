drop table if exists public.users;
drop sequence if exists public.menu_item_id_sequence;
drop table if exists public.menu_items;

create table public.users
(
    id       uuid not null
        constraint users_pk
            primary key,
    username varchar
        constraint users_pk_2
            unique,
    password varchar,
    role     varchar
);

alter table public.users
    owner to postgres;

create table public.menu_items
(
    id                   integer not null
        constraint menu_items_pk
            primary key,
    name                 varchar
        constraint menu_items_pk_2
            unique,
    price                double precision,
    cook_time_in_minutes integer,
    quantity             integer
);

alter table public.menu_items
    owner to postgres;

create sequence public.menu_item_id_sequence
    as integer;

alter sequence public.menu_item_id_sequence owner to postgres;