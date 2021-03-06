package me.grison.monmet.domain;


import com.google.common.collect.ImmutableSet;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a time table.
 * Times are stored as string formatted like this `HH:mm`.
 * There are different times for monday-friday, saturday and sunday.
 */
@Data
@XmlRootElement
public class TimeTable {
    List<String> week = new ArrayList<>();
    List<String> saturday = new ArrayList<>();
    List<String> sunday = new ArrayList<>();
    List<String> nextRides;


        public List<String> collectMinutes(String line, String hour) {
            return Arrays.asList(line.split(" ")).stream()
                           .filter(s -> !s.trim().isEmpty()).map(s -> hour + ":" + s.trim()).collect(Collectors.toList());
        }

        public void addHourLine(List<String> line) {
            String hour = line.get(0).replace("“", "");
            week.addAll(collectMinutes(line.get(1), hour));
            saturday.addAll(collectMinutes(line.get(2), hour));
            sunday.addAll(collectMinutes(line.get(3), hour));
        }

    /**
     * Merge with another timetable.
     * @param o the other timetable
     */
    public void mergeWith(TimeTable o) {
        // merge
        week = new ArrayList<>(o.getWeek());
        saturday = new ArrayList<>(o.getSaturday());
        sunday = new ArrayList<>(o.getSunday());

        // sort
        final Comparator<String> comparator = stringTimeComparator();
        Collections.sort(week, comparator);
        Collections.sort(saturday, comparator);
        Collections.sort(sunday, comparator);

        // remove duplicates
        week = ImmutableSet.copyOf(week).asList();
        saturday = ImmutableSet.copyOf(saturday).asList();
        sunday = ImmutableSet.copyOf(sunday).asList();
    }

    /**
     * String time comparator.
     * @return a Comparator&lt;String> to sort times having the following format `HH:mm`.
     */
    private Comparator<String> stringTimeComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer h1 = Integer.valueOf(o1.split(":")[0]);
                Integer h2 = Integer.valueOf(o2.split(":")[0]);
                Integer m1 = Integer.valueOf(o1.split(":")[1]);
                Integer m2 = Integer.valueOf(o2.split(":")[1]);
                if (h1.intValue() != h2.intValue()) {
                    return h1.compareTo(h2);
                } else {
                    return m1.compareTo(m2);
                }
            }
        };
    }
}
