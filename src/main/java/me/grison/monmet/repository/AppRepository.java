package me.grison.monmet.repository;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.grison.monmet.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * This is the application repository.
 */
@Repository
public class AppRepository {
    @Autowired
    JedisPool jedisPool;
    final Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
    final Joiner joiner = Joiner.on(",").skipNulls();

    /**
     * Get all the lines.
     *
     * @return all the lines.
     */
    public Map<String, List<BusLine>> getLines() {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, List<BusLine>> allLines = new HashMap<>();
            for (String line : jedis.lrange(StorageKey.TYPES, 0, 100)) {
                List<BusLine> currentLines = new ArrayList<>();
                for (Map.Entry<String, String> e : jedis.hgetAll(StorageKey.line(line)).entrySet()) {
                    currentLines.add(
                            new BusLine(e.getKey(), e.getValue())
                    );
                }
                allLines.put(line, currentLines);
            }
            return allLines;
        }
    }

    /**
     * Get the heads for a specific line id.
     *
     * @param lineId the line id.
     * @return the heads for that line id.
     */
    public List<String> getHeads(String lineId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(StorageKey.heads(lineId), 0, 100);
        }
    }

    /**
     * Get the stops for a specific line id and head.
     *
     * @param lineId the line id.
     * @param head the head.
     * @return the stops.
     */
    public List<BusStop> getStops(String lineId, String head) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<BusStop> stops = new ArrayList<>();
            for (Tuple t: jedis.zrangeByScoreWithScores(StorageKey.stops(lineId, head), 0, Double.MAX_VALUE)) {
                stops.add(new BusStop(t.getScore(), t.getElement()));
            }
            return stops;
        }
    }

    /**
     * Check whether we have some stops in our store for the given line id and head.
     *
     * @param lineId the line id.
     * @param head the head.
     * @return whether we have stops or not.
     */
    public boolean hasStops(String lineId, String head) {
        try (Jedis jedis = jedisPool.getResource()) {
            final String key = StorageKey.stops(lineId, head);
            return jedis.exists(key) && jedis.zcard(key) > 0;
        }
    }

    /**
     * Save the stops for the given line id and head.
     *
     * @param lineId the line id.
     * @param head the head.
     * @param stops the stops.
     * @return the stops.
     */
    public List<BusStop> saveStops(String lineId, String head, Map<Integer, String> stops) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<BusStop> busStops = new ArrayList<>();
            final String key = StorageKey.stops(lineId, head);
            for (Map.Entry<Integer, String> e: stops.entrySet()) {
                jedis.zadd(key, e.getKey(), e.getValue());
                busStops.add(new BusStop(e.getKey(), e.getValue()));
            }
            return busStops;
        }
    }

    /**
     * Check if we have the timetable for a specific stop in our store.
     *
     * @param stop the stop.
     * @return whether we have the timetable for that stop.
     */
    public boolean hasTimeTableForStop(Stop stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(StorageKey.timeTable(stop));
        }
    }

    /**
     * Get the number of days since the last timetable update.
     *
     * @param stop the stop.
     * @return the number of days.
     */
    public long daysSinceLastTimeTableUpdate(Stop stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(StorageKey.timeTableUpdate(stop))) {
                Long lastUpdate = Long.valueOf(jedis.get(StorageKey.timeTableUpdate(stop)));
                Calendar lastUpdateCal = Calendar.getInstance();
                lastUpdateCal.setTimeInMillis(lastUpdate);
                return (Calendar.getInstance().getTimeInMillis() - lastUpdate) / (24*60*60*1000);
            } else {
                return 0;
            }
        }
    }

    /**
     * Get the timetable for a specific stop.
     * If we don't have it we will fetch it then store it.
     *
     * @param stop the stop.
     * @return the timetable for that stop.
     */
    public TimeTable getTimeTableForStop(final Stop stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> info = jedis.hgetAll(StorageKey.timeTable(stop));
            TimeTable timeTable = new TimeTable();
            timeTable.setWeek(split(info.get("week")));
            timeTable.setSaturday(split(info.get("saturday")));
            timeTable.setSunday(split(info.get("sunday")));
            return timeTable;
        }
    }

    /**
     * Save the given timetable for a specific stop in our store.
     *
     * @param stop      the stop.
     * @param timeTable the timetable.
     * @return the timetable for a specific stop.
     */
    public TimeTable saveTimeTableForStop(final Stop stop, final TimeTable timeTable) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(
                    StorageKey.timeTable(stop),
                    new HashMap<String, String>() {{
                        put("week", join(timeTable.getWeek()));
                        put("saturday", join(timeTable.getSaturday()));
                        put("sunday", join(timeTable.getSunday()));
                    }}
            );
        }
        return timeTable;
    }

    /**
     * Read the name.
     */
    private List<String> split(String s) {
        return Lists.newArrayList(splitter.split(s));
    }

    /**
     * Read the name.
     */
    private String join(List<String> l) {
        return joiner.join(l);
    }

    public void incrementHits() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.incr(StorageKey.hits());
        }
    }

    public void saveCoordinates(String line, String stopName, List<Double> coordinates) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(StorageKey.coordinates(line, stopName),
                    String.format("%f;%f", coordinates.get(0), coordinates.get(1)).replace(",", ".").replace(";", ","));
        }
    }

    public LatLon getCoordinates(String line, String stopName) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!jedis.exists(StorageKey.coordinates(line, stopName))) {
                return null;
            }
            String coords[] = jedis.get(StorageKey.coordinates(line, stopName)).split(",");
            return new LatLon(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
        }
    }
}
