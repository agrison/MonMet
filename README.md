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

## API

Get the available lines:

`GET /api/lines`


Get the available heads (directions) for a specific line:

`GET /api/lines/{lineId}`


Get the available stops for a specific line and head:

`GET /api/lines/{lineId}/{head}`


Get the timetable for a specific line, head and stop:

`GET /api/tt/{lineId}/{head}/{stopId}?stopName={stopName}`

