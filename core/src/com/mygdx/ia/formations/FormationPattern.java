package com.mygdx.ia.formations;

import java.util.List;

import com.mygdx.engine.Transform;

public abstract class FormationPattern {
	
	protected int numSlots;
	
	public FormationPattern(int numSlots) {
		this.numSlots = numSlots;
	}

	public abstract Transform getDriftOffset(List<SlotAssignment> slotAssignments);
	
	public abstract boolean supportsSlots(int numSlots);
	
//	public abstract Transform getSlotLocation(int i);
}
