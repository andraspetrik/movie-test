# movie-test
Test task for java developers

## Development

Install redis for the development

`docker run --name redis -d -p 6379:6379 redis`

Install mysql for the development

`docker run --name movie-mysql -e MYSQL_ROOT_PASSWORD=secret -d -p 23306:3306 mysql:latest`

Install prometheus for development

`docker run --name prometheus  -v prometheus.yml:/etc/prometheus/prometheus.yml -d -p 9090:9090 prom/prometheus`

## Deploy

To deploy the application, run command:

`docker-compose up`