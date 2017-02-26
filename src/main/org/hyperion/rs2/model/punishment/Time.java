package org.hyperion.rs2.model.punishment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a z");

    private long startTime;
    private long duration;
    private TimeUnit unit;

    private boolean expired;

    public Time(final long startTime, final long duration, final TimeUnit unit){
        this.startTime = startTime;

        set(duration, unit);
    }

    public Time(final long duration, final TimeUnit unit){
        this(System.currentTimeMillis(), duration, unit);
    }

    public long getStartTime(){
        return startTime;
    }

    public void setStartTime(final long startTime){
        this.startTime = startTime;
    }

    public Date getStartDate(){
        return new Date(getStartTime());
    }

    public String getStartDateStamp(){
        return DATE_FORMAT.format(getStartDate());
    }

    public long getDuration(){
        return duration;
    }

    public long getDuration(final TimeUnit unit){
        return unit.convert(getDuration(), getUnit());
    }

    public TimeUnit getUnit(){
        return unit;
    }

    public void set(final long duration, final TimeUnit unit){
        this.duration = duration;
        this.unit = unit;
    }

    public void set(final long duration){
        set(duration, unit);
    }

    public void set(final Time time){
        set(time.getDuration(), time.getUnit());
    }

    public long getExpirationTime(){
        return getStartTime() + getDuration(TimeUnit.MILLISECONDS);
    }

    public Date getExpirationDate(){
        return new Date(getExpirationTime());
    }

    public String getExpirationDateStamp(){
        return DATE_FORMAT.format(getExpirationDate());
    }

    public void setExpired(final boolean expired){
        this.expired = expired;
    }

    public boolean isExpired(){
        return expired || System.currentTimeMillis() > getExpirationTime();
    }

    public String getRemainingTimeStamp(){
        if(isExpired())
            return "----";
        long remaining = getExpirationTime() - System.currentTimeMillis();
        long seconds = remaining / 1000;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        long hours = minutes / 60;
        minutes -= hours * 60;
        long days = hours / 24;
        hours -= days * 24;
        long weeks = days / 7;
        days -= weeks * 7;
        long years = weeks / 52;
        weeks -= years * 52;
        final StringBuilder bldr = new StringBuilder();
        if(years > 0)
            bldr.append(String.format("%,d Year%s", years, years > 1 ? "s" : "")).append(" ");
        if(weeks > 0)
            bldr.append(String.format("%,d Week%s", weeks, weeks > 1 ? "s" : "")).append(" ");
        if(days > 0)
            bldr.append(String.format("%,d Day%s", days, days > 1 ? "s" : "")).append(" ");
        if(hours > 0)
            bldr.append(String.format("%,d Hour%s", hours, hours > 1 ? "s" : "")).append(" ");
        if(minutes > 0)
            bldr.append(String.format("%,d Minute%s", minutes, minutes > 1 ? "s" : "")).append(" ");
        if(seconds > 0)
            bldr.append(String.format("%,d Second%s", seconds, seconds > 1 ? "s" : "")).append(" ");
        return bldr.toString();
    }

    public String toString(){
        return String.format("%,d %s", getDuration(), getUnit());
    }

    public static Time create(final long startTime, final long duration, final TimeUnit unit){
        return new Time(startTime, duration, unit);
    }

    public static Time create(final long duration, final TimeUnit unit){
        return new Time(duration, unit);
    }
}
