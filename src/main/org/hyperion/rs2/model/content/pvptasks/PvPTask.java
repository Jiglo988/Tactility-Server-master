package org.hyperion.rs2.model.content.pvptasks;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.pvptasks.impl.*;

public abstract class PvPTask {
	public abstract boolean isTask(Player p, Player o);
	
	
	public static String toString(PvPTask task) {
		if(task instanceof PureTask)
			return "pure";
		else if(task instanceof MainTask)
			return "main";
		else if(task instanceof ZerkTask)
			return "zerk";
		return "";
	}
	public static PvPTask toTask(int i) {
		switch(i) {
		case 1:
			return new PureTask();
		case 2:
			return new MainTask();
		case 3:
			return new ZerkTask();
		default:
			return null;
		}
	}
	public static int toInteger(PvPTask task) {
		if(task instanceof PureTask)
			return 1;
		else if(task instanceof MainTask)
			return 2;
		else if(task instanceof ZerkTask)
			return 3;
		return 0;
	}
}
