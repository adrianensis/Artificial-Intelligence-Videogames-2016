package com.mygdx.game.units;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Engine;
import com.mygdx.engine.Renderer;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Path;
import com.mygdx.ia.Pathfinding;
import com.mygdx.ia.behaviours.Behaviour;
import com.mygdx.ia.behaviours.delgate.PathFollowing;
import com.mygdx.ia.statemachine.Action;
import com.mygdx.ia.statemachine.Condition;
import com.mygdx.ia.statemachine.State;
import com.mygdx.ia.statemachine.StateMachine;
import com.mygdx.ia.statemachine.Transition;

public class BasicStateMachine extends StateMachine{
	
	private enum StateName{
		WAIT, ATTACK, GOTO
	};
	
	private Map<StateName, State> states;
	
	
	/**
	 * Esta clase representa una maquina de estados basica, desde la cual
	 * podemos generar maquinas mas complejas. Posee 3 estados basicos, que son:
	 * 
	 * WAIT: estado idle o en espera.
	 * ATTACK: es una sub-maquina que se encarga de perseguir, alcanzar y atacar al objetivo.
	 * GOTO: cuando la unidad esta viajando hacia un destino.
	 * 
	 * @param unit
	 */
	public BasicStateMachine(final Unit unit){
		
		states = new HashMap<StateName, State>();		

		
		
		
		/*
		 * STATES
		 * 
		 * Primero creamos todos los estados
		 */
		
		
		
		
		// WAIT
		State stateWait = new State();
		
		stateWait.setEntryAction(new Action() {
			
			@Override
			public void run() {
				unit.getComponent(BotScript.class).clearSingleBehaviours();
			}
		});
		
		stateWait.setAction(new Action() {
			
			@Override
			public void run() {
				Vector2 pos = unit.getComponent(Transform.class).position;
				float w = unit.getComponent(Renderer.class).getSprite().getWidth();
				UI.getInstance().drawTextWorld("WAIT", pos.x-(w/2),pos.y-(1*Scene.SCALE), Color.BLUE);
			}
		});
		
		// ATTACK
		State stateAttack = new State();
		
		// El estado attack realmente es una sub-maquina de estados
		stateAttack.setStateMachine(new AttackStateMachine(unit));

		stateAttack.setEntryAction(new Action() {
			
			@Override
			public void run() {
				unit.getComponent(BotScript.class).clearSingleBehaviours();
			}
		});
		
		// GO TO
		State stateGoTo = new State();
		stateGoTo.setEntryAction(new Action() {
			
			@Override
			public void run() {
				
				Path path = new Path(0.5f*Scene.SCALE);
				
				BotScript bot = unit.getComponent(BotScript.class);
				
				Vector2 botPosition = bot.getPosition();
				
				// PATHFINDING
				Pathfinding pathfinding = new Pathfinding(Engine.getInstance().getCurrentScene(), unit, botPosition ,unit.getDestiny());
				path.setParams(pathfinding.generate());
				
//				bot.clearBehaviours(PathFollowing.class);
				bot.clearSingleBehaviours();
				
				if( ! path.isEmpty())
					bot.addBehaviour(new PathFollowing(bot, path, 0f));
				
				unit.setDestiny(null);
			}
		});
		
		
		stateGoTo.setAction(new Action() {
			
			@Override
			public void run() {
				Vector2 pos = unit.getComponent(Transform.class).position;
				float w = unit.getComponent(Renderer.class).getSprite().getWidth();
				UI.getInstance().drawTextWorld("GO", pos.x-(w/2),pos.y-(1*Scene.SCALE), Color.BLUE);
			}
		});
		
		
		
		
		
		/*
		 * TRANSITIONS
		 */
		
		
		
		
		
		// WAIT -> ATTACK
		Transition waitToAttack = new Transition(stateAttack);

		waitToAttack.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				return unit.hasNewTargetAttack() ;
			}
		});
		
		stateWait.addTransition(waitToAttack);
		
		// GOTO -> ATTACK
		Transition gotoToAttack = new Transition(stateAttack);

		gotoToAttack.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				return unit.hasNewTargetAttack() && unit.getDestiny() != null;
			}
		});
		
		stateGoTo.addTransition(gotoToAttack);
		
		// ATTACK -> ATTACK
//		Transition attackToAttack = new Transition(stateAttack);
//
//		attackToAttack.setCondition(new Condition() {
//			
//			@Override
//			public boolean test() {
//				return unit.hasNewTargetAttack();
//			}
//		});
//		
//		stateAttack.addTransition(attackToAttack);
		
		// ATTACK -> WAIT
		Transition attackToWait = new Transition(stateWait);
		
		attackToWait.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				
				if(unit.getTargetAttack() == null)
					return true;
				
				return unit.getTargetAttack().getLife() == 0|| unit.getLife() == 0;
			}
		});
		
		attackToWait.setAction(new Action() {
			
			@Override
			public void run() {
				unit.clearTargetAttack();
			}
		});
		
		stateAttack.addTransition(attackToWait);
		
		// WAIT -> GOTO
		Transition waitToGoTo = new Transition(stateGoTo);
		
		waitToGoTo.setCondition(new Condition() {
			
			@Override
			public boolean test() {						
				return unit.getDestiny() != null;
			}
		});
		
		stateWait.addTransition(waitToGoTo);
		
		// ATTACK -> GOTO
		Transition attackToGoTo = new Transition(stateGoTo);
		
		attackToGoTo.setCondition(new Condition() {
			
			@Override
			public boolean test() {						
				return unit.getDestiny() != null;
			}
		});
		
		attackToGoTo.setAction(new Action() {
			
			@Override
			public void run() {
				unit.clearTargetAttack();
				
			}
		});
		
		stateAttack.addTransition(attackToGoTo);
		
		// GOTO -> GOTO
		Transition goToToGoTo = new Transition(stateGoTo);
		
		goToToGoTo.setCondition(new Condition() {
			
			@Override
			public boolean test() {						
				return unit.getDestiny() != null;
			}
		});
		
		stateGoTo.addTransition(goToToGoTo);
		
		// GOTO -> WAIT
		Transition goToToWait = new Transition(stateWait);
		
		goToToWait.setCondition(new Condition() {
			
			@Override
			public boolean test() {		
				
				// aqui buscamos, si hemos terminado de recorrer el path.
				
				List<Behaviour> behaviours = unit.getComponent(BotScript.class).getBehaviours();
				
				for (Behaviour b : behaviours) {
					
					if(b instanceof PathFollowing){
						
						PathFollowing pf = (PathFollowing) b;
						return pf.getPath().isFinished();						
					}
				}
				
				return false;
			}
		});
		
		stateGoTo.addTransition(goToToWait);
		
		
		
		

		/*
		 * ADD STATES TO THE STATE MACHINE
		 */
		
		
		
		
		
		this.addInitialState(stateWait);
		this.addState(stateAttack);
		this.addState(stateGoTo);
		
		states.put(StateName.WAIT, stateWait);
		states.put(StateName.ATTACK, stateAttack);
		states.put(StateName.GOTO, stateGoTo);
		
	}
	
	public State getWaitState(){
		return states.get(StateName.WAIT);
	}
	
	public State getAttackState(){
		return states.get(StateName.ATTACK);
	}
	
	public State getGotoState(){
		return states.get(StateName.GOTO);
	}
	
}
