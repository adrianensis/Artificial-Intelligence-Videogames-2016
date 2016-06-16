package com.mygdx.ia.statemachine;

import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase representa una maquina de estados.
 *
 */
public class StateMachine {
	
	private List<State> states; // conjunto de estados
	private State initialState; // estado inicial
	private State currentState; // referencia al estado actual
	
	public StateMachine() {
		states = new LinkedList<State>();
		initialState = null;
	}
	
	/**
	 * Reinicia la maquina.
	 */
	public void reset(){
		currentState = initialState;
	}
	
	/**
	 * Actualiza la maquina.
	 */
	public void update(){
		
		// Si hay el estado actual contiene una sub-maquina la actualiza
		currentState.updateStateMachine();
		
		// Buscamos una transicion activada
		
		Transition triggeredTransition = null;
		
		for (Transition t : currentState.getTransitions()) {
			if(t.isTriggered()){
				triggeredTransition = t;
				break;
			}
		}
		
		// Si econtramos una, la ejecutamos.
		
		if(triggeredTransition != null){
			
			State targetState = triggeredTransition.getTargetState();
			
			currentState.runExitAction();
			triggeredTransition.runAction();
			targetState.runEntryAction();
			
			currentState.resetStateMachine(); // nuevo
			
			currentState = targetState;
			
		}else{
			
			currentState.runAction();
		}
		
	}
	
	public StateMachine setStates(List<State> states) {
		this.states = states;
		return this;
	}
	
	public List<State> getStates() {
		return states;
	}
	
	public StateMachine addState(State state){
		this.states.add(state);
		return this;
	}
	
	public StateMachine addInitialState(State state){
		this.initialState = state;
		currentState = initialState;
		this.states.add(state);
		return this;
	}
}
