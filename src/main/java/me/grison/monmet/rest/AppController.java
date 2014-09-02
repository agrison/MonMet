package me.grison.monmet.rest;

import me.grison.monmet.domain.*;
import me.grison.monmet.timetable.StopsService;
import me.grison.monmet.timetable.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * This is the application controller.
 */
@RestController
public class AppController {
    @Autowired
    StopsService stopsService;
    @Autowired
    TimeTableService timeTableService;


    /**
     * Get all the bus lines.
     *
     * @return a map of line types associated with a list of the lines in that category.
     * Category can be "Mettis", "Lines", "Navettes", etc.
     */
    @RequestMapping(value = "/api/lines", method = RequestMethod.GET)
    public Map<String, List<BusLine>> getLines() {
        return stopsService.getLines();
    }

    /**
     * Get all the heads for a specific line id.
     *
     * @param lineId the line id.
     * @return the list of heads available for the given line id.
     */
    @RequestMapping(value = "/api/lines/{lineId}", method = RequestMethod.GET)
    public List<String> getHeads(@PathVariable("lineId") String lineId) {
        return stopsService.getHeads(lineId);
    }


    /**
     * Get all the heads for a specific line id.
     *
     * @param lineId the line id.
     * @return the list of heads available for the given line id.
     */
    @RequestMapping(value = "/api/lines/{lineId}/{head}", method = RequestMethod.GET)
    public List<BusStop> getStops(@PathVariable("lineId") String lineId, @PathVariable("head") String head) {
        return stopsService.getStops(lineId, head);
    }

    /**
     * Retrieve the timetable for a specific stop.
     *
     * @param line the line id.
     * @param head the head id.
     * @param stop the stop id.
     * @param timestamp a timestamp.
     * @return the timetable.
     * @throws Exception in case something bad occurs
     */
    @RequestMapping(value = "/api/tt/{line}/{head}/{stop}", method = RequestMethod.GET)
    public TimeTable specificStopTimeTable(@PathVariable("line") String line,
                                           @PathVariable("head") String head,
                                           @PathVariable("stop") String stop,
                                           @RequestParam(value = "time", required = false) Long timestamp,
                                           @RequestParam(value = "stopName") String stopName,
                                           @RequestParam(value = "forceRefresh", required = false) Boolean refresh) throws Exception {
        TimeTable tt = timeTableService.getTimeTableFor(
                new Stop(line, head, stop, stopName),
                refresh != null ? refresh.booleanValue() : false
        );

        // give next rides
        if (timestamp != null) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timestamp * 1000);
            int day = c.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY) {
                tt.setNextRides(getNext3IfPossible(tt.getSunday(), c));
            } else if (day == Calendar.SATURDAY) {
                tt.setNextRides(getNext3IfPossible(tt.getSaturday(), c));
            } else {
                tt.setNextRides(getNext3IfPossible(tt.getWeek(), c));
            }
        }

        return tt;
    }

    /**
     * Get the next 3 rides if possible.
     *
     * @param next the next.
     * @param c a calendar.
     * @return the next 3 rides if available.
     */
    private List<String> getNext3IfPossible(List<String> next, Calendar c) {
        List<String> nextRides = new ArrayList<>();
        for (String n: next) {
            if (nextRides.size() > 2)
                break;

            Calendar cn = (Calendar)c.clone();
            cn.set(Calendar.HOUR_OF_DAY, Integer.valueOf(n.split(":")[0]));
            cn.set(Calendar.MINUTE, Integer.valueOf(n.split(":")[1]));

            if (cn.after(c))
                nextRides.add(n);
        }
        return nextRides;
    }

    /**
     * Get the stop coordinates (latitude & longitude).
     *
     * @param line the line id.
     * @param stopName the stop name.
     * @return the coordinates for that stop.
     */
    @RequestMapping(value = "/api/coords/{lineId}/{stopName}")
    public LatLon getStopCoordinates(@PathVariable("lineId") String line,
                                     @PathVariable("stopName") String stopName) {
        return stopsService.getStopCoordinates(line, stopName);
    }
}
