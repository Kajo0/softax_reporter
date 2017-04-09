# softax_reporter

## Source build
```
user@~/webapp$ ./gradlew build
```

# Docker

## Images build
```
user@~/activemq$ docker build -t activemq .
user@~/app$ docker build --no-cache -t webapp .
```

## Containers run standalone
```
user@~/$ docker run -itd -p 8161:8161 -p 61616:61616 --name activemq1 activemq
user@~/$ docker run -itd -p 8081:8080 --link activemq1:amq --name webapp1 webapp
```

### Or via `docker-compose`
```
docker-compose up -d
```

# App available
[http://localhost:8081](http://localhost:8081)

# ActiveMQ console
[http://localhost:8161](http://localhost:8161)

user: admin

pass: admin
