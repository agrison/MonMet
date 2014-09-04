package me.grison.monmet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@AllArgsConstructor
public class LatLon {
    double lat;
    double lon;
}
