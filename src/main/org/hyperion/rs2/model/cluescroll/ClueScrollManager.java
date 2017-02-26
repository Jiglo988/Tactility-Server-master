package org.hyperion.rs2.model.cluescroll;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.logging.Level;

public final class ClueScrollManager {

    private static final File FILE = new File("./data/cluescrolls.xml");
    private static final Map<Integer, ClueScroll> MAP = new HashMap<>();
    private static final Map<ClueScroll.Difficulty, List<ClueScroll>> DIFFICULTY_MAP = new HashMap<>();

    static{
        try{
            load();
            if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
                Server.getLogger().log(Level.INFO, "Successfully loaded " + MAP.size() + " Clue Scrolls.");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private ClueScrollManager(){}

    public static void trigger(final Player player, final int id){
        final ClueScroll clue = getInInventory(player);
        if(clue == null)
            return;
        if(clue.getTrigger().getId() != id) {
            if(player.debug)
                player.sendf("cluescroll trigger: %d | your trigger: %d", clue.getTrigger().getId(), id);
            return;
        }
        if(getInventoryCount(player) > 1){
            player.sendMessage("You are only allowed to have 1 clue scroll in your inventory!");
            return;
        }
        if(!clue.hasAllRequirements(player)) {
            if(player.debug)
                player.sendMessage("You do not meet all requirements for this clue scroll.");
            return;
        }
        clue.apply(player);
    }

    public static boolean isClue(int id) {
        final ClueScroll cs = get(id);
        return cs != null;
    }

    public static void trigger(final Player player, final ClueScroll.Trigger trigger){
        trigger(player, trigger.getId());
    }

    public static ClueScroll getInInventory(final Player player){
        for(final Item i : player.getInventory().toArray()){
            if(i == null)
                continue;
            final ClueScroll cs = get(i.getId());
            if(cs != null)
                return cs;
        }
        return null;
    }

    public static ClueScroll getInBank(final Player player){
        for(final Item i : player.getBank().toArray()){
            if(i == null)
                continue;
            final ClueScroll cs = get(i.getId());
            if(cs != null)
                return cs;
        }
        return null;
    }

    public static boolean hasClueScroll(final Player player) {
        return getBankCount(player) == 0 && getInventoryCount(player) == 0;
    }

    public static int getInventoryCount(final Player player){
        int count = 0;
        for(final Item i : player.getInventory().toArray())
            if(i != null && get(i.getId()) != null)
                ++count;
        return count;
    }

    public static int getBankCount(final Player player){
        int count = 0;
        for(final Item i : player.getBank().toArray())
            if(i != null && get(i.getId()) != null)
                ++count;
        return count;
    }

    public static ClueScroll get(final int id){
        return MAP.get(id);
    }

    public static void add(final ClueScroll clueScroll){
        MAP.put(clueScroll.getId(), clueScroll);
    }

    public static void remove(final ClueScroll clueScroll){
        MAP.remove(clueScroll.getId());
    }

    public static Collection<ClueScroll> getAll(){
        return MAP.values();
    }

    public static List<ClueScroll> getAll(final ClueScroll.Difficulty difficulty){
        return DIFFICULTY_MAP.get(difficulty);
    }

    public static int size(){
        return MAP.size();
    }

    public static void save() throws Exception{
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder bldr = factory.newDocumentBuilder();
        final Document doc = bldr.newDocument();
        final Element root = doc.createElement("cluescrolls");
        for(final ClueScroll clueScroll : MAP.values())
            root.appendChild(clueScroll.toElement(doc));
        doc.appendChild(root);
        final Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(FILE)));
    }

    public static void load() throws Exception{
        if(!FILE.exists())
            return;
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder bldr = factory.newDocumentBuilder();
        final Document doc = bldr.parse(FILE);
        final Element clueScrollsElement = (Element) doc.getElementsByTagName("cluescrolls").item(0);
        final NodeList list = clueScrollsElement.getElementsByTagName("cluescroll");
        for(int i = 0; i < list.getLength(); i++){
            final Node node = list.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element element = (Element) node;
            final ClueScroll clueScroll = ClueScroll.parse(element);
            if(!DIFFICULTY_MAP.containsKey(clueScroll.getDifficulty()))
                DIFFICULTY_MAP.put(clueScroll.getDifficulty(), new ArrayList<>());
            DIFFICULTY_MAP.get(clueScroll.getDifficulty()).add(clueScroll);
            MAP.put(clueScroll.getId(), clueScroll);
        }
    }
}
