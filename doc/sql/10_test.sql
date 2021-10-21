
-- planb用户

-- 删除
DROP TABLE IF EXISTS test_example;
DROP TABLE IF EXISTS test_example_sub;

create table test_example (
    example_id serial not null,
    example_name varchar(16) not null,
    example_type char(1),
    example_date date,
    example_time timestamp default now() not null,
    primary key(example_id),
    unique(example_name)
);
comment on table test_example is 'test_example';
comment on column test_example.example_type is '类型';

create table test_example_sub (
    example_id int not null,
    sub_name varchar(16) not null,
    primary key(example_id)
);
comment on table test_example_sub is 'test_example_sub';


-- >>> mobile
create table mob_example (
	example_id serial not null,
    example_title varchar(16) not null,
    example_price money not null,
    example_main_img varchar(128) not null, 
	primary key(example_id),
    unique(example_title)
)





