

-- 管理员名称和数据库postgres

create user planb with password '#postgres14#';
create database planb owner planb;
grant all on database planb to planb;

