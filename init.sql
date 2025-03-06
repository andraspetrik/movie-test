CREATE DATABASE IF NOT EXISTS movie_test;

CREATE USER IF NOT EXISTS 'default_user'@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON movie_test.* TO 'default_user'@'%';

FLUSH PRIVILEGES;