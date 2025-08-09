create table users
(
    id       integer primary key,
    username varchar(255),
    password varchar(255),
    created  timestamp
);

create unique index UQ_USERS_USERNAME on users (username);

