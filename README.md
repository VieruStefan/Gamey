# To run the app
* `docker compose build --no-cache`
* `docker compose up`
* `docker compose down`

The kafka server is open for Docker containers on `kafka:9092`.\
The eureka server is open on `8761` and `8762`.\
The grafana is open on `3010`.\
The prometheus is open on `9090`.

The used topics are:
* `api.site-discovery` - queue an entire page to be discovered
* `api.requests` - queue API call for a single page
* `api.responses` - the output with the result of the API call
* `gamedata.products` - the output with the result of all products on the page

## To run frontend
### Dev:
```
sudo docker build -t next-app-dev \
 --build-arg NEXT_PUBLIC_GATEWAY_URL=http://localhost:8888 \
 -f ./frontend/dev.Dockerfile ./frontend
```
```
sudo docker run -d \
    --name next-app-dev \
    -p 3000:3000 \
    -e NEXT_PUBLIC_GATEWAY_URL=http://localhost:8888 \
    --network app-network \
    --restart always 
    next-app-dev
```

### Production:
```
sudo docker build -t next-app \
    --build-arg NEXT_PUBLIC_GATEWAY_URL=http://34.30.119.122:8888 \
    -f ./frontend/prod.Dockerfile ./frontend
```
```
sudo docker run -d \
    --name next-app \
    -p 3000:3000 \
    -e NEXT_PUBLIC_GATEWAY_URL=http://34.30.119.122:8888 \
    --network app-network \
    --restart always 
    next-app
```