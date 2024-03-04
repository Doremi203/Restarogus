create table if not exists public.restaurant_stats
(
    id      int not null default 1
        constraint restaurant_stats_pk
            primary key,
    revenue double precision
);

alter table public.restaurant_stats
    owner to postgres;

create table if not exists public.users
(
    id       bigint not null generated by default as identity
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

create table if not exists public.menu_items
(
    id                   bigint not null generated by default as identity
        constraint menu_items_pk
            primary key,
    name                 varchar
        constraint menu_items_uk
            unique,
    price                double precision,
    cook_time_in_minutes integer,
    quantity             integer
);

alter table public.menu_items
    owner to postgres;

create table if not exists public.orders
(
    id          bigint not null generated by default as identity
        constraint orders_pk
            primary key,
    customer_id bigint
        constraint orders_users_id_fk
            references public.users,
    status      varchar,
    date        timestamp
);

alter table public.orders
    owner to postgres;

create table if not exists public.order_positions
(
    id            bigint not null generated by default as identity
        constraint order_positions_pk
            primary key,
    order_id      bigint
        constraint order_positions_orders_id_fk
            references public.orders,
    menu_item_id  bigint
        constraint order_positions_menu_items_id_fk
            references public.menu_items,
    quantity      integer,
    quantity_done integer
);

alter table public.order_positions
    owner to postgres;