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


## Sample JSON

#### GET `/api/lines`

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

#### GET `/api/lines/{lineId}`

```json
[
    "CITE U",
    "MATERNITE",
    ...
]
```


#### GET `/api/lines/{lineId}/{head}`

```json
[
    {
        "id": "21327",
        "name": "REPUBLIQUE",
    },
    {
        "id": "31329",
        "name": "CITE U",
    },
    ...
]
```


### GET `/api/tt/{lineId}/{head}/{stopId}?stopName={stopName}(&timestamp={timestamp})`

```json
{
    "week": [
        "05:18",
        "05:36",
        "06:10",
        "06:28",
        "06:43",
        ...
    ],
    "saturday": [
        "05:18",
        "05:36",
        "06:06",
        "06:36",
        ...
    ],
    "sunday": [
        "05:18",
        "06:36",
        "07:12",
        "08:05",
        "08:35",
        ...
    ],
    "nextRides": [
        "19:06",
        "19:16",
        "19:26"
     ]
}
```
