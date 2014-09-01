package me.grison.monmet.rest;

import me.grison.monmet.domain.Stop;
import me.grison.monmet.domain.TimeTable;
import me.grison.monmet.timetable.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class AppController {
    //@Autowired
    //StopsService stopsService;
    @Autowired
    TimeTableService timeTableService;

   /*
    @RequestMapping(value = "/api/stops", method = RequestMethod.GET)
    public Set<Line> allLines() {
        return stopsService.getAllLines();
    }   */

    @RequestMapping(value = "/api/tt/{line}/{head}/{stop}", method = RequestMethod.GET)
    public TimeTable specificStopTimeTable(@PathVariable("line") String line,
                                           @PathVariable("head") String head, // can be multiple separated by ','
                                           @PathVariable("stop") String stop, // can be multiple separated by ','
                                           @RequestParam(value = "time", required = false) Long timestamp) throws Exception {
        String[] heads = head.split(",");
        String[] stops = stop.split(",");
        TimeTable tt = new TimeTable();
        for (int i = 0; i < stops.length; ++i) {
            tt.mergeWith(timeTableService.getTimeTableFor(new Stop(line, heads[i], stops[i], "")));
        }
        //Stop specificStop = new Stop(line, head, stop, "");
        //TimeTable tt = timeTableService.getTimeTableFor(specificStop);

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

    private List<String> getNext3IfPossible(List<String> next, Calendar c) {
        List<String> nextRides = new ArrayList<String>();
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
}
