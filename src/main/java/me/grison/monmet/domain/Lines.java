package me.grison.monmet.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Lines {
    Map<String, List<BusLine>> lines;
}
