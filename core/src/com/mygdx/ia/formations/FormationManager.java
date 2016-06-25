package com.mygdx.ia.formations;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.ia.Bot;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.behaviours.basic.SeekU;

public class FormationManager {

	private List<SlotAssignment> slotAssignments;
	private SlotAssignment leaderSlot;
	private FormationPattern pattern;
	private Transform driftOffset;
	
	
	
	public FormationManager(FormationPattern pattern) {
		slotAssignments = new LinkedList<SlotAssignment>();
		this.pattern = pattern;
	}
	
	public void updateSlotAssignments() {
		
		for (int i = 0; i < slotAssignments.size(); i++) {
			slotAssignments.get(i).setSlotNumber(i);
		}
		
		driftOffset = pattern.getDriftOffset(slotAssignments);
	}
	
	public boolean addBot(Bot bot){
		
		int occupiedSlots = slotAssignments.size();
		
		if(pattern.supportsSlots(occupiedSlots + 1)){
			SlotAssignment sa = new SlotAssignment(bot);
			slotAssignments.add(sa);
			
			updateSlotAssignments();
			return true;
		}
		
		return false;
	}
	
	public void setLeaderBot(Bot bot){
		for (SlotAssignment sa : slotAssignments) {
			if(sa.getBot().getName() == bot.getName())
				leaderSlot = sa;
				
		}
	}

	
	
	
	public void removeBot(Bot bot){
		
		int i = 0;
		for (SlotAssignment sa : slotAssignments) {
			
			if(sa.getBot().getName() == bot.getName()){
				
				if(i >= 0 && i < slotAssignments.size()){
					slotAssignments.remove(i);
					break;
				}
				
			}
			
			i++;
		}
		
		updateSlotAssignments();
	}
	
	private Vector2 rotate(Vector2 vec, float orientation){
		
		float x = MathUtils.cos(MathUtils.degRad*(orientation))*Scene.SCALE;
		float y = MathUtils.sin(MathUtils.degRad*(orientation))*Scene.SCALE;
		
		return new Vector2(x,y);
	}
	
	public void updateSlots(){
		
		// Anchor = leaderSlot
		
		BotScript anchor = leaderSlot.getBot().getComponent(BotScript.class);
		
		int n = slotAssignments.size();
		
		for (int i = 0; i < n; i++) {
			
//			Transform relativeLoc = pattern.getSlotLocation(slotAssignments.get(i).getSlotNumber());
			Transform relativeLoc = slotAssignments.get(i).getBot().getComponent(Transform.class);
			
			Transform location = new Transform();
			
			float orientationOffset = ((360/n)*i)%360;
			float rot = anchor.getOrientation()+orientationOffset;
			
			Vector2 separationVector = rotate(new Vector2(1,1).scl(Scene.SCALE*(n-1)*2),rot);
			
			System.out.println(separationVector);
			
			location.position =  rotate(relativeLoc.position, rot).add(separationVector).add(anchor.getPosition());
			
			location.orientation = anchor.getOrientation() + relativeLoc.orientation;
			
			location.position = location.position.sub(driftOffset.position);
			
			location.orientation = location.orientation - driftOffset.orientation;
			
			BotScript bot = slotAssignments.get(i).getBot().getComponent(BotScript.class);
			bot.clearBehaviours(SeekU.class);
			bot.addBehaviour(new SeekU(bot, location));
		}
	}

}
