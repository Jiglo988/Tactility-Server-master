package org.hyperion.rs2.model;

import java.util.LinkedList;

public class FriendList {

	public static final int SIZE = 200;

	public static final int EMPTY_FRIEND_SPOT = 0;

	private long[] friends = new long[SIZE];

	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	private long[] previousFriends = new long[SIZE];

	public void clear() {
		friends = new long[SIZE];
	}

	public long[] toArray() {
		return friends;
	}

	private boolean loaded = false;

	/**
	 * @param loaded
	 */
	public void setLoaded(boolean loaded) {
		if(loaded) {
			if(this.loaded) {
				System.out.println("Was already loaded!");
			} else {
				this.loaded = true;
				updatePreviousFriends();
			}
		} else {
			System.out.println("Invalid input");
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void updatePreviousFriends() {
		for(int i = 0; i < SIZE; i++) {
			previousFriends[i] = friends[i];
		}
	}


	public LinkedList<Integer> getChangedSlots() {
		LinkedList<Integer> changedSlots = new LinkedList<Integer>();
		for(int i = 0; i < SIZE; i++) {
			long friend = friends[i];
			long previousFriend = previousFriends[i];
			if(friend != previousFriend)
				changedSlots.add(i);
		}
		return changedSlots;
	}

	public boolean add(long friend) {
		for(int i = 0; i < friends.length; i++) {
			if(friends[i] == EMPTY_FRIEND_SPOT) {
				friends[i] = friend;
				return true;
			}
		}
		return false;
	}

	public void set(long friend, int slot) {
		friends[slot] = friend;
	}

	public boolean remove(long friend) {
		for(int i = 0; i < friends.length; i++) {
			if(friends[i] == friend) {
				friends[i] = EMPTY_FRIEND_SPOT;
				return true;
			}
		}
		return false;
	}

	public boolean contains(long friend) {
		for(long f : friends) {
			if(f == friend)
				return true;
		}
		return false;
	}
}
