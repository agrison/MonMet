package me.grison.monmet.timetable;

import me.grison.monmet.domain.Line;
import me.grison.monmet.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Service
public class StopsService {
    @Autowired
    AppRepository repository;

    public Set<Line> getAllLines() {
        return repository.getAllLines();
    }

}
