package me.grison.monmet.repository;

import me.grison.monmet.domain.Stop;

public class StorageKey {

    public static String TYPES = "monmet:types";

    public static String line(String l) {
        return "monmet:" + l;
    }

    public static String heads(String lineId) {
        return String.format("monmet:line:%s:heads", lineId);
    }

    public static String stops(String lineId, String head) {
        return String.format("monmet:line:%s:%s", lineId, head);
    }

    public static String hits() {
        return "monmet:api:hits";
    }

    public static String coordinates(String line, String stopName) {
        return String.format("monmet:line:%s:stop:%s:coordinates", line, stopName);
    }

    public static String timeTable(Stop stop) {
        return String.format("monmet:tt:%s:%s", stop.getHead(), stop.getStopId());
    }

    public static String timeTableUpdate(Stop stop) {
        return timeTable(stop) + ":update";
    }
}
