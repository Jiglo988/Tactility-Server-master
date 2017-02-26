package org.hyperion.rs2.model.joshyachievementsv2;

import java.util.concurrent.TimeUnit;

public class Interval{

    public final int interval;
    public final TimeUnit unit;

    public Interval(final int interval, final TimeUnit unit){
        this.interval = interval;
        this.unit = unit;
    }

}
