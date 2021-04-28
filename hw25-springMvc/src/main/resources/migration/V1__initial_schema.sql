create table users
(
    id bigserial not null primary key,
    name     varchar(100),
    login    varchar(100),
    password varchar(100)
)