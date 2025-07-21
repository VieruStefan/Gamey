# To run the app
* `docker compose build --no-cache`
* `docker compose up`
* `docker compose down`

The kafka server is open for Docker containers on `kafka:9093`. 

The used topics are:
* `web-scraping-list` - queue API call for an entire page
* `web-scraping-product` - queue API call for a single page
* `web-scraping-list-output` - the output queue with the result of the API
* `web-scraping-product-output` - the output queue with the result of the API

