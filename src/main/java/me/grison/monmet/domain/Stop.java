package me.grison.monmet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a bus stop.
 */
@Data
@XmlRootElement
@AllArgsConstructor
public class Stop {
    private String line;
    private String head;
    private String stopId;
    private String stopName;
}
