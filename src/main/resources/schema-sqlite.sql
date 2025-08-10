create table users
(
    id       integer primary key,
    email    varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    created  timestamp not null
);

create unique index UQ_USERS_USERNAME on users (username);
create unique index UQ_USERS_EMAIL on users (email);
