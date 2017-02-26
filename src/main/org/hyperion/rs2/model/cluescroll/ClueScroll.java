package org.hyperion.rs2.model.cluescroll;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.requirement.Requirement;
import org.hyperion.rs2.model.cluescroll.reward.ItemReward;
import org.hyperion.rs2.model.cluescroll.reward.Reward;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.hyperion.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ClueScroll {

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        ELITE
    }

    public enum RareRewards {
        EASY(   new ItemReward(5020, 2500, 5000, 100), //Pkt
                new ItemReward(995, 25000, 100000, 200), //Coins for ironmen
                new ItemReward(12183, 5000, 10000, 25), //Spirit shards
                new ItemReward(15509, 1, 1, 40), //Royal crown
                new ItemReward(16755, 1, 1, 10), //Magic box
                new ItemReward(13870, 1, 1, 40), new ItemReward(13873, 1, 1, 40), new ItemReward(13876, 1, 1, 40), //Morrigan's set
                new ItemReward(13858, 1, 1, 40), new ItemReward(13861, 1, 1, 40), new ItemReward(13864, 1, 1, 40), //Zuriels set
                new ItemReward(13867, 1, 1, 40), //Zuriels staff
                new ItemReward(15602, 1, 1, 30), new ItemReward(15600, 1, 1, 30), new ItemReward(15604, 1, 1, 30), //White infinity
                new ItemReward(15608, 1, 1, 30), new ItemReward(15606, 1, 1, 30), new ItemReward(15610, 1, 1, 30), //Blue infinity
                new ItemReward(15614, 1, 1, 30), new ItemReward(15612, 1, 1, 30), new ItemReward(15616, 1, 1, 30), //Pink infinity
                new ItemReward(15620, 1, 1, 30), new ItemReward(15618, 1, 1, 30), new ItemReward(15622, 1, 1, 30) //Brown infinity
                ),
        MEDIUM( new ItemReward(15332, 1, 5, 250), //Overload
                new ItemReward(5020, 5000, 7500, 100), //Pkt
                new ItemReward(995, 50000, 150000, 200), //Coins for ironmen
                new ItemReward(15511, 1, 1, 20), //Royal Amulet
                new ItemReward(15507, 1, 1, 20), //Royal sceptre
                new ItemReward(18747, 1, 1, 10), //Faithfull shield
                new ItemReward(17237, 1, 1, 10), new ItemReward(17017, 1, 1, 10), new ItemReward(16755, 1, 1, 10), new ItemReward(16931, 1, 1, 10), //Celestial set
                new ItemReward(18768, 1, 1, 40), //Mystery box
                new ItemReward(16755, 1, 1, 20), //Magic box
                new ItemReward(13887, 1, 1, 40), new ItemReward(13893, 1, 1, 40), //Vesta set
                new ItemReward(15033, 1, 1, 20), new ItemReward(15034, 1, 1, 20), new ItemReward(15036, 1, 1, 20), new ItemReward(15035, 1, 1, 20), new ItemReward(15037, 1, 1, 20), new ItemReward(15038, 1, 1, 20), //War chief
                new ItemReward(15021, 1, 1, 20), new ItemReward(15022, 1, 1, 20), new ItemReward(15023, 1, 1, 20), new ItemReward(15024, 1, 1, 20), new ItemReward(15025, 1, 1, 20), new ItemReward(15026, 1, 1, 20), //Serjeant
                new ItemReward(15614, 1, 1, 30), new ItemReward(15612, 1, 1, 30), new ItemReward(15616, 1, 1, 30) //Pink infinity
                ),
        HARD(   new ItemReward(15332, 2, 7, 200), //Overload
                new ItemReward(5020, 7500, 10000, 100), //Pkt
                new ItemReward(995, 75000, 200000, 200), //Coins for ironmen
                new ItemReward(13663, 1, 1, 5), //Legendary ticket
                new ItemReward(2430, 2, 20, 50), //Special restore
                new ItemReward(15241, 1, 1, 20), //Hand cannon
                new ItemReward(13109, 1, 1, 20), //Penguin Mask
                new ItemReward(15241, 1, 1, 5), //Chaotic staff
                new ItemReward(18357, 1, 1, 10), //Chaotic cbow
                new ItemReward(15505, 1, 1, 10), //Royal leggings
                new ItemReward(15503, 1, 1, 10), //Royal shirt
                new ItemReward(17171, 1, 1, 5), //Celestial staff
                new ItemReward(18768, 1, 1, 50), //Mystery box
                new ItemReward(16755, 1, 1, 40), //Magic box
                new ItemReward(13884, 1, 1, 40), new ItemReward(13890, 1, 1, 40), //Statius
                new ItemReward(18808, 1, 1, 20), //Double exp lamp
                new ItemReward(15511, 1, 1, 20), //Royal Amulet
                new ItemReward(15507, 1, 1, 20), //Royal sceptre
                new ItemReward(15033, 1, 1, 20), new ItemReward(15034, 1, 1, 20), new ItemReward(15036, 1, 1, 20), new ItemReward(15035, 1, 1, 20), new ItemReward(15037, 1, 1, 20), new ItemReward(15038, 1, 1, 20), //War chief
                new ItemReward(15021, 1, 1, 20), new ItemReward(15022, 1, 1, 20), new ItemReward(15023, 1, 1, 20), new ItemReward(15024, 1, 1, 20), new ItemReward(15025, 1, 1, 20), new ItemReward(15026, 1, 1, 20) //Serjeant
                ),
        ELITE(  new ItemReward(15332, 4, 10, 100), //Overload
                new ItemReward(5020, 10000, 15000, 100), //Pkt
                new ItemReward(13898, 1, 1, 5), new ItemReward(13892, 1, 1, 5), new ItemReward(13886, 1, 1, 5), new ItemReward(13898, 1, 1, 5),//Cursed Statius
                new ItemReward(8959, 1, 1, 5), new ItemReward(8960, 1, 1, 5), new ItemReward(8961, 1, 1, 5), new ItemReward(8962, 1, 1, 5), new ItemReward(8963, 1, 1, 5), new ItemReward(8964, 1, 1, 5), new ItemReward(8965, 1, 1, 5), //Tricorn hats
                new ItemReward(17660, 1, 1, 5), //vig i
                new ItemReward(11694, 1, 1, 5), //Armadyl GS
                new ItemReward(11337, 1, 1, 5), new ItemReward(16887, 1, 1, 5), //Sag bows
                new ItemReward(995, 100000, 2500000, 200), //Coins for ironmen
                new ItemReward(13663, 1, 1, 10), //Legendary ticket
                new ItemReward(14484, 1, 1, 5), //Dragon claws
                new ItemReward(2430, 3, 30, 50), //Special restore
                new ItemReward(15241, 1, 1, 30), //Hand cannon
                new ItemReward(13109, 1, 1, 30), //Penguin Mask
                new ItemReward(15241, 1, 1, 10), //Chaotic staff
                new ItemReward(18357, 1, 1, 10), //Chaotic cbow
                new ItemReward(18768, 1, 1, 60), //Mystery box
                new ItemReward(16755, 1, 1, 40), //Magic box
                new ItemReward(18739, 1, 1, 20), //RDK
                new ItemReward(18740, 1, 1, 20), //BDK
                new ItemReward(10350, 1, 1, 1), new ItemReward(10352, 1, 1, 1), new ItemReward(10348, 1, 1, 1), new ItemReward(10346, 1, 1, 1), //Third age melee
                new ItemReward(10330, 1, 1, 1), new ItemReward(10332, 1, 1, 1), new ItemReward(10334, 1, 1, 1), new ItemReward(10336, 1, 1, 1), //Third age ranged
                new ItemReward(10342, 1, 1, 1), new ItemReward(10344, 1, 1, 1), new ItemReward(10338, 1, 1, 1), new ItemReward(10340, 1, 1, 1), //Third age mage
                new ItemReward(18808, 1, 1, 40), //Double exp lamp
                new ItemReward(13884, 1, 1, 40), new ItemReward(13890, 1, 1, 40), //Statius
                new ItemReward(13870, 1, 1, 40), new ItemReward(13873, 1, 1, 40), new ItemReward(13876, 1, 1, 40), //Morrigan's set
                new ItemReward(13858, 1, 1, 40), new ItemReward(13861, 1, 1, 40), new ItemReward(13864, 1, 1, 40), //Zuriels set
                new ItemReward(15241, 1, 1, 5), //Chaotic staff
                new ItemReward(18357, 1, 1, 10), //Chaotic cbow
                new ItemReward(15033, 1, 1, 20), new ItemReward(15034, 1, 1, 20), new ItemReward(15036, 1, 1, 20), new ItemReward(15035, 1, 1, 20), new ItemReward(15037, 1, 1, 20), new ItemReward(15038, 1, 1, 20), //War chief
                new ItemReward(15021, 1, 1, 20), new ItemReward(15022, 1, 1, 20), new ItemReward(15023, 1, 1, 20), new ItemReward(15024, 1, 1, 20), new ItemReward(15025, 1, 1, 20), new ItemReward(15026, 1, 1, 20) //Serjeant
                );

        Reward[] rewards;

        RareRewards(Reward... rewards) {
            this.rewards = rewards;
        }
    }

    public List<Reward> getRareRewards(Difficulty difficulty) {
        List<Reward> rewards = new ArrayList<>();
        for (RareRewards reward : RareRewards.values()) {
            if (reward.toString().equalsIgnoreCase(difficulty.name()))
                for (int i = 0; i < reward.rewards.length; i++)
                    rewards.add(reward.rewards[i]);
        }
        return rewards;
    }

    public enum Trigger {
        CRY(Animation.CRY),
        THINK(Animation.THINKING),
        WAVE(Animation.WAVE),
        BOW(Animation.BOW),
        ANGRY(Animation.ANGRY),
        YES(Animation.YES_EMOTE),
        NO(Animation.NO_EMOTE),
        SHRUG(Animation.SHRUG),
        CHEER(Animation.CHEER),
        BECKON(Animation.BECKON),
        LAUGH(Animation.LAUGH),
        JUMP_FOR_JOY(Animation.JOYJUMP),
        YAWN(Animation.YAWN),
        DANCE(Animation.DANCE),
        JIG(Animation.JIG),
        SPIN(Animation.SPIN),
        HEAD_BANG(Animation.HEADBANG),
        BLOW_KISS(Animation.BLOW_KISS),
        PANIC(Animation.PANIC),
        RASPBERRY(Animation.RASPBERRY),
        CLAP(Animation.CLAP),
        SALUTE(Animation.SALUTE),
        GOBLIN_BOW(Animation.GOBLIN_BOW),
        GOBLIN_SALUTE(Animation.GOBLIN_DANCE),
        GLASS_BOX(Animation.GLASS_BOX),
        CLIMB_ROPE(Animation.CLIMB_ROPE),
        LEAN_ON_AIR(Animation.LEAN),
        GLASS_WALL(Animation.GLASS_WALL),
        ATTACK_CAPE(4959),
        DEFENCE_CAPE(4961),
        STRENGTH_CAPE(4981),
        HITPOINTS_CAPE(4971),
        RANGING_CAPE(4973),
        PRAYER_CAPE(4979),
        MAGIC_CAPE(4939),
        COOKING_CAPE(4955),
        WOODCUTTING_CAPE(4957),
        FLETCHING_CAPE(4937),
        FISHING_CAPE(4951),
        FIREMAKING_CAPE(4975),
        CRAFTING_CAPE(4949),
        SMITHING_CAPE(4943),
        MINING_CAPE(4941),
        HERBLORE_CAPE(4969),
        AGILITY_CAPE(4977),
        THIEVING_CAPE(4965),
        SLAYER_CAPE(4967),
        FARMING_CAPE(4963),
        RUNECRAFTING_CAPE(4947),
        HUNTER_CAPE(5158),
        CONSTRUCTION_CAPE(4953),
        SUMMONING_CAPE(8525),
        QUEST_CAPE(4945);

        private final int id;

        Trigger(final int id){
            this.id = id;
        }

        Trigger(final Animation anim){
            this(anim.getId());
        }

        public int getId(){
            return id;
        }
    }

    private int id;
    private String description;
    private Difficulty difficulty;
    private Trigger trigger;

    private final List<Requirement> requirements;
    private final List<Reward> rareRewards;

    public ClueScroll(final int id, final String description, final Difficulty difficulty, final Trigger trigger) {
        this.id = id;
        this.description = description;
        this.difficulty = difficulty;
        this.trigger = trigger;

        requirements = new ArrayList<>();
        rareRewards = new ArrayList<>();

        for (Reward reward : getRareRewards(difficulty))
            rareRewards.add(reward);
    }

    public int getId(){
        return id;
    }

    public void setId(final int id){
        this.id = id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(final String description){
        this.description = description;
    }

    public Difficulty getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(final Difficulty difficulty){
        this.difficulty = difficulty;
    }

    public Trigger getTrigger(){
        return trigger;
    }

    public void setTrigger(final Trigger trigger){
        this.trigger = trigger;
    }

    public List<Requirement> getRequirements(){
        return requirements;
    }

    public boolean hasAllRequirements(final Player player){
        for(final Requirement req : requirements)
            if(!req.apply(player))
                return false;
        return true;
    }

    public void send(final Player player){
        final String[] lines = Misc.wrapString(description.replaceAll("<br>", "\n"), 50).split("\n");
        player.getActionSender().openQuestInterface(String.format("@dre@%s Clue Scroll", Misc.ucFirst(difficulty.toString().toLowerCase())), lines);
        if(player.debug) {
            player.sendf("trigger: %s", trigger);
            for(final Requirement req : requirements)
                player.sendf(req.toString());
        }
    }

    public void apply(final Player player){
        Item oldItem = Item.create(id);
        if(player.getInventory().remove(oldItem) < 1)
            return;
        double currentSteps = 0;
        if(player.getPermExtraData().get("clueScrollProgress") != null)
            try {
                currentSteps = (Double) player.getPermExtraData().get("clueScrollProgress") + 1;
            } catch(Exception e) {
                currentSteps = Double.parseDouble((String)player.getPermExtraData().get("clueScrollProgress")) + 1;
            }
        double maxSteps = getDifficulty().ordinal() + 2;
        double minSteps = getDifficulty().ordinal();
        boolean giveReward = currentSteps > maxSteps;
        double random = Math.random();
        double number = currentSteps / maxSteps;
        if(currentSteps >= minSteps) {
            if (!giveReward && (number) > random) {
                giveReward = true;
            }
        }
        if(giveReward) {
            giveReward = giveReward(player);
        }
        if(!giveReward) {
            Item item = oldItem;
            while(item.getId() == oldItem.getId())
                item = Item.create(ClueScrollManager.getAll(difficulty).get((int) Math.round(Math.random() * (ClueScrollManager.getAll(difficulty).size() - 1 != -1 ? ClueScrollManager.getAll(difficulty).size() - 1 : 0))).getId());
            player.sendMessage("You find another clue scroll!");
            player.getInventory().add(item);
            player.getPermExtraData().put("clueScrollProgress", currentSteps);
        }
    }

    public boolean giveReward(final Player player) {
        player.getPermExtraData().remove("clueScrollProgress");
        int amount = Misc.random(getDifficulty().ordinal() + 3);
        if(amount > 8) {
            amount = 8;
        }
        if(amount < 3 || amount < getDifficulty().ordinal() + 1) {
            amount = 3;
        }
        List<Reward> received = new ArrayList<>();
        while(received.size() != amount) {
            Reward item;
            if(Misc.random(4) == 1) {
                player.sendMessage("Entered rare loot table");
                item = getRandomRare();
            } else {
                item = getRandomNormal();
            }
            if(item != null && !received.contains(item))
                received.add(item);
        }
        for(int i = 0; i <= 8; i++) {
            player.getActionSender().sendUpdateItem(6963, i, null);
        }
        int i = 0;
        for(Reward reward : received) {
            if(reward != null)
                if(reward.getType() == Reward.Type.ITEM) {
                    reward.apply(player, i);
                    i++;
                } else {
                    reward.apply(player);
                }
        }
        player.getActionSender().showInterface(6960);
        return true;
    }

    public Reward getRandomRare() {
        List<Reward> possibleRewards = new ArrayList<>();
        int i = 0;
        while(possibleRewards.isEmpty() && !rareRewards.isEmpty()) {
            for (Reward reward : rareRewards) {
                if (reward.canGet() && !possibleRewards.contains(reward)) {
                    possibleRewards.add(reward);
                }
            }
        }
        int randomItem = Misc.random(possibleRewards.size() - 1);
        return possibleRewards.get(randomItem);
    }

    public Reward getRandomNormal() {
        /*int random = Misc.random(SpawnCommand.giveSpawnables().size());
        int key = -1;
        if(SpawnCommand.giveSpawnables().isEmpty())
            return new ItemReward(4716, 1, 1, 1000);
        while(NewGameMode.getUnitPrice(random == 0 ? 1 : random) < 10000 || !SpawnCommand.giveSpawnables().containsKey(random)) {
            random = Misc.random(SpawnCommand.giveSpawnables().size());
        }
        Item item = Item.create(random);
        if(item.getDefinition().isStackable())
            return new ItemReward(random, 1, 20, 1000);
        return new ItemReward(random, 1, 1, 1000);*/
        return null; //TODO FIX THIS
    }

    public Element toElement(final Document doc){
        final Element element = doc.createElement("cluescroll");
        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("difficulty", difficulty.name());
        element.setAttribute("trigger", trigger.name());
        final Element requirementsElement = doc.createElement("requirements");
        for(final Requirement requirement : requirements)
            requirementsElement.appendChild(requirement.toElement(doc));
        final Element rewardsElement = doc.createElement("rewards");
        /*
        for(final Reward reward : normalRewards)
            rewardsElement.appendChild(reward.toElement(doc));
        for(final Reward reward : rareRewards)
            rewardsElement.appendChild(reward.toElement(doc));
            */
        element.appendChild(ClueScrollUtils.createElement(doc, "description", description));
        element.appendChild(requirementsElement);
        element.appendChild(rewardsElement);
        return element;
    }

    public boolean equals(final Object o){
        if(o == null || !(o instanceof ClueScroll))
            return false;
        if(o == this)
            return true;
        final ClueScroll cs = (ClueScroll) o;
        return cs.id == id
                && cs.description.equals(description)
                && cs.difficulty == difficulty
                && cs.trigger == trigger;
    }

    public String toString(){
        return Integer.toString(id);
    }

    public static ClueScroll parse(final Element element){
        final int id = Integer.parseInt(element.getAttribute("id"));
        final Difficulty difficulty = Difficulty.valueOf(element.getAttribute("difficulty"));
        final Trigger trigger = Trigger.valueOf(element.getAttribute("trigger"));
        final String description = element.getElementsByTagName("description").item(0).getTextContent();
        final ClueScroll clueScroll = new ClueScroll(id, description, difficulty, trigger);
        final Element requirementsElement = (Element) element.getElementsByTagName("requirements").item(0);
        final NodeList requirements = requirementsElement.getElementsByTagName("requirement");
        for(int i = 0; i < requirements.getLength(); i++){
            final Node node = requirements.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element e = (Element) node;
            final Requirement.Type type = Requirement.Type.valueOf(e.getAttribute("type"));
            clueScroll.requirements.add(type.parse(e));
        }
        /*
        final Element rewardsElement = (Element) element.getElementsByTagName("rewards").item(0);
        final NodeList rewards = rewardsElement.getElementsByTagName("reward");
        for(int i = 0; i < rewards.getLength(); i++){
            final Node node = rewards.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element e = (Element) node;
            final Reward.Type type = Reward.Type.valueOf(e.getAttribute("type"));
            clueScroll.normalRewards.add(type.parse(e));
        }
        final Element rareRewardsElement = (Element) element.getElementsByTagName("rareRewards").item(0);
        final NodeList rareRewards = rareRewardsElement.getElementsByTagName("rareReward");
        for(int i = 0; i < rareRewards.getLength(); i++){
            final Node node = rareRewards.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element e = (Element) node;
            final Reward.Type type = Reward.Type.valueOf(e.getAttribute("type"));
            clueScroll.rareRewards.add(type.parse(e));
        }
        */
        return clueScroll;
    }

}
