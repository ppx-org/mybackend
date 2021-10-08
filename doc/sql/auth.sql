

-- >>>>>>>>>>>>>>>>>> 权限
create table auth_user (
	user_id serial not null,
	username varchar(32) not null,
	password varchar(60) not null,
	enable boolean not null default true, 
	primary key(user_id),
	unique(username)
);
comment on table auth_user is '用户';

create table auth_role (
	role_id serial not null,
	role_name varchar(32) not null,
	primary key(role_id)
);
comment on table auth_role is '角色';

create table auth_user_role (
	user_id int not null,
	role_id int not null,
	primary key(user_id, role_id)
);
comment on table auth_user_role is '用户所属角色';

create table auth_res (
	res_id serial not null,
	res_name varchar(32) not null,
	res_parent_id int not null,
	res_type varchar(1) not null,
	menu_path varchar(64),
	primary key(res_id)
);
comment on table auth_res is '权限资源';
comment on column auth_res.res_type is '类型:目录、菜单、操作';

create table auth_uri (
	uri_id serial not null,
	uri_path varchar(64) not null,
	primary key(uri_id),
	unique(uri_path)
);
comment on table auth_uri is 'URI(path)';

create table auth_res_uri (
	res_id int not null,
	uri_id int not null,
	primary key(res_id, uri_id)
);
comment on table auth_res_uri is '资源拥有的URI';

create table auth_role_res (
	role_id int not null,
	res_id int not null,
	primary key(role_id, res_id)
);
comment on table auth_role_res is '角色拥有的资源';


-- >>>>>>>>>>>>>>>>>> 刷新缓存
create table auth_cache_jwt (
	user_id int not null,
	validate_version int not null,
	replace_version int not null,
	primary key(user_id)
);
comment on table auth_cache_jwt is 'jwt缓存,放redis';
comment on column auth_cache_jwt.validate_version is '修改密码、作废等，禁用该token';
comment on column auth_cache_jwt.replace_version is '修改用户名、角色等，需要换token';


create table auth_cache_version (
	auth_key varchar(32) not null,
	auth_version int not null,
	primary key(auth_key)
);
comment on table auth_cache_version is '权限版本,放redis';
comment on column auth_cache_version.auth_version is '修改权限时，需要刷新内存';


