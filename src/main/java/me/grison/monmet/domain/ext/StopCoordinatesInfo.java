package me.grison.monmet.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class StopCoordinatesInfo {
    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public class StopInfo {
        String name;
        String lignes;
        List<Double> latlon;
        // ignore what we don't need
    }
    Integer total;
    List<StopInfo> stops;
}
