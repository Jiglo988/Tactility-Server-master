package org.hyperion.rs2.model.content.jge;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.content.jge.entry.claim.Claims;
import org.hyperion.rs2.model.content.jge.entry.progress.ProgressManager;
import org.hyperion.rs2.model.content.jge.itf.JGrandExchangeInterface;
import org.hyperion.rs2.model.iteminfo.ItemInfo;
import org.hyperion.sql.DbHub;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Created by Administrator on 9/23/2015.
 */
public class JGrandExchange {

    public static boolean enabled = false;

    private static JGrandExchange instance;

    private static final Function<Entry, Object> ITEM_KEY = e -> e.itemId;
    private static final Function<Entry, Object> PLAYER_KEY = e -> e.playerName;
    private static final Function<Entry, Object> TYPE_KEY = e -> e.type;

    public static final int DEFAULT_UNIT_PRICE = 1000;

    private final Map<Object, List<Entry>> map;

    public JGrandExchange(){
        map = new HashMap<>();
    }

    public Stream<Entry> stream(){
        return map.values().stream()
                .flatMap(List::stream)
                .distinct();
    }

    public boolean delete(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().delete(entry);
    }

    public boolean insert(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().insert(entry);
    }

    public boolean updateCancelAndClaims(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().updateCancelAndClaims(entry);
    }

    public boolean updateProgressAndClaims(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().updateProgressAndClaims(entry);
    }

    public boolean updateProgress(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().updateProgress(entry);
    }

    public boolean updateClaims(final Entry entry){
        return DbHub.getPlayerDb().getGrandExchange().updateClaims(entry);
    }

    public boolean load(){
        if(!DbHub.getPlayerDb().isInitialized())
            return false;
        List<Entry> entryList = DbHub.getPlayerDb().getGrandExchange().load();
        if(entryList == null)
            return false;
        entryList.forEach(this::add);
        return true;
    }

    private void add(final Entry entry, final Function<Entry, Object> key){
        final Object k = key.apply(entry);
        map.putIfAbsent(k, new ArrayList<>());
        map.get(k).add(entry);
    }

    private void remove(final Entry entry, final Function<Entry, Object> key){
        final Object k = key.apply(entry);
        if(map.containsKey(k))
            map.get(k).remove(entry);
    }

    public void add(final Entry entry){
        add(entry, ITEM_KEY);
        add(entry, PLAYER_KEY);
        add(entry, TYPE_KEY);
    }

    public void remove(final Entry entry){
        remove(entry, ITEM_KEY);
        remove(entry, PLAYER_KEY);
        remove(entry, TYPE_KEY);
    }

    public void submit(final Entry submitEntry){
        if(!enabled || submitEntry.cancelled || submitEntry.progress.completed() || submitEntry.progress.remainingQuantity() == 0)
            return;
        final Optional<Entry> opt = stream(submitEntry.type.opposite())
                .filter(e -> {
                    if(e.cancelled || e.progress.completed() || e.progress.remainingQuantity() == 0 || e.itemId != submitEntry.itemId)
                        return false;
                    if(e.type != submitEntry.type.opposite())
                        return false;
                    if(submitEntry.type == Entry.Type.BUYING && e.unitPrice > submitEntry.unitPrice)
                        return false;
                    if(submitEntry.type == Entry.Type.SELLING && e.unitPrice < submitEntry.unitPrice)
                        return false;
                    if(e.playerName.equalsIgnoreCase(submitEntry.playerName))
                        return false;
                    if(e.currency != submitEntry.currency)
                        return false;
                    //maybe some other criteria
                    return true;
                })
                .sorted(Comparator.comparingInt(e -> e.unitPrice))
                .min(Comparator.comparing(e -> e.date));
        if(!opt.isPresent())
            return;
        final Entry matchedEntry = opt.get();
        final int submitRemaining = submitEntry.progress.remainingQuantity();
        final int matchedRemaining = matchedEntry.progress.remainingQuantity();
        final int maxQuantity = submitRemaining > matchedRemaining ? matchedRemaining : submitRemaining;
        final ProgressManager submitProgress = submitEntry.progress.copy();
        final Claims submitClaims = submitEntry.claims.copy();
        final ProgressManager matchedProgress = matchedEntry.progress.copy();
        final Claims matchedClaims = matchedEntry.claims.copy();
        switch(submitEntry.type){
            case BUYING:
                //matchedEntry = selling entry
                submitEntry.progress.add(matchedEntry.playerName, Math.min(matchedEntry.unitPrice, submitEntry.unitPrice), maxQuantity);
                submitEntry.claims.addProgress(submitEntry.itemId, maxQuantity);
                if(submitEntry.unitPrice > matchedEntry.unitPrice)
                    submitEntry.claims.addReturn(submitEntry.currency.itemId, (submitEntry.unitPrice - matchedEntry.unitPrice) * maxQuantity);
                matchedEntry.progress.add(submitEntry.playerName, matchedEntry.unitPrice, maxQuantity);
                matchedEntry.claims.addProgress(matchedEntry.currency.itemId, maxQuantity * matchedEntry.unitPrice);
                break;
            case SELLING:
                //matchedEntry = buying entry
                matchedEntry.progress.add(submitEntry.playerName, Math.min(submitEntry.unitPrice, submitEntry.unitPrice), maxQuantity);
                matchedEntry.claims.addProgress(submitEntry.itemId, maxQuantity);
                if(matchedEntry.unitPrice > submitEntry.unitPrice)
                    matchedEntry.claims.addReturn(matchedEntry.currency.itemId, (matchedEntry.unitPrice - submitEntry.unitPrice) * maxQuantity);
                submitEntry.progress.add(matchedEntry.playerName, submitEntry.unitPrice, maxQuantity);
                submitEntry.claims.addProgress(submitEntry.currency.itemId, maxQuantity * submitEntry.unitPrice);
                break;
        }
        if(!updateProgressAndClaims(submitEntry) || !updateProgressAndClaims(matchedEntry)){
            submitEntry.progress = submitProgress;
            submitEntry.claims = submitClaims;
            matchedEntry.progress = matchedProgress;
            matchedEntry.claims = matchedClaims;
            System.err.printf("ERROR UPDATING GE BETWEEN %s and %s%n", submitEntry.playerName, matchedEntry.playerName);
            return;
        }
        submitEntry.ifPlayer(p -> {
            p.getGrandExchangeTracker().notifyChanges(false);
            //p.getLogManager().add(LogEntry.geProgress(submitEntry.progress.last()));
//            p.sendf("[GE Update] %s %s %s x %,d %s you @ %,d %s!",
//                    matchedEntry.playerName, matchedEntry.type.pastTense,
//                    submitEntry.item().getDefinition().getName(), maxQuantity,
//                    submitEntry.type == Entry.Type.BUYING ? "to" : "from",
//                    submitEntry.unitPrice, submitEntry.currency.shortName);
            if (p.getGrandExchangeTracker().activeSlot == submitEntry.slot)
                JGrandExchangeInterface.ViewingEntry.set(p, submitEntry);
            else
                JGrandExchangeInterface.Entries.setAll(p, p.getGrandExchangeTracker().entries);
        });
        matchedEntry.ifPlayer(p -> {
            p.getGrandExchangeTracker().notifyChanges(false);
            //p.getLogManager().add(LogEntry.geProgress(matchedEntry.progress.last()));
//            p.sendf("[GE Update] %s %s %s x %,d %s you @ %,d %s!",
//                    submitEntry.playerName, submitEntry.type.pastTense,
//                    submitEntry.item().getDefinition().getName(), maxQuantity,
//                    matchedEntry.type == Entry.Type.BUYING ? "to" : "from",
//                    submitEntry.unitPrice, submitEntry.currency.shortName);
            if (p.getGrandExchangeTracker().activeSlot == matchedEntry.slot)
                JGrandExchangeInterface.ViewingEntry.set(p, matchedEntry);
            else
                JGrandExchangeInterface.Entries.setAll(p, p.getGrandExchangeTracker().entries);
        });
        if(!submitEntry.progress.completed())
            submit(submitEntry); //what if there are other entries
    }

    public List<Entry> get(final Object playerOrItemId){
        return map.getOrDefault(playerOrItemId, Collections.emptyList());
    }

    public Stream<Entry> stream(final Object playerOrItemId){
        return get(playerOrItemId).stream();
    }

    public boolean contains(final Object playerOrItemId){
        return !get(playerOrItemId).isEmpty();
    }

    public IntSummaryStatistics itemUnitPriceStats(final int itemId, final Entry.Type type, final Entry.Currency currency){
        return stream(itemId)
                .filter(e -> !e.cancelled && e.type == type && e.currency == currency)
                .mapToInt(e -> e.unitPrice)
                .summaryStatistics();
    }

    public int defaultItemUnitPrice(final int itemId, final Entry.Type type, final Entry.Currency currency){
        final int avg = (int)Math.round(DbHub.getPlayerDb().getGrandExchange().averagePrice(itemId, type, currency));
        return avg < 1 ? DEFAULT_UNIT_PRICE : avg;
    }

    public static JGrandExchange getInstance(){
        return instance;
    }

    public static void init(){
        ItemInfo.geBlacklist.load();
        instance = new JGrandExchange();
        if(instance.load() && Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
            Server.getLogger().log(Level.INFO, "Grand Exchange has successfully loaded.");
    }
}
