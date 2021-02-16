drop database expensetrackerdb;
drop user expensetracker;

create user expensetracker with password 'password';
create database expensetrackerdb with template=template0 owner=expensetracker;
\connect expensetrackerdb;

alter default privileges grant all on tables to expensetracker;
alter default privileges grant all on sequences to expensetracker;

create table users_tbl(
user_id integer primary key not null,
first_name varchar(20) not null,
last_name varchar(20) not null,
email varchar(50) not null,
password text not null
);

create table categories_tbl(
category_id integer primary key not null,
user_id integer not null,
title varchar(20) not null,
description varchar(30) not null
);

alter table categories_tbl add constraint cats_users_fk
foreign key (user_id) references users_tbl(user_id);

create table transactions_tbl(
transaction_id integer primary key not null,
category_id integer not null,
user_id integer not null,
amount numeric(10, 2) not null,
note varchar(50) not null,
transaction_date bigint not null
);

alter table transactions_tbl add constraint trans_users_fk
foreign key (user_id) references users_tbl(user_id);

alter table transactions_tbl add constraint trans_cats_fk
foreign key (category_id) references categories_tbl(category_id);

create sequence users_seq increment 1 start 1;
create sequence categories_seq increment 1 start 1;
create sequence transactions_seq increment 1 start 1;


