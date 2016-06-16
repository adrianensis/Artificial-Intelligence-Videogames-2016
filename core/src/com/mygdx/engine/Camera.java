package com.mygdx.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Este componente permite crear GameObjects con camara.
 */
public class Camera extends Component {
	
	public OrthographicCamera camera;
	private Transform target;
	
	public Camera() {
		this.target = null;
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
		this.camera.zoom = 0.5f;
	}
	
	public GameObject getTarget() {
		return target.getGameObject();
	}

	/**
	 * Funcion para establecer un target a perseguir.
	 * @param target
	 * @return
	 */
	public void setTarget(GameObject target) {
		if(target == null)
			this.target = null;
		else
			this.target = target.getComponent(Transform.class);
	}
	
	public void setZoom(float zoom){
	
		if(zoom < 0.2f)
			this.camera.zoom = 0.2f;
		else
			this.camera.zoom = zoom;
	}
	
	public float getZoom(){
		return this.camera.zoom;
	}
	
	public void addZoom(float value){
		setZoom(getZoom()+value);
	}

	@Override
	public void update() {
		float x,y;
		
		// Si existe un target, lo persigo, si no ,no.
		if(target != null){
			this.gameObject.getComponent(Transform.class).position.x = target.position.x;
			this.gameObject.getComponent(Transform.class).position.y = target.position.y;
			this.gameObject.getComponent(Transform.class).checkLimits();
		}
		
		x = this.gameObject.getComponent(Transform.class).position.x;
		y = this.gameObject.getComponent(Transform.class).position.y;
		
		
		this.camera.position.set(x,y,0);
		this.camera.update();
	}

}
