package me.grison.monmet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This is a bus stop.
 */
@Data
@AllArgsConstructor
public class Stop {
    private String line;
    private String head;
    private String stopId;
    private String stopName;
}
