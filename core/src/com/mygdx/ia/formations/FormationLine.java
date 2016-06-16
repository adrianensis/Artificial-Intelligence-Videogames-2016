package com.mygdx.ia.formations;

import java.util.List;

import com.mygdx.engine.Transform;

public class FormationLine extends FormationPattern {

	public FormationLine(int numSlots) {
		super(numSlots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Transform getDriftOffset(List<SlotAssignment> slotAssignments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsSlots(int numSlots) {
		// TODO Auto-generated method stub
		return false;
	}

}
