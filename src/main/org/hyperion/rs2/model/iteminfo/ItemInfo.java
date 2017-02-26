package org.hyperion.rs2.model.iteminfo;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ItemInfo{

    public static class UnSpawnables extends ItemInfo{

        private UnSpawnables(){
            super("./data/unspawnables.txt");
        }

        public boolean trySpawn(final Player player, final Item item){
            if(check(player, item.getDefinition()))
                return false;
            if(!player.getInventory().hasRoomFor(item)){
                player.sendf("You don't have enough room for %,d %s", item.getCount(), item.getDefinition().getProperName());
                return false;
            }
            player.getInventory().add(item);
            return true;
        }

        public boolean trySpawn(final Player player, final int id, final int quantity){
            return trySpawn(player, Item.create(id, quantity));
        }
    }

    public static class UnTradeables extends ItemInfo{

        private UnTradeables(){
            super("./data/untradeables.txt");
        }

    }

    public static class GrandExchangeBlackList extends ItemInfo {

        private GrandExchangeBlackList(){
            super("./data/ge_blacklist.txt");
        }
    }

    public static class Entry{

        public final String rawMsg;
        public final Predicate<ItemDefinition> matcher;

        private Entry(final String rawMsg, final Predicate<ItemDefinition> matcher){
            this.rawMsg = rawMsg;
            this.matcher = matcher;
        }

        public void msg(final Player player, final ItemDefinition def){
            if(rawMsg != null)
                player.sendf(msg(def));
        }

        public String msg(final ItemDefinition def){
            return rawMsg.replace("$id$", Integer.toString(def.getId()))
                    .replace("$name$", def.getProperName())
                    .replace("$server$", Configuration.getString(Configuration.ConfigurationObject.NAME));
        }

        private static Entry id(final String msg, final int id){
            return new Entry(msg, def -> def.getId() == id);
        }

        private static Entry nameEquals(final String msg, final String name){
            return new Entry(msg, def -> def.getProperName().equalsIgnoreCase(name));
        }

        private static Entry nameContains(final String msg, final String substring){
            return new Entry(msg, def -> def.getProperName().toLowerCase().contains(substring.toLowerCase()));
        }
    }

    public static UnSpawnables unspawnables = new UnSpawnables();
    public static UnTradeables untradeables = new UnTradeables();
    public static GrandExchangeBlackList geBlacklist = new GrandExchangeBlackList();

    private static final Pattern MSG =
            Pattern.compile("msg ([^=]+)\\s*=\\s*([^=]+)");

    private static final Pattern ENTRY =
            Pattern.compile("entry-(id|equals|contains) ([^=]+)\\s*(?:=\\s*([^=]+))?");

    private static final Pattern RANGE =
            Pattern.compile("(\\d{1,8})-(\\d{1,8})");

    private final File file;

    private final List<Entry> entries;
    private final Map<Integer, Entry> cache;

    private ItemInfo(final String file){
        this.file = new File(file);

        entries = new ArrayList<>();

        cache = new HashMap<>();
    }

    public int size(){
        return entries.size();
    }

    public Entry find(final ItemDefinition def){
        if(def == null)
            return null;
        if(cache.containsKey(def.getId()))
            return cache.get(def.getId());
        final Entry entry = entries.stream()
                .filter(e -> e.matcher.test(def))
                .findFirst()
                .orElse(null);
        if(entry != null)
            cache.put(def.getId(), entry);
        return entry;
    }

    public boolean contains(final ItemDefinition def){
        return find(def) != null;
    }

    public boolean check(final Player player, final ItemDefinition def){
        final Entry e = find(def);
        if(e == null)
            return false;
        e.msg(player, def);
        return true;
    }

    public boolean check(final Player player, final int itemId){
        return check(player, ItemDefinition.forId(itemId));
    }

    public boolean load(){
        if(!file.exists())
            return true;
        final Map<String, String> msgs = new HashMap<>();
        try(final Scanner input = new Scanner(file)){
            while(input.hasNextLine()){
                final String line = input.nextLine().trim()
                        .replaceAll("\\/\\*([^*/]+)\\*\\/", "");
                if(line.isEmpty() || line.startsWith("//") || line.startsWith("ignore"))
                    continue;
                Matcher m;
                if((m = MSG.matcher(line)).find()){
                    final String key = m.group(1).trim();
                    final String value = m.group(2).trim();
                    if(key.isEmpty() || value.isEmpty())
                        continue;
                    msgs.put(key, value);
                }else if((m = ENTRY.matcher(line)).find()){
                    final String matcherStr = m.group(1).trim();
                    final int matcherType = matcherStr.equals("id") ? 0
                            : matcherStr.equals("equals") ? 1
                            : matcherStr.equals("contains") ? 2
                            : -1;
                    if(matcherType == -1)
                        throw new IllegalArgumentException("Invalid entry type");
                    String msg = m.group(3);
                    if(msg != null){
                        msg = msg.trim();
                        msg = msg.isEmpty() ? null : msgs.getOrDefault(msg, msg);
                    }
                    final String fmsg = msg;
                    Stream.of(m.group(2).trim().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty() && !s.startsWith("//") && !s.startsWith("ignore"))
                            .forEach(s -> {
                                switch(matcherType){
                                    case 0:
                                        Matcher m2;
                                        if((m2 = RANGE.matcher(s)).find()){
                                            final int start = Integer.parseInt(m2.group(1));
                                            final int end = Integer.parseInt(m2.group(2));
                                            for(int id = start; id <= end; id++)
                                                entries.add(Entry.id(fmsg, id));
                                        }else{
                                            entries.add(Entry.id(fmsg, Integer.parseInt(s)));
                                        }
                                        break;
                                    case 1:
                                        entries.add(Entry.nameEquals(fmsg, s));
                                        break;
                                    case 2:
                                        entries.add(Entry.nameContains(fmsg, s));
                                        break;
                                }
                            });
                }
            }
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean reload(){
        entries.clear();
        cache.clear();
        return load();
    }

    public static boolean init(){
        return unspawnables.load()
                && untradeables.load()
                && geBlacklist.load();
    }
}
