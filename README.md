terraform init
terraform validate

docker build --build-arg JAR_FILE=build/libs/\*.jar -t dis/master .
docker run -p 8090:8090 dis/master:latest
docker network create kafka_network
