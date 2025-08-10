create table users
(
    id       integer,
    email    varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    created  timestamp not null,

    constraint PK_USERS primary key (id)
);

create unique index UQ_USERS_USERNAME on users (username);
create unique index UQ_USERS_EMAIL on users (email);

create table categories
(
    id       integer,
    name     varchar(255) not null,
    slug     varchar(255) not null,

    constraint PK_CATEGORIES primary key (id)
);

create unique index UQ_CATEGORIES_SLUG on categories (slug);

create table ads
(
    id          integer,
    title       varchar(255) not null,
    description text null,

    price       decimal(10, 2) null,
    www         varchar(255) null,
    contact     varchar(255) null,

    user_id     integer not null,
    category_id integer not null,
    created     timestamp not null,


    constraint PK_ADS primary key (id),
    constraint FK_ADS_ON_USER foreign key (user_id) references users (id),
    constraint FK_ADS_ON_CATEGORY foreign key (category_id) references categories (id)
);

create index IX_ADS_CATEGORY on ads (category_id);
