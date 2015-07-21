package me.grison.monmet.timetable;

import me.grison.monmet.domain.Stop;
import me.grison.monmet.domain.TimeTable;
import me.grison.monmet.repository.AppRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Timetable service.
 */
@Service
public class TimeTableService {
    @Autowired
    AppRepository repository;
    @Autowired
    StopsService stopsService;
    @Value("${timeTable.refreshInterval}")
    Long timeTableRefreshInterval;
    @Value("${timeTable.connectionTimeout}")
    Integer timeTableConnectionTimeout;
    final Matcher everyXMinutes = Pattern.compile("^.*toutes les (\\d+) minutes.*").matcher("");

    /**
     * Get the timetable for a specific stop.
     *
     * @param stop the stop.
     * @return the timetable.
     * @throws Exception in case something bad occurs.
     */
    public TimeTable getTimeTableFor(Stop stop, boolean forceRefresh) throws Exception {
        if (repository.hasTimeTableForStop(stop))
            if (repository.daysSinceLastTimeTableUpdate(stop) > timeTableRefreshInterval)
                return repository.saveTimeTableForStop(stop, fetchTimeTable(stop));
            else
                return repository.getTimeTableForStop(stop);
        else
            return repository.saveTimeTableForStop(stop, fetchTimeTable(stop));
    }

    /**
     * Fetch the timetable from lemet.fr.
     *
     * @param stop th stop.
     * @return the timetable.
     * @throws Exception
     */
    public TimeTable fetchTimeTable(Stop stop) throws Exception {
        String url = "http://lemet.fr/vos-fiches-horaires-a-larret/?ligne=" + stop.getLine() + "&head=" + stop.getHead().replaceAll("\\s", "%20") + "%7C" + stop.getLine() + "&arret=" + stop.getStopId();
        System.out.println("Fetching from: " + url);
        Document doc = Jsoup.connect(url).timeout(timeTableConnectionTimeout).get();

        String imageUrl = doc.select("#target_plan").attr("src").replaceAll(" ", "%20");
        System.out.println("-> " + imageUrl);

        TimeTable tt = new TimeTable();
        Tesseract instance = Tesseract.getInstance();
        try {
            instance.setPageSegMode(6); // Assume a single uniform block of text.
            //instance.setTessVariable("tessedit_char_whitelist", "0123456789“"); // only allow digits
            String result = instance.doOCR(ImageIO.read(new URL(imageUrl)));
            System.out.println("OCR Ok");
            Arrays.asList(result.split("\n"))
                    .stream()
                    .filter(s -> s.contains("“"))
                    .map(s -> s.replaceAll("O", "0") + " ")
                    .map(s -> Arrays.asList((s.split(" ")[0] + s.replaceAll(s.split(" ")[0], ";")).split(";")))
                    .forEach(line -> tt.addHourLine(line));
            System.out.println(tt.getWeek());
            System.out.println(tt.getSunday());
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return tt;
    }

        /*TimeTable timeTable = new TimeTable();
        timeTable.setWeek(extractTimeTable(doc.select("table#horaires tr:eq(1) td.un table").first()));
        timeTable.setSaturday(extractTimeTable(doc.select("table#horaires tr:eq(1) td.deux table").first()));
        timeTable.setSunday(extractTimeTable(doc.select("table#horaires tr:eq(1) td.trois table").first()));

        // Go get also the stop coordinates
        stopsService.fetchStopCoordinates(stop.getLine(), stop.getStopName());
        return timeTable;*/
    }

    /**
     * Extract the timetable from an HTML document.
     *
     * @param table the table#horaires in the HTML page.
     * @return the timetable.
     */
    private List<String> extractTimeTable(Element table) {
        List<String> timeTable = new ArrayList<>();

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

    /**
     * Extracts a numeric from the given input.
     *
     * @param input the text to extract a numeric from.
     * @return the time
     */
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
