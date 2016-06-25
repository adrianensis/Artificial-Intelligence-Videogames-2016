package com.mygdx.game.units;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.behaviours.group.Alignment;
import com.mygdx.ia.behaviours.group.Cohesion;
import com.mygdx.ia.behaviours.group.Separation;

public class FlockingGroup extends UnitGroup {
	
	private LinkedList<BotScript> targets;

	public FlockingGroup(String key) {
		super(key);
		this.targets = new LinkedList<BotScript>();
	}
	
	public FlockingGroup(String key,List<Unit> units) {
		super(key,units);
		
		this.targets = new LinkedList<BotScript>();
		
		for (Unit unit : units) {
			targets.add(unit.getComponent(BotScript.class));
		}
	}
	
	@Override
	public void addUnit(Unit unit) {
		
		super.addUnit(unit);

		targets.add(unit.getComponent(BotScript.class));
	}

	@Override
	public void join() {
		for (BotScript bot : targets) {
			bot.clearGroupBehaviours(Cohesion.class, Separation.class, Alignment.class);
			bot.addBehaviour(new Cohesion(bot, targets, 10*Scene.SCALE));
			bot.addBehaviour(new Separation(bot, targets, 10*Scene.SCALE, 1000*Scene.SCALE));
		}
	}

	@Override
	public void split() {
		
		super.split();
		
		List<BotScript> cpy = new LinkedList<BotScript>(targets);
		
		for (BotScript bot : cpy) {
			bot.clearGroupBehaviours(Cohesion.class, Separation.class, Alignment.class);
		}
		
		for (BotScript bot : targets) {
			targets.remove(bot);
		}
		
	}
	
	@Override
	public void splitUnit(Unit unit) {
		super.splitUnit(unit);
		
		BotScript bot = unit.getComponent(BotScript.class);
		
		targets.remove(bot);
		bot.clearGroupBehaviours(Cohesion.class, Separation.class, Alignment.class);
	}

	@Override
	public void setDestiny(Vector2 destiny) {
		
		Unit leader = units.get(0);
		Vector2 leaderPos = leader.getComponent(Transform.class).position;
		
		for (int i = 0; i < units.size(); i++) {
			Unit u = units.get(i);
			Vector2 uPos = u.getComponent(Transform.class).position;
			
			u.setDestiny(destiny.cpy().add(uPos.cpy().sub(leaderPos)));
		}
	}

	@Override
	public void setTargetAttack(Unit targetAttack) {
		for (Unit unit : units) {
			unit.setTargetAttack(targetAttack);
		}
		
	}

}
