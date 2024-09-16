create table book (
    id serial primary key,
    title text not null ,
    author text not null,
    genre text not null,
    price decimal,
    rating decimal,
    number_of_rates int
)