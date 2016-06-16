package com.mygdx.ia.formations;

import com.mygdx.ia.Bot;

public class SlotAssignment {

	private Bot bot;
	private int slotNumber;
	
	public SlotAssignment(Bot bot) {
		this.bot = bot;
		this.slotNumber = 0;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
	
	
}
