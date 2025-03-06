# movie-test
Test task for java developers

## Development

Install redis for the development

`docker run --name redis -d -p 6379:6379 redis`

Install mysql for the development

`docker run --name movie-mysql -e MYSQL_ROOT_PASSWORD=secret -d -p 23306:3306 mysql:latest`

## Deploy