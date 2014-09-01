MonMet
======

## Dependencies

* Java 7
* Maven  3+
* Redis

## Running the Backend

Run redis
```
redis-server
```

Run the backend
```
mvn package
nohup java -jar target/MonMet*.jar &
```

