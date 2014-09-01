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
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, List<BusLine>> allLines = new HashMap<String, List<BusLine>>();
            for (String line : jedis.lrange(StorageKey.TYPES, 0, 100)) {
                List<BusLine> currentLines = new ArrayList<BusLine>();
                for (Map.Entry<String, String> e : jedis.hgetAll(StorageKey.line(line)).entrySet()) {
                    currentLines.add(
                            new BusLine(e.getKey(), e.getValue())
                    );
                }
                allLines.put(line, currentLines);
            }
            return allLines;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * Get the heads for a specific line id.
     *
     * @param lineId the line id.
     * @return the heads for that line id.
     */
    public List<String> getHeads(String lineId) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lrange(StorageKey.heads(lineId), 0, 100);
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            List<BusStop> stops = new ArrayList<BusStop>();
            for (Tuple t: jedis.zrangeByScoreWithScores(StorageKey.stops(lineId, head), 0, Double.MAX_VALUE)) {
                stops.add(new BusStop(t.getScore(), t.getElement()));
            }
            return stops;
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            final String key = StorageKey.stops(lineId, head);
            return jedis.exists(key) && jedis.zcard(key) > 0;
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            List<BusStop> busStops = new ArrayList<BusStop>();
            final String key = StorageKey.stops(lineId, head);
            for (Map.Entry<Integer, String> e: stops.entrySet()) {
                jedis.zadd(key, e.getKey(), e.getValue());
                busStops.add(new BusStop(e.getKey(), e.getValue()));
            }
            return busStops;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * Find all lines, their heads and stops. Find everything.
     *
     * @return all the lines in store.
     */
    public Set<Line> getAllLines() {
        Set<Line> lines = new HashSet<Line>();

        Jedis jedis = jedisPool.getResource();
        try {
            // iterate on lines
            for (String l : jedis.lrange("monmet:lines", 0, 1000)) {
                Line line = new Line();
                // get name
                line.setName(jedis.hget("monmet:lines:" + l, "name"));

                // iterate on heads
                for (String h : jedis.lrange("monmet:lines:" + l + ":heads", 0, 10)) {
                    // get all stops
                    LineHeadStops headStops = new LineHeadStops();
                    headStops.setHead(h);
                    for (Tuple tuple : jedis.zrangeByScoreWithScores("monmet:lines:" + l + ":stops:" + h.replaceAll("\\s+", "_"), 0, Double.MAX_VALUE)) {
                        headStops.getStops().add(new Stop(l, h, String.valueOf(Double.valueOf(tuple.getScore()).intValue()), tuple.getElement()));
                    }

                    line.getHeadStops().add(headStops);
                }

                lines.add(line);
            }
        } finally {
            jedisPool.returnResource(jedis);
        }

        return lines;
    }

    /**
     * Check if we have the timetable for a specific stop in our store.
     *
     * @param stop the stop.
     * @return whether we have the timetable for that stop.
     */
    public boolean hasTimeTableForStop(Stop stop) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists("monmet:tt:" + stop.getHead() + ":" + stop.getStopId());
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, String> info = jedis.hgetAll("monmet:tt:" + stop.getHead() + ":" + stop.getStopId());
            TimeTable timeTable = new TimeTable();
            timeTable.setWeek(split(info.get("week")));
            timeTable.setSaturday(split(info.get("saturday")));
            timeTable.setSunday(split(info.get("sunday")));
            return timeTable;
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hmset(
                    "monmet:tt:" + stop.getHead() + ":" + stop.getStopId(),
                    new HashMap<String, String>() {{
                        put("week", join(timeTable.getWeek()));
                        put("saturday", join(timeTable.getSaturday()));
                        put("sunday", join(timeTable.getSunday()));
                    }}
            );
        } finally {
            jedisPool.returnResource(jedis);
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
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.incr(StorageKey.hits());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
