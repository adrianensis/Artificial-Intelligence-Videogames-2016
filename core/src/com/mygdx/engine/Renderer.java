package com.mygdx.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * El componente Renderer se encarga de todo el ambito grafico.
 *
 */
public class Renderer extends Component {
	
	private Sprite sprite;
	private Texture texture;
	
	public Renderer(String textureName, float w, float h) {
		this.texture = new Texture(textureName);
		this.sprite = new Sprite(this.texture);
		sprite.setSize(w*Scene.SCALE, h*Scene.SCALE); // Según nuestra escala nuestro personaje mide 1x1.
	}
	
	/**
	 * Dibuja el sprite.
	 * @param batch
	 */
	public void render(SpriteBatch batch) {	
		UI.getInstance().drawRectangle(this.gameObject.getComponent(Transform.class).position, sprite.getWidth(), sprite.getWidth(), Color.BLUE);
		this.sprite.draw(batch);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	public void update() {
		
		Transform t = this.gameObject.getComponent(Transform.class);
		float x = t.position.x;
		float y = t.position.y;
		
		this.sprite.setOriginCenter();
		
		this.sprite.setRotation(t.orientation);
				
		this.sprite.setPosition(x -sprite.getWidth()/2, y-sprite.getHeight()/2);
		
		
	}

	
}
