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

Init redis

```
cat src/main/resources/init.redis | redis-cli --pipe
```

Run the backend
```
mvn package
nohup java -jar target/MonMet*.jar &
```


## API

| Method  | Path                                                 | Description                                                |
|:-------:| ---------------------------------------------------- | -----------------------------------------------------------|
|   GET   | /api/lines                                           | Get the available lines bus lines.                         |
|   GET   | /api/lines/{lineId}                                  | Get the available heads (directions) for a specific line   |
|   GET   | /api/lines/{lineId}/{head}                           | Get the available stops for a specific line and head       |
|   GET   | /api/tt/{lineId}/{head}/{stopId}?stopName={stopName} | Get the timetable for a specific line, head and stop       |


## JSON

### GET `/api/lines`

```json
{
    "Mettis": [
        {
            "id": "998",
            "name": "A",
        },
        {
            "id": "999",
            "name": "B",
        }
    ],
    ...
}
```

### GET `/api/lines/{lineId}`



### GET `/api/lines/{lineId}/{head}`



### GET `/api/tt/{lineId}/{head}/{stopId}?stopName={stopName}`

