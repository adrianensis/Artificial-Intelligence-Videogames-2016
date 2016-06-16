package com.mygdx.game.units;

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
import com.mygdx.ia.behaviours.basic.ArriveUA;
import com.mygdx.ia.behaviours.delgate.PathFollowing;
import com.mygdx.ia.behaviours.delgate.Persue;
import com.mygdx.ia.statemachine.Action;
import com.mygdx.ia.statemachine.Condition;
import com.mygdx.ia.statemachine.State;
import com.mygdx.ia.statemachine.StateMachine;
import com.mygdx.ia.statemachine.Transition;

public class AttackStateMachine extends StateMachine {
	
	private void followTarget(Unit unit){
		/*
		 * Pathfinding para que persiga al target
		 */
						
		Path path = new Path(0.5f*Scene.SCALE);
		
		BotScript bot = unit.getComponent(BotScript.class);
		
		Vector2 botPosition = bot.getPosition();
		Vector2 targetPostition = unit.getTargetAttack().getComponent(BotScript.class).getPosition();
		
		// PATHFINDING
		Pathfinding pathfinding = new Pathfinding(Engine.getInstance().getCurrentScene(), unit, botPosition ,targetPostition);
		path.setParams(pathfinding.generate());
		
//		bot.clearBehaviours(PathFollowing.class);
		bot.clearSingleBehaviours();
		
		if( ! path.isEmpty())
			bot.addBehaviour(new PathFollowing(bot, path, 0f));
	}
	
	public AttackStateMachine(final Unit unit) {
		super();
		
		
		
		
		/*
		 * STATES
		 */
		
		
		
		
		// INIT
		State stateInit = new State();
		
		// PERSUE
		State statePersue = new State();
		
		statePersue.setEntryAction(new Action() {
			
			@Override
			public void run() {
				followTarget(unit);
			}
		});
		
		statePersue.setAction(new Action() {
			
			@Override
			public void run() {
				
				Vector2 targetVel = unit.getTargetAttack().getComponent(BotScript.class).getVelocity();
				
				if( ! targetVel.isZero()){
					followTarget(unit);
				}
				
				Vector2 pos = unit.getComponent(Transform.class).position;
				float w = unit.getComponent(Renderer.class).getSprite().getWidth();
				UI.getInstance().drawTextWorld("PERSUE", pos.x-(w/2),pos.y-(1*Scene.SCALE), Color.RED);
			}
		});
		
		// ATTACK
		State stateAttack = new State();
		
		stateAttack.setEntryAction(new Action() {
			
			@Override
			public void run() {
				unit.getTargetAttack().setTargetAttack(unit);
			}
			
		});
		
		stateAttack.setAction(new Action() {
			
			@Override
			public void run() {
				
				Vector2 pos = unit.getComponent(Transform.class).position;
				float w = unit.getComponent(Renderer.class).getSprite().getWidth();
				UI.getInstance().drawTextWorld("ATTACK", pos.x-(w/2),pos.y-(1*Scene.SCALE), Color.RED);
					
				unit.attack(unit.getTargetAttack());

			}
		});
		
		
		
		
		/*
		 * TRANSITIONS
		 */
		
		
		
		
		// INIT -> PERSUE
		Transition initToPersue = new Transition(statePersue);

		initToPersue.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				return true; // siempre true, forzamos a que siempre haga esta transicion cuando esta en el estado INIT.
			}
		});
		
		stateInit.addTransition(initToPersue);
		
		// PERSUE -> ATTACK
		Transition persueToAttack = new Transition(stateAttack);

		persueToAttack.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				
				/*
				 * Comprobamos si ya no hay target o hay uno cerca.
				 */
				
				if(unit.getTargetAttack() == null)
					return false;
				
				Vector2 unitPos = unit.getComponent(BotScript.class).getPosition();
				Vector2 targetPos = unit.getTargetAttack().getComponent(BotScript.class).getPosition();
				
				return targetPos.dst(unitPos) < unit.getRangeAttack();
			}
		});
		
		persueToAttack.setAction(new Action() {
			
			@Override
			public void run() {
				unit.getComponent(BotScript.class).clearSingleBehaviours();
			}
		});
		
		statePersue.addTransition(persueToAttack);
		
		// ATTACK -> PERSUE
		Transition attackToPersue = new Transition(statePersue);

		attackToPersue.setCondition(new Condition() {
			
			@Override
			public boolean test() {
				
				/*
				 * Comprobamos si el target esta lejos.
				 */
				
				Vector2 unitPos = unit.getComponent(BotScript.class).getPosition();
				Vector2 targetPos = unit.getTargetAttack().getComponent(BotScript.class).getPosition();
				
				return targetPos.dst(unitPos) > unit.getRangeAttack();
			}
		});
		
		stateAttack.addTransition(attackToPersue);
		
		
		
	
		/*
		 * ADD STATES TO THE STATE MACHINE
		 */
		
		
		
		
		this.addInitialState(stateInit);
		this.addState(statePersue);
		this.addState(stateAttack);
	}
}
