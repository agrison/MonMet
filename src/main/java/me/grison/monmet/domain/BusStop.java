package me.grison.monmet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@AllArgsConstructor
public class BusStop {
    String id;
    String name;

    public BusStop(double score, String value) {
        this.id = String.valueOf(Double.valueOf(score).intValue());
        this.name = value;
    }
}
