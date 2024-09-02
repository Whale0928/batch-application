FROM mysql:8.0.30
COPY ./db/conf.d /etc/mysql/conf.d
COPY ./db/init/schema-mysql.sql /docker-entrypoint-initdb.d
COPY ./db/init/schema-beta-table.sql /docker-entrypoint-initdb.d

RUN chmod 644 /etc/mysql/conf.d/my.cnf
