package com.mygdx.game.units;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.GameObject;
import com.mygdx.engine.Script;

public abstract class UnitGroup extends GameObject {

	protected List<Unit> units;
	
	public UnitGroup(String key) {
		super(key);
		units = new LinkedList<Unit>();
		addScript();
	}
	
	public UnitGroup(String key, List<Unit> units) {
		super(key);
		
		units = new LinkedList<Unit>();
		
		
		this.units = new LinkedList<Unit>(units);
		
		for (Unit unit : this.units) {
			unit.setGroup(this);
		}
		
		addScript();
	}
	
	private void addScript(){
		this.addComponent(new Script() {
			
			UnitGroup group;
			
			@Override
			public void update() {
				group.updateGroup();
				
			}
			
			@Override
			public void start() {
				group = (UnitGroup) gameObject;
				
			}
		});
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
		
		for (Unit unit : this.units) {
			unit.setGroup(this);
		}
	}
	
	public abstract void join();
	
	public void split(){
		for (Unit unit : units) {
			unit.setGroup(null);
		}
	};
	
	public void splitUnit(Unit unit){
		unit.setGroup(null);
	};
	
	public abstract void setDestiny(Vector2 destiny);
	
	public abstract void setTargetAttack(Unit targetAttack);
	
	public void updateGroup(){
		for (Unit unit : units) {
			if(unit.getLife() == 0){
				this.splitUnit(unit);
			}
		}
	};
}
