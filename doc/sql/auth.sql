

create table auth_user (
	user_id serial not null,
	user_name varchar(32) not null,
	user_password varchar(32) not null
)
comment on table auth_user is 'auth_user';

create table auth_role (
	role_id serial not null,
	role_name varchar(32) not null
)
comment on table auth_role is 'auth_role';

create table auth_user_in_role (

)


create table auth_res (
	res_id serial not null,
	res_name varchar(32) not null,
	res_type varchar(1) not null,
	res_menu_uri_id int,
)
comment on table auth_res is 'auth_res';
comment on column auth_role.res_type is '类型 目录、菜单、操作';

create table auth_uri (
	uri_id serial not null,
	uri varchar(64) not null
)
comment on table auth_uri is 'auth_uri';

create table auth_res_uri (
	
)



