package com.mygdx.game.units;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public abstract class UnitGroup {

	protected List<Unit> units;
	
	public UnitGroup() {
		units = new LinkedList<Unit>();
	}
	
	public UnitGroup(List<Unit> units) {
		this.units = new LinkedList<Unit>(units);
		
		for (Unit unit : this.units) {
			unit.setGroup(this);
		}
	}
	
	public void addUnit(Unit unit){
		unit.setGroup(this);
		units.add(unit);
	}
	
	public List<Unit> getUnits(){
		return units;
	}
	
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	public abstract void join();
	
	public void split(){
		for (Unit unit : units) {
			unit.setGroup(null);
		}
	};
	
	public abstract void setDestiny(Vector2 destiny);
	
	public abstract void setTargetAttack(Unit targetAttack);
}
