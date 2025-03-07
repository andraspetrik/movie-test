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

To deploy the application, run command in the root of the project:

`docker-compose up`

## Testing the API

Please find the [Basic testing.postman_collection.json](Basic%20testing.postman_collection.json) in the project. 
Import it to your Postman workspace, and run the items of the collection. 


