package me.grison.monmet.repository;

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
}
