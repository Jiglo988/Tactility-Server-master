package org.hyperion.rs2.model.combat.npclogs;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.NPCDefinition;

public final class NPCKillsLogger {
	
	private Map<Integer, Integer> wrapped;
		
	public NPCKillsLogger() {
		wrapped = new TreeMap<>();
		wrapped.put(8133, 0); //so it doens't null bug playersave
	}
	
	public void edit(final String loaded) {
		final String[] split = loaded.split(",");
		for(final String s : split) {
			final String[] semiSplit = s.split(":");
			wrapped.put(Integer.valueOf(semiSplit[0]), Integer.valueOf(semiSplit[1]));
		}
	}
	
	public int log(final NPC npc) {
		return log(npc.getDefinition().getId());
	}
	
	private int log(final int id) {
		if(wrapped.containsKey(id)) {
			final int newCount = wrapped.get(id) + 1;
			wrapped.put(id, newCount);
			return newCount;
		} else
			wrapped.put(id, 1);
		return 1;
	}
	
	public String[] getDisplay() {
		final String[] strings = new String[wrapped.size()];
		int i = 0;
		for(Map.Entry<Integer, Integer> entry : wrapped.entrySet()) {
			strings[i++] = String.format("%s - @red@%d", NPCDefinition.forId(entry.getKey()).getName(), entry.getValue());
		}
		return strings;
	}

    public Map<Integer, Integer> map(){
        return wrapped;
    }
	
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final Set<Map.Entry<Integer, Integer>> set = wrapped.entrySet();
		int size = set.size();
		for(Map.Entry<Integer, Integer> entry : set) {
			builder.append(String.format("%d:%d", entry.getKey(), entry.getValue()));
			if(--size > 0)
				builder.append(",");
		}
		return builder.toString();
	}
	
}
