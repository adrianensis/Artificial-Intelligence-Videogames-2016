package com.mygdx.ia.behaviours.group;

import java.util.List;

import com.mygdx.ia.BotScript;
import com.mygdx.ia.behaviours.Behaviour;

public abstract class GroupBehaviour extends Behaviour {
	
	protected List<BotScript> targets;
	
	public GroupBehaviour(List<BotScript> targets) {
		super();
		this.targets = targets;
	}
	
	public List<BotScript> getTargets() {
		return targets;
	}

}
