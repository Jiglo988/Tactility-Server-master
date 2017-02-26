package org.hyperion.rs2.model;

import org.hyperion.rs2.model.content.specialareas.NIGGERUZ;

import java.util.Arrays;
import java.util.LinkedList;

public class OSPK extends NIGGERUZ {

    public OSPK() {
        super(600);
    }
	
	public String canEnter(Player player) {
        final String base = "You cannot have: ";
        final StringBuilder builder = new StringBuilder().append(base);
		for(Item i : player.getEquipment().toArray()) {
			if(i != null)
				if(!valid(i.getId()))
					builder.append(i.getDefinition().getName()).append(", ");
		}
		for(Item i : player.getInventory().toArray()) {
			if(i != null)
				if(!valid(i.getId()))
                    builder.append(i.getDefinition().getName()).append(", ");
		}
        if(!builder.toString().equalsIgnoreCase(base))
            return builder.toString();
		if(!player.getPrayers().isDefaultPrayerbook())
			return "You must be on the normal prayer book!";
		return "";
	}
	
	private static LinkedList<Integer> exceptions = new LinkedList<>(Arrays.asList(13351));

	private static boolean valid(int id) {
		return id < 12000 || exceptions.contains(id);
	}

	
}
