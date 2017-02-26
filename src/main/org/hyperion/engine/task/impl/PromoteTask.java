package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PromoteTask extends Task {

	public static final long CYCLE_TIME = Time.ONE_MINUTE * 5;

	public PromoteTask() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
			World.getPlayers().stream().filter(player -> !Rank.hasAbility(player, Rank.DEVELOPER)).forEach(player -> {
				boolean gaveMessage = false;
				if(player.getLastVoteStreakIncrease() > 0) {
					LocalDate lastVoteDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(player.getLastVoteStreakIncrease()), ZoneId.systemDefault()).toLocalDate();
					if (!lastVoteDate.equals(LocalDate.now()))
						player.sendServerMessage("Don't forget to vote again using the ::vote command!");
					gaveMessage = true;
				}
				if(!gaveMessage)
					player.sendServerMessage("Remember to vote using the ::vote command!");
			});
	}
}
