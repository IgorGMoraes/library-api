create table library_user (
    id serial primary key,
    login text not null unique,
    password text not null,
    role text not null
)