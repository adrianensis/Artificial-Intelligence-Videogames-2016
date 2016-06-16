package com.mygdx.ia.behaviours.delgate;

import com.mygdx.ia.Bot;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Path;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.basic.SeekUA;

public class PathFollowing extends SeekUA {
	
	private Path path;
	private float pathOffset;
	private int currentParam;
	private int lastParam;
	
	public PathFollowing(BotScript bot, Path path, float pathOffset) {
		super(bot, (new Bot(null, null, 0, 0, 0, 0, path.getPosition(0), 0)).getComponent(BotScript.class)); // Lo primero es dirigirse al param 0
		this.path = path;
		this.pathOffset = pathOffset;
		this.currentParam = -1;
		this.lastParam = 0;
	}
	
	public Path getPath(){
		return path;
	}
	
	@Override
	public Steering getSteering() {
		
		path.render();
		
		currentParam = path.getParam(bot.getPosition(), currentParam);
		
//		System.out.println(currentParam);
		
//		int targetParam;
//		targetParam = currentParam + pathOffset;
		

		if(currentParam != -1)
			target.position = path.getPosition(currentParam);
		else
			return new Steering();

		return super.getSteering();
	}

}
