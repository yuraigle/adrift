create table questions
(
    id       bigserial primary key,
    name     varchar(255) not null,
    type     varchar(10) not null,
    required boolean,
    min      integer,
    max      integer,
    regex    varchar(255),
    message  varchar(255)
);

create table options
(
    id          bigserial primary key,
    question_id bigint not null,
    name        varchar(255) not null,
    constraint FK_OPTIONS_ON_QUESTION foreign key (question_id)
        references questions (id) on delete cascade
);

create index IX_OPTIONS_QUESTION on options (question_id);

create table templates
(
    id   bigserial primary key,
    name varchar(255) not null
);

create table templates_questions
(
    template_id bigint not null,
    question_id bigint not null,
    ord         integer,
    constraint PK_TEMPLATES_QUESTIONS primary key (template_id, question_id),
    constraint FK_TEMPLATES_QUESTIONS_ON_TEMPLATE foreign key (template_id)
        references templates (id) on delete cascade,
    constraint FK_TEMPLATES_QUESTIONS_ON_QUESTION foreign key (question_id)
        references questions (id) on delete cascade
);

create index IX_TEMPLATES_QUESTIONS_TEMPLATE on templates_questions (template_id);

create table categories
(
    id          bigserial primary key,
    name        varchar(255) not null,
    slug        varchar(255) not null,
    template_id bigint,
    parent_id   bigint,
    constraint FK_CATEGORIES_ON_PARENT foreign key (parent_id)
        references categories (id),
    constraint FK_CATEGORIES_ON_TEMPLATE foreign key (template_id)
        references templates (id)
);

create unique index UQ_CATEGORIES_SLUG on categories (slug);

create table users
(
    id       bigserial primary key,
    email    varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    created  timestamp not null default now()
);

create unique index UQ_USERS_USERNAME on users (username);
create unique index UQ_USERS_EMAIL on users (email);

create table ads
(
    id          bigserial primary key,
    title       varchar(255) not null,
    description text,
    price       numeric(10, 2),
    created     timestamp not null default now(),
    updated     timestamp,
    user_id     bigint,
    category_id bigint,
    constraint FK_ADS_ON_USER foreign key (user_id)
        references users (id) on delete set null,
    constraint FK_ADS_ON_CATEGORY foreign key (category_id)
        references categories (id) on delete set null
);

create index IX_ADS_CATEGORY on ads (category_id);

create table ads_fields
(
    ad_id       bigint not null,
    question_id bigint not null,
    val_number  integer,
    val_decimal numeric(10, 2),
    val_text    varchar(255),
    constraint PK_ADS_FIELDS primary key (ad_id, question_id),
    constraint FK_ADS_FIELDS_ON_AD foreign key (ad_id)
        references ads (id) on delete cascade,
    constraint FK_ADS_FIELDS_ON_QUESTION foreign key (question_id)
        references questions (id) on delete cascade
);

create index IX_ADS_FIELDS_ON_AD on ads_fields (ad_id);

create table ads_options
(
    ad_id       bigint not null,
    question_id bigint not null,
    option_id   bigint not null,
    constraint PK_ADS_OPTIONS primary key (ad_id, question_id, option_id),
    constraint FK_ADS_OPTIONS_ON_AD foreign key (ad_id)
        references ads (id) on delete cascade,
    constraint FK_ADS_OPTIONS_ON_OPTION foreign key (option_id)
        references options (id) on delete cascade,
    constraint FK_ADS_OPTIONS_ON_QUESTION foreign key (question_id)
        references questions (id) on delete cascade
);

create index IX_ADS_OPTIONS_ON_AD on ads_options (ad_id);
create index IX_ADS_OPTIONS_ON_OPTION on ads_options (option_id);

create table ads_images
(
    id bigint not null,
    ad_id bigint not null,
    filename varchar(32) not null,
    ord integer null,
    orig_filename varchar(255) null,
    alt varchar(255) null,
    constraint PK_ADS_IMAGES primary key (id),
    constraint FK_ADS_IMAGES_ON_AD foreign key (ad_id)
        references ads (id) on delete cascade
);

create index IX_ADS_IMAGES_AD on ads_images (ad_id);
create index UQ_ADS_IMAGES_FILENAME on ads_images (filename);
