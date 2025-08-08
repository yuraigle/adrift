create table users
(
    id       integer primary key,
    name     varchar(255),
    email    varchar(255),
    password varchar(255),
    created  timestamp
);

create unique index UQ_USERS_EMAIL on users (email);
