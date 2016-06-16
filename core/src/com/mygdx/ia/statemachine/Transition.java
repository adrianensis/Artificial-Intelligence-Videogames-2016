package com.mygdx.ia.statemachine;

public class Transition {

	private State targetState;
	private Condition condition;
	private Action action;
	
	public Transition(State targetState) {
		
		this.targetState = targetState;
		
		condition = new Condition() {
			
			@Override
			public boolean test() {
				return false;
			}
		};
		
		action = new Action() {
			
			@Override
			public void run() {}
		};
	}
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public void runAction(){
		action.run();
	}
	
	public boolean isTriggered(){
		return condition.test();
	}
	
	public State getTargetState() {
		return targetState;
	}
}
