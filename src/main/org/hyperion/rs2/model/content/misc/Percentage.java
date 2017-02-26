package org.hyperion.rs2.model.content.misc;


public class Percentage {

    private final double total, remaining;

    public Percentage(final double remaining, final double total) {
        this.total = total;
        this.remaining = remaining;
    }

    public double toDouble(final double min) {
        return remaining/total * (100.0-min) + min;
    }
    public double toDouble() {
        return toDouble(0);
    }

    public String toString(int places, double min) {
        final String format = new StringBuilder("%.").append(places).append("f").toString();
        return String.format(format, toDouble(min));
    }

    public String toString(int places) {
        return toString(places, 0);
    }

    @Override
    public String toString() {
        return toString(1);
    }


}
