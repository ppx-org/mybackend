
-- planb用户

-- 删除
DROP TABLE IF EXISTS base_dict;
DROP TABLE IF EXISTS test_example;
DROP TABLE IF EXISTS test_example_sub;
DROP TABLE IF EXISTS mob_example;


create table base_dict (
	dict_val varchar(3) not null,
	dict_type varchar(32) not null,
	dict_name varchar(32) not null,
	dict_enable boolean not null default true,
	primary key(dict_val, dict_type)
);
comment on table base_dict is '业务数据字典';

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



-- 
INSERT INTO "public"."base_dict"("dict_val", "dict_type", "dict_name", "dict_enable") VALUES ('0', 'base-sex', '女', 't');
INSERT INTO "public"."base_dict"("dict_val", "dict_type", "dict_name", "dict_enable") VALUES ('1', 'base-sex', '男', 't');
INSERT INTO "public"."base_dict"("dict_val", "dict_type", "dict_name", "dict_enable") VALUES ('2', 'base-sex', '中性', 'f');

INSERT INTO "public"."base_dict"("dict_val", "dict_type", "dict_name", "dict_enable") VALUES ('0', 'base-text', '测试值0', 't');
INSERT INTO "public"."base_dict"("dict_val", "dict_type", "dict_name", "dict_enable") VALUES ('1', 'base-text', '测试值1', 't');



