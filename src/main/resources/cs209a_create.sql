drop table if exists public.question_tags;
drop table if exists public.answers;
drop table if exists public.tags;
drop table if exists public.questions;
drop table if exists public.comments;

create table if not exists tags
(
    name varchar(30)  primary key
);

create table if not exists questions
(
    id           integer primary key,
    title        text    not null,
    body         text    not null,
    score        integer not null,
    view_count   integer not null,
    answer_count integer not null
);

create table if not exists question_tags
(
    tag_name      varchar(30) not null references tags (name),
    question_id integer not null references questions (id),
    primary key (tag_name, question_id)
);

create table if not exists answers
(
    id          integer primary key,
    question_id integer not null references questions (id),
    is_accepted boolean not null,
    body        text    not null,
    score       integer not null
);

create table if not exists comments
(
    id    integer primary key,
    post_id integer not null ,
    body  text    not null,
    score integer not null
);


