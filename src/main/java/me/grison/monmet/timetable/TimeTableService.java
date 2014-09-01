package me.grison.monmet.timetable;

import me.grison.monmet.domain.Stop;
import me.grison.monmet.domain.TimeTable;
import me.grison.monmet.repository.AppRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TimeTableService {
    @Autowired
    AppRepository repository;
    final Matcher everyXMinutes = Pattern.compile("^.*toutes les (\\d+) minutes.*").matcher("");

    public TimeTable getTimeTableFor(Stop stop) throws Exception {
        if (repository.hasTimeTableForStop(stop)) {
            return repository.getTimeTableForStop(stop);
        } else {
            return repository.saveTimeTableForStop(stop, fetchTimeTable(stop));
        }
    }

    public TimeTable fetchTimeTable(Stop stop) throws Exception {
        String url = "http://lemet.fr/src/page_editions_horaires_iframe_build.php?ligne=" + stop.getLine() + "&head=" + stop.getHead().replaceAll("\\s", "%20") + "%7C" + stop.getLine() + "&arret=" + stop.getStopId();
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.html());

        TimeTable timeTable = new TimeTable();
        timeTable.setWeek(extractTimeTable(doc.select("table#horaires tr:eq(1) td.un table").first()));
        timeTable.setSaturday(extractTimeTable(doc.select("table#horaires tr:eq(1) td.deux table").first()));
        timeTable.setSunday(extractTimeTable(doc.select("table#horaires tr:eq(1) td.trois table").first()));
        return timeTable;
    }

    private List<String> extractTimeTable(Element table) {
        List<String> timeTable = new ArrayList<String>();

        for (Element tr : table.select("tr")) {
            String hour = numeric(tr.children().first());
            for (Element td : tr.select("td:gt(0)")) {
                String min = numeric(td);
                if (min.contains(",")) {
                    for (String m: min.split(",")) {
                        if (!m.equals("60"))
                            timeTable.add(hour + ":" + m);
                        else
                            timeTable.add(String.format("%02d", Integer.valueOf(hour) + 1) + ":00");
                    }
                } else if (!"".equals(min)) {
                    timeTable.add(hour + ":" + min);
                }
            }
        }

        return timeTable;
    }

    private String numeric(Element input) {
        String txt = input.text();
        if (everyXMinutes.reset(txt).matches()) {
            String interval = everyXMinutes.group(1);
            String times = interval;
            Integer time = Integer.valueOf(interval);
            do {
                time += Integer.valueOf(interval);
                times += "," + time;
            } while (time < 60);
            return times;
        } else
            return input.text().replaceAll("[^0-9]", "");
    }
}
