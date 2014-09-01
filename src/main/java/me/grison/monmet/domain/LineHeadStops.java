package me.grison.monmet.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class LineHeadStops {
    String head;
    Set<Stop> stops = new HashSet<Stop>();
}
