create table categories
(
    id          integer not null,
    name        varchar(255) not null,
    slug        varchar(255) not null,
    template_id integer null,

    constraint PK_CATEGORIES primary key (id)
);

create unique index UQ_CATEGORIES_SLUG on categories (slug);

create table questions
(
    id   integer not null,
    name varchar(255) not null,
    type varchar(255) not null,

    constraint PK_QUESTIONS primary key (id)
);

create table templates
(
    id   integer not null,
    name varchar(255) not null,
    constraint PK_TEMPLATES primary key (id)
);

CREATE TABLE template_questions
(
    template_id integer not null,
    question_id integer not null,
    ord         integer null,
    constraint PK_TEMPLATE_QUESTIONS primary key (template_id, question_id),
    constraint FK_TEMPLATE_QUESTIONS_ON_TEMPLATE foreign key (template_id) references templates (id) on delete cascade,
    constraint FK_TEMPLATE_QUESTIONS_ON_QUESTION foreign key (question_id) references questions (id) on delete cascade
);

create index IX_TEMPLATE_QUESTIONS_ON_TEMPLATE on template_questions (template_id);

create table users
(
    id       integer not null,
    email    varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    created  timestamp not null,

    constraint PK_USERS primary key (id)
);

create unique index UQ_USERS_USERNAME on users (username);
create unique index UQ_USERS_EMAIL on users (email);

create table ads
(
    id          integer not null,
    title       varchar(255) not null,
    description text null,
    price       decimal(10, 2) null,
    user_id     integer not null,
    category_id integer not null,
    created     timestamp not null,


    constraint PK_ADS primary key (id),
    constraint FK_ADS_ON_USER foreign key (user_id) references users (id) on delete set null,
    constraint FK_ADS_ON_CATEGORY foreign key (category_id) references categories (id) on delete set null
);

create index IX_ADS_CATEGORY on ads (category_id);
