package org.hyperion.rs2.model.possiblehacks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PossibleHack {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MMM dd/yyyy HH:mm");
    private static final SimpleDateFormat DEFAULT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    public final String name;
    public final String ip;
    public final String date;

    public PossibleHack(final String name, final String ip, final String date) {
        this.name = name;
        this.ip = ip;
        this.date = date;
    }

    @Override
    public abstract String toString();

    public String dateString() {
        try {
            final Date date = DEFAULT.parse(this.date);
            return FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }


}
