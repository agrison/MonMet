package me.grison.monmet.timetable;

import me.grison.monmet.domain.BusLine;
import me.grison.monmet.domain.BusStop;
import me.grison.monmet.domain.Line;
import me.grison.monmet.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stop service.
 */
@Service
public class StopsService {
    @Autowired
    AppRepository repository;

    public Map<String, List<BusLine>> getLines() {
        return repository.getLines();
    }

    public List<String> getHeads(String lineId) {
        return repository.getHeads(lineId);
    }

    public List<BusStop> getStops(String lineId, String head) {
        return repository.getStops(lineId, head);
    }

    public Set<Line> getAllLines() {
        return repository.getAllLines();
    }

}
