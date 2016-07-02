package com.mygdx.game.units;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ia.formations.FormationCircle;
import com.mygdx.ia.formations.FormationManager;

public class FormationGroup extends UnitGroup {
	
	private FormationManager fm;
	private FormationCircle fc;
	
	public FormationGroup(String key) {
		super(key);
		this.fc = new FormationCircle(0);
		this.fm = new FormationManager(this.fc);
		
	}
	
	public FormationGroup(String key,List<Unit> units) {
		super(key,units);
		this.fc = new FormationCircle(0);
		this.fm = new FormationManager(this.fc);
		
		for (Unit unit : units) {
			fm.addBot(unit);
		}
		
		fm.setLeaderBot(units.get(0));
	}
	
	@Override
	public void addUnit(Unit unit) {
		super.addUnit(unit);
		fm.addBot(unit);
	}

	@Override
	public void join() {
		fm.updateSlots();
		super.split();
	}

	@Override
	public void split() {
		super.split();
	}
	
	@Override
	public void splitUnit(Unit unit) {
		super.splitUnit(unit);
	}

	@Override
	public void setDestiny(Vector2 destiny) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTargetAttack(Unit targetAttack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGroup() {
		super.updateGroup();
	}
	
	
}
