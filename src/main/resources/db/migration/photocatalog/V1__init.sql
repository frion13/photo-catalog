
 create extension if not exists "uuid-ossp";

 create table if not exists "photos"
 (
 id UUID unique not null default uuid_generate_v1() primary key,
 description varchar(255) not null,
 last_modify_date date not null,
 content bytea
 );