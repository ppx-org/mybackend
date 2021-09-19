
-- 管理员名称和数据库postgres

create user planb with password '#postgres13.4#';
create database planb owner planb;
grant all on database planb to planb;


-- planb用户
create table test_example (
    example_id serial not null,
    example_name varchar(16) not null,
    example_type char(1),
    example_date date,
    example_time timestamp default now() not null,
    primary key(example_id)
);
comment on table test_example is 'test_example';
comment on column test_example.example_type is '类型';

create table test_example_sub (
    example_id int not null,
    sub_name varchar(16) not null,
    primary key(example_id)
);
comment on table test_example_sub is 'test_example_sub';

