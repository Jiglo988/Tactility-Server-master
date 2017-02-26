package org.hyperion.rs2.model.content.skill.summoning;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Daniel on 5/28/2016.
 */
public enum PouchData {
    ;

    private static final Map<Integer, PouchData> BY_BUTTON_VALUE = Stream.of(values()).collect(Collectors.toMap(PouchData::getButton, Function.identity()));

    private final Charm CHARM;
    private final int BUTTON, POUCH, INGREDIENT, SHARDS, LEVEL, FAMILIAT, DURATION;
    private final double EXPERIENCE;

    PouchData(int button, int pouch, Charm charm, int shards, int ingredient, int level, double experience, int familiar, int duration) {
        this.BUTTON = button;
        this.POUCH = pouch;
        this.CHARM = charm;
        this.SHARDS = shards;
        this.INGREDIENT = ingredient;
        this.LEVEL = level;
        this.EXPERIENCE = experience;
        this.FAMILIAT = familiar;
        this.DURATION = duration;
    }

    public static PouchData getByIntegerValue(final int value) {
        return BY_BUTTON_VALUE.get(value);
    }

    public int getButton() { return BUTTON; }

    public int getPouch() { return POUCH; }

    public Charm getCharm() { return CHARM; }

    public int getShards() { return SHARDS; }

    public int getIngredient() { return INGREDIENT; }

    public int getLevel() { return LEVEL; }

    public double getExperience() { return EXPERIENCE; }

    public int getFamiliar() { return FAMILIAT; }

    public int getDuration() { return DURATION; }

    private boolean hasPouch(final Player player, final int amount) {
        return player.getInventory().hasItem(Item.create(12155, amount));
    }

    public boolean hasCharm(final Player player, final int amount) {
        return player.getInventory().hasItem(Item.create(getCharm().getItem(), amount));
    }

    public boolean hasShards(final Player player, final int amount) {
        return player.getInventory().hasItem(Item.create(18016, getShards() * amount));
    }

    public void createPouch(final Player player, final int amount) {
        /*if(player.getSkills().getLevel(Skills.SUMMONING) < getLevelRequirement()) {
            player.sendMessage("You need a Summoning level of " + getLevelRequirement() + " to create this pouch.");
            return;
        }

        if(player.getInventory().getCount(getRequiredCharm().getId()) >= getRequiredCharm().getCount()) {
            player.sendMessage("You need at least " + getRequiredCharm().getCount() + " " + getRequiredCharm().getDefinition().getProperName() + (getRequiredCharm().getCount() == 1 ? "" : "s") + " to create this pouch.");
            return;
        }

        if(player.getInventory().getCount(SHARD_ID) >= getShardsAmount()) {
            player.sendMessage("You need at least " + getShardsAmount() + " shards to create this pouch.");
            return;
        }

        if(player.getInventory().getCount(SHARD_ID) >= getShardsAmount()) {
            player.sendMessage("You need at least " + getShardsAmount() + " shards to create this pouch.");
            return;
        }

        if(player.getInventory().getCount(POUCH_ID) >= getShardsAmount()) {
            player.sendMessage("You need a pouch to create this pouch.");
            return;
        }

        if(player.getInventory().getCount(getRequiredItem().getId()) < getRequiredItem().getCount()) {
            player.sendMessage("You need " + getRequiredItem().getCount() + " " + getRequiredItem().getDefinition().getProperName() + (getRequiredItem().getCount() == 1 ? "" : "s") + " to make this pouch.");
            return;
        }

        if(player.getInventory().remove(getRequiredItem()) < getRequiredItem().getCount() || player.getInventory().remove(new Item(SHARD_ID, getShardsAmount())) < getShardsAmount() || player.getInventory().remove(new Item(POUCH_ID)) < 1 || player.getInventory().remove(getRequiredCharm()) < getRequiredCharm().getCount())
            return;

        if(!player.getInventory().hasRoomFor(getPouchItem())) {
            player.getInventory().add(getRequiredItem());
            player.getInventory().add(new Item(SHARD_ID, getShardsAmount()));
            player.getInventory().add(new Item(POUCH_ID));
            player.getInventory().add(getRequiredCharm());
            return;
        }

        player.getInventory().add(getPouchItem());*/
    }
}
