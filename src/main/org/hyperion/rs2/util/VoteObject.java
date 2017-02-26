package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Player;

/**
 * @author Arsen Maxyutov.
 */

public class VoteObject {

	public static final long VOTE_LIMIT = 1000 * 60 * 60 * 24;

	public static VoteObject getVoteObject(Player player) {
		String name = player.getName();
		String IP = player.getFullIP().split(":")[0];
		for(VoteObject vo : VoteSystem.votes) {
			if(vo.getName().equals(name.toLowerCase()) || vo.getIP().equals(IP)) {
				return vo;
			}
		}
		return null;
	}

	private String IP;

	private void save() {
		TextUtils.writeToFile("./data/voteobjects.txt", name + "," + IP + "," + time);
	}

	private String name;

	private long time = System.currentTimeMillis();

	public VoteObject(String name, String usersIP) {
		String[] parts = usersIP.split(":");
		this.IP = parts[0];
		this.name = name.toLowerCase();
		save();
	}

	public VoteObject(String name, String IP, long time) {
		this.IP = IP;
		this.name = name.toLowerCase();
		this.time = time;
	}

	public boolean canVote() {
		return System.currentTimeMillis() - time > VOTE_LIMIT;
	}

	public String getName() {
		return name;
	}

	public String getIP() {
		return IP;
	}

	public void updateTime() {
		time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}
}
