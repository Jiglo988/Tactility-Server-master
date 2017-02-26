package org.hyperion.rs2.model.content.ticket;

import org.hyperion.rs2.model.Rank;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 1/9/15
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ticket {

    public static int current_ticket = 0;

    public final int id;
    public final String name, request, title;
    public final Rank min_rank;

    public Ticket(final String name, final String request, final String title, final Rank min_rank) {
        this.id = current_ticket++;
        this.name = name;
        this.request = request;
        this.title = title;
        this.min_rank = min_rank;
    }

}
