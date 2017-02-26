package org.hyperion.rs2.model;


public class NPCFacing {
	public static void faceBankers(Player player) {
		for(NPC n : player.getRegion().getNpcs()) {
			if(n.getDefinition().getId() == 494 || n.getDefinition().getId() == 495) {
				if(n.getSpawnPosition().getY() == 3366) {
					n.cE.face(n.getSpawnPosition().getX(), 3367);
				} else if(n.getSpawnPosition().getY() == 3353) {
					n.cE.face(n.getSpawnPosition().getX(), 3367);
				}
			}
		}
	}

	public static void faceBankers() {
		for(NPC n : World.npcs) {
			if(n.getDefinition().getId() == 494 || n.getDefinition().getId() == 495) {
				if(n.getSpawnPosition().getY() == 3366) {
					n.cE.face(n.getSpawnPosition().getX(), 3367);
				} else if(n.getSpawnPosition().getY() == 3353) {
					n.cE.face(n.getSpawnPosition().getX(), 3367);
				} else if(n.getSpawnPosition().getX() == 3187) {
					n.cE.face(3186, n.getSpawnPosition().getY());
				}


				if(n.getSpawnPosition().getX() > 3094 && n.getSpawnPosition().getZ() <= 3099) {
					if(n.getSpawnPosition().getY() == 3492) {
						n.cE.face(n.getSpawnPosition().getX(), 3493);
					} else
						n.cE.face(n.getSpawnPosition().getX() - 1, n.getSpawnPosition().getY());
				}
			}
		}
	}
}
