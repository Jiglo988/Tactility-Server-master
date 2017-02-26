package org.hyperion.rs2.model.possiblehacks;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class IPChange extends PossibleHack {

    public final String newIp;

    public IPChange(final String name, final String ip, final String date, final String newIp) {
        super(name, ip, date);
        this.newIp = newIp;
    }

    public String toString() {
        return String.format("[@red@IP@bla@] %s @blu@>@bla@ %s.", ip, newIp);
    }
}
