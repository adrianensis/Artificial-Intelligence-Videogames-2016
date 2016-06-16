package com.mygdx.ia.statemachine;

import java.util.LinkedList;
import java.util.List;

public class State {
	
	private StateMachine stateMachine;
	private Action entryAction;
	private Action action;
	private Action exitAction;
	private List<Transition> transitions;
	
	public State() {
		
		stateMachine = null;
		transitions = new LinkedList<Transition>();
		
		entryAction = new Action() {
			
			@Override
			public void run() {}
		};
		
		action = new Action() {
			
			@Override
			public void run() {}
		};
		
		exitAction = new Action() {
			
			@Override
			public void run() {}
		};
	}
	
	public void addTransition(Transition transition){
		transitions.add(transition);
	}
	
	public void setStateMachine(StateMachine stateMachine){
		this.stateMachine = stateMachine;
	}
	
	public void updateStateMachine(){
		if(stateMachine != null)
			stateMachine.update();
	}
	
	public void resetStateMachine(){
		if(stateMachine != null)
			stateMachine.reset();
	}
	
	public StateMachine getStateMachine(){
		return stateMachine;
	}
	
	public Action getEntryAction() {
		return entryAction;
	}

	public void setEntryAction(Action entryAction) {
		this.entryAction = entryAction;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getExitAction() {
		return exitAction;
	}

	public void setExitAction(Action exitAction) {
		this.exitAction = exitAction;
	}

	public List<Transition> getTransitions() {
		
		List<Transition> totalTransitions = new LinkedList<Transition>(transitions);
		
		// si hay sub-maquina de estados obtenemos tambien sus transiciones
//		if(stateMachine != null){
//			totalTransitions.addAll(stateMachine.getTransitions());
//		}
		
		return totalTransitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public void runAction(){
		action.run();
	}
	
	public void runEntryAction(){
		entryAction.run();
	}
	
	public void runExitAction(){
		exitAction.run();
	}
}
