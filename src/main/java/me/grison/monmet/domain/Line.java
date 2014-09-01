package me.grison.monmet.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a bus line.
 */
@Data
public class Line {
    String name;
    Set<LineHeadStops> headStops = new HashSet<LineHeadStops>();
}
