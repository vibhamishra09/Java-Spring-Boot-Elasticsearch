Demo Project - Elasticsearch with Spring Boot
Prerequisites

Docker (for running Elasticsearch)

Java 11 or later

Maven (for building the Spring Boot application)

Git (for version control)

1. Launch Elasticsearch (using Docker)

To run Elasticsearch in a Docker container, we can use docker-compose. Follow these steps:

Clone this repository if you haven’t already:

git clone [https://github.com/your-username/demo-project.git](https://github.com/vibhamishra09/Java-Spring-Boot-Elasticsearch.git)
cd demo


open docker-compose.yml file in the project root under elastic-start-local folder




Run docker-compose in docker desktop to start Elasticsearch:

docker-compose up -d


This will pull the Elasticsearch Docker image and start the container. Elasticsearch will be accessible at http://localhost:9200.

Verify Elasticsearch is running by visiting http://localhost:9200 in your browser or by using the following curl command:

curl http://localhost:9200


You should see a JSON response with details about the Elasticsearch instance.

2. Build and Run the Spring Boot Application

Clone the repository and navigate to the project directory if you haven't already:

git clone [https://github.com/your-username/demo-project.git](https://github.com/vibhamishra09/Java-Spring-Boot-Elasticsearch.git)
cd demo


Build the Spring Boot application using Maven:
On macOS (using Homebrew):

brew install maven


On Ubuntu/Linux:

sudo apt update
sudo apt install maven(if maven not installed)

mvn clean install


Run the Spring Boot application:

mvn spring-boot:run


The application will start running on http://localhost:8080.

3. Populate the Index with Sample Data

The application uses a sample dataset to populate Elasticsearch. You can use the provided endpoints or manually index data by sending POST requests.

Populate the product_index with sample data:

You can use curl or Postman to manually populate the Elasticsearch index with some sample courses:

curl -X POST "http://localhost:8080/api/products" -H "Content-Type: application/json" -d '{
  "id": "1",
  "title": "Introduction to Mathematics",
  "description": "A beginner\'s course to understand the basics of mathematics, including algebra, geometry, and number theory.",
  "category": "Math",
  "type": "COURSE",
  "gradeRange": "1st–3rd",
  "minAge": 6,
  "maxAge": 9,
  "price": 45.99,
  "nextSessionDate": "2025-06-01T10:00:00Z"
}'


Repeat the same process for other sample data, such as:

Science Fundamentals

Art and Creativity

These will be indexed into Elasticsearch for search operations.

4. Call Endpoints with Example Requests
Search Products

You can search for products using the GET /api/search endpoint with various filters.
Index Searching in Elasticsearch

To search the indexed data in Elasticsearch, use the following curl command:

curl -X GET "http://localhost:9200/product_index/_search?pretty=true&q=*&size=10"


This will return the top 10 documents from the product_index. You can adjust the query or size parameter as needed for specific search results.
Example: Search for courses in the 'Math' category

curl -X GET "http://localhost:8080/api/search?q=category:Math"


Example: Search for courses with price greater than or equal to $40 and within 'Math' category

curl -X GET "http://localhost:8080/api/search?q=category:Math&minPrice=40"


Example: Search with pagination and sorting by price (ascending)

curl -X GET "http://localhost:8080/api/search?q=category:Math&minPrice=40&sort=priceAsc&page=0&size=2"

Endpoints Overview

GET /api/search

Query Parameters:

q: Search query (supports full-text search and filters like category, price, type, etc.)

minPrice: Minimum price filter

maxPrice: Maximum price filter

minAge: Minimum age filter

maxAge: Maximum age filter

startDate: Start date filter (in ISO-8601 format)

sort: Sorting options (upcoming, priceAsc, priceDesc)

page: Page number for pagination (default: 0)

size: Number of results per page (default: 10)

POST /api/products

Request Body: JSON payload to add a new product to the index.
For testing testcontainer in docker
run  
mvn -X test   
It will generate testcontainer

Troubleshooting

Elasticsearch not starting: Ensure Docker is running and the container is correctly launched. You can use docker-compose logs to view logs for troubleshooting.

Application not starting: If the application fails to start, check for issues in the application.properties file or dependencies using mvn clean install to re-build the project.

Cleanup

To stop the Elasticsearch container:

docker-compose down


This will stop and remove the container.

That's it! You've successfully set up your project with Elasticsearch, indexed sample data, and tested search functionality.
