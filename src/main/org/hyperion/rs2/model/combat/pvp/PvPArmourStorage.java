package org.hyperion.rs2.model.combat.pvp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PvPArmourStorage {
	
	public static void main(String[] args) {
		PvPArmourStorage storage = new PvPArmourStorage();
		System.out.println(storage);
		for(int i = 0; i < 999; i++) {
			int degrade = storage.degrade(STATIUS_HELM);
			System.out.println(degrade);
		}
		storage.editFromString(storage.toString());
		System.out.println(storage);
        int degrade = storage.degrade(STATIUS_HELM);
        System.out.println(storage + " \n" + degrade);
	}
	
	public static final int DEFAULT_CHARGE = 1000;
	
	public static final int STATIUS_BODY = 13884, STATIUS_LEGS = 13890, STATIUS_HELM = 13896, STATIUS_WARHAMMER = 13902;
	public static final int VESTA_BODY = 13887, VESTA_LEGS = 13893, VESTA_SWORD = 13899;
	public static final int MORRIGANS_TOP = 13870, MORRIGANS_CHAPS = 13873, MORRIGANS_COIF = 13876;
    public static final int ZURIELS_TOP = 13858, ZURIELS_BOTTOMS = 13861, ZURIELS_HAT = 13864, ZURIELS_STAFF = 13867;
    public static final int VESTA_DEG_TOP = 13889, VESTA_DEG_LEGS = 13895;
	
	private static final int[] armours = new int[]{STATIUS_BODY, STATIUS_LEGS, STATIUS_HELM, STATIUS_WARHAMMER, VESTA_BODY, VESTA_LEGS, VESTA_SWORD, MORRIGANS_TOP
    , MORRIGANS_CHAPS, MORRIGANS_COIF, ZURIELS_BOTTOMS, ZURIELS_STAFF, ZURIELS_TOP, ZURIELS_HAT, VESTA_DEG_TOP, VESTA_DEG_LEGS};
	
	public static final int[] getArmours() {
		return armours.clone();
	}
	
	private final Map<Integer, Integer> wrappedMap;

	public PvPArmourStorage() {
		wrappedMap = new HashMap<>();
		for(int i = 0; i < armours.length; i++)
			wrappedMap.put(armours[i], DEFAULT_CHARGE);

	}
	
	private PvPArmourStorage edit(int[] armours, int[] args) {
		for(int i = 0; i < armours.length; i++) {
			wrappedMap.put(armours[i], args[i]);
		}
		return this;
	}
	
	public PvPArmourStorage editFromString(String fromString) {
        final String armourData[] = fromString.split(",");
		int[] armours = new int[armourData.length];
		int[] armourCharges = new int[armourData.length];
		for(int i = 0; i < armourData.length; i++) {
			try {
				final String s = armourData[i];
				final String[] pieceData = s.split(":");
				armours[i] = Integer.parseInt(pieceData[0]);
				armourCharges[i] = Integer.parseInt(pieceData[1]);
			} catch (Exception e) {
				System.err.printf("Error in creating PvP Armour #%d\n", i);
			}
		}
		return edit(armours, armourCharges);
	}
	
	public synchronized boolean contains(int id) {
		return wrappedMap.containsKey(id);
	}
	
	public synchronized int degrade(int id) {
		if(!contains(id))
			return DEFAULT_CHARGE;
		int amount = wrappedMap.get(id) - 1;
		wrappedMap.put(id, amount);			
		if(amount == 0)
			wrappedMap.put(id, DEFAULT_CHARGE);
		return amount;
	}
	
	public synchronized int get(int id) {
		return wrappedMap.get(id);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final Set<Map.Entry<Integer, Integer>> set = wrappedMap.entrySet();
		int size = set.size();
		for(Map.Entry<Integer, Integer> entry : set) {
			builder.append(String.format("%d:%d", entry.getKey(), entry.getValue()));
			if(--size > 0)
				builder.append(",");
		}
		return builder.toString();
	}
}
