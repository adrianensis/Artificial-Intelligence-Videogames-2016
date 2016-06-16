package com.mygdx.ia.formations;

import java.util.List;

import com.mygdx.engine.Transform;

public class FormationCircle extends FormationPattern {

	public FormationCircle(int numSlots) {
		super(numSlots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Transform getDriftOffset(List<SlotAssignment> slotAssignments) {
		
		Transform center = new Transform();
		
		// Calculamos el centro de masas
		for (SlotAssignment sa : slotAssignments) {
			Transform location = sa.getBot().getComponent(Transform.class);
			center.position = center.position.add(location.position);
			center.orientation += location.orientation;
		}
		
		int numberOfAssignments = slotAssignments.size();
		center.position = center.position.scl(1/numberOfAssignments);
		center.orientation /= numberOfAssignments;
		return center;
	}

	@Override
	public boolean supportsSlots(int numSlots) {
		return true;
	}
	
}
