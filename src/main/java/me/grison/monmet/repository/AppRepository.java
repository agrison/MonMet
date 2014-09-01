package me.grison.monmet.repository;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.grison.monmet.domain.Line;
import me.grison.monmet.domain.LineHeadStops;
import me.grison.monmet.domain.Stop;
import me.grison.monmet.domain.TimeTable;
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
     * Find all lines, their heads and stops. Find everything.
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
     * @param stop the stop.
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
}
