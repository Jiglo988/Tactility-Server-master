package org.hyperion.rs2.model.content.ticket;

import org.hyperion.rs2.model.Rank;

import java.util.Optional;

public class TicketHolder {

    private Ticket ticket;
    private long lastTicket = 0L;

    public final Optional<Ticket> get() {
        return Optional.ofNullable(ticket);
    }

    public final void create(final String name, final String title, final String reason, final Rank min_rank) {
        if(ticket != null)
            TicketManager.remove(ticket);
        this.ticket = new Ticket(name, reason, title, min_rank);
        lastTicket = System.currentTimeMillis();
        TicketManager.add(this.ticket);
    }

    public final boolean canMakeTicket() {
        return System.currentTimeMillis() - lastTicket >= 60000;
    }

    public final void fireOnLogout() {
        if(ticket != null)
            TicketManager.remove(ticket);
    }
}
