package org.hyperion.rs2.model.challenge;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.util.TextUtils;

import java.util.Random;

public class Challenge {

    private static final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RAND = new Random();

    private final String name;
    private final String text;
    private final Item item;

    private Challenge(final String name, final String text, final int id, final int amount){
        this.name = name;
        this.text = text;

        item = new Item(id, amount);
    }

    public String getName(){
        return TextUtils.titleCase(name);
    }

    public String getText(){
        return text;
    }

    public Item getPrize(){
        return item;
    }

    public boolean isCorrect(final String text){
        return text.equals(text);
    }

    public void send(final Player player, final boolean alert){
        if(alert){
            player.sendf(
                    "Alert##%s's Challenge##::challenge %s##Prize: %s x%,d!",
                    getName(), getText(), getPrize().getDefinition().getName(), getPrize().getCount()
            );
        }else{
            player.sendf("@red@----------------------------------------------------------------------------------------");
            player.sendf("@blu@%s@bla@'s Challenge!", getName());
            player.sendf("::challenge @blu@%s", getText());
            player.sendf("Prize: @blu@%s@bla@ x@blu@%,d@blu@", getPrize().getDefinition().getName(), getPrize().getCount());
            player.sendf("@red@----------------------------------------------------------------------------------------");
        }
    }

    public boolean equals(final Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!(o instanceof Challenge))
            return false;
        final Challenge c = (Challenge) o;
        return c.name.equals(name)
                && c.text.equals(text)
                && c.item.getId() == item.getId()
                && c.item.getCount() == item.getCount();
    }

    public static Challenge create(final Player player, final int length, final int id, final int amount){
        final StringBuilder bldr = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            bldr.append(DATA.charAt(RAND.nextInt(DATA.length())));
        return new Challenge(player.getSafeDisplayName(), bldr.toString(), id, amount);
    }
}
