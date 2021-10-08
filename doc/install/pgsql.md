

# centos7.9 + postgresql14

* https://www.postgresql.org/docs/14/index.html
* Packages and Installers https://www.postgresql.org/download/


### Install the repository RPM

* Install the repository RPM
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm

* Install PostgreSQL
sudo yum install -y postgresql13-server


###  Optionally initialize the database and enable automatic start
```
/usr/pgsql-14/bin/postgresql-14-setup initdb
sudo chkconfig postgresql-14 on 
sudo service postgresql-14 start

# 配置监听和访问控制
echo "listen_addresses = '*'" >> /var/lib/pgsql/14/data/postgresql.conf 
echo "host all all all md5" >> /var/lib/pgsql/14/data/pg_hba.conf
(local all all peer 改local all all md5)
sudo service postgresql-14 restart

# 重新设置postgres管理员密码
su - postgres psql -U postgres
alter user postgres with password '#postgres14#';

# 创建数据库见test.sql

```










