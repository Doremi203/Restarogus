drop table if exists public.users;

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