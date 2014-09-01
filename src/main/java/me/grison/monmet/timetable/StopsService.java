package me.grison.monmet.timetable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.grison.monmet.domain.BusLine;
import me.grison.monmet.domain.BusStop;
import me.grison.monmet.domain.Line;
import me.grison.monmet.repository.AppRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
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
    static final Logger LOG = LoggerFactory.getLogger(StopsService.class);

    public Map<String, List<BusLine>> getLines() {
        LOG.info("Getting lines.");
        return repository.getLines();
    }

    public List<String> getHeads(String lineId) {
        LOG.info("Getting heads for line `" + lineId + "`.");
        return repository.getHeads(lineId);
    }

    public List<BusStop> getStops(String lineId, String head) {
        LOG.info("Getting stops for line `" + lineId + "` and head `" + head + "`.");
        if (repository.hasStops(lineId, head)) {
            LOG.info("Got it in cache.");
            return repository.getStops(lineId, head);
        } else {
            String url = String.format("http://lemet.fr/src/inc/LEMET_Horaires_print_form.class.php?action=arrets&head=%s|%s",
                    head, lineId);
            LOG.info("Not in cache, fetching it from `" + url + "`.");
            // fetch stops
            InputStream in = null;
            try {
                in = new URL(url).openStream();
                final String json = IOUtils.toString(in);
                LOG.debug(json);
                Map<Integer, String> stops = new Gson().fromJson(json,
                        new TypeToken<Map<Integer, String>>(){}.getType());
                LOG.info("Saving in cache.");
                return repository.saveStops(lineId, head, stops);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }

    public Set<Line> getAllLines() {
        return repository.getAllLines();
    }

}
