package me.grison.monmet.domain;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class TimeTable {
    List<String> week = new ArrayList<String>();
    List<String> saturday = new ArrayList<String>();
    List<String> sunday = new ArrayList<String>();
    List<String> nextRides;

    public void mergeWith(TimeTable o) {
        // merge
        week = new ArrayList<String>(o.getWeek());
        saturday = new ArrayList<String>(o.getSaturday());
        sunday = new ArrayList<String>(o.getSunday());

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