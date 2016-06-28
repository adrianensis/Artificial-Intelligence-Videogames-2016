package com.mygdx.engine;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


/**
 *
 * Clase para funciones de UI
 *
 */
public class UI {

	private static UI instance = null;
	private boolean enabled;

	public static UI getInstance(){
		if(instance == null)
			instance = new UI();
		return instance;
	}

	/**
	 * Esto es para que se puedan enviar peticiones de dibujado desde los scripts de IA.
	 *
	 * Las peticiones (commands o comandos de dibujado), se almacenan y se dibujan todos en la fase de render.
	 */

	private LinkedList<DrawCommand> worldDrawCommands = new LinkedList<DrawCommand>();
	private LinkedList<DrawCommand> drawCommands = new LinkedList<DrawCommand>();
	private LinkedList<DrawCommand> textCommands = new LinkedList<DrawCommand>();

	private ShapeRenderer worldDebugRenderer = new ShapeRenderer();
	private ShapeRenderer debugRenderer = new ShapeRenderer();
	private Matrix4 projectionMatrix;
	private OrthographicCamera cam;
	private SpriteBatch textBatch;

    private BitmapFont font;

    private UI(){

    	enabled = true;

    	/*
    	 * CONSTUIMOS LA FUENTE
    	 */
    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Hack-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12; // TAMAÑO
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()<>?:;,-+*/_ []{}|@"; // CARACTERES QUE USAREMOS
        // These characters should not repeat!

        font = generator.generateFont(parameter);

        generator.dispose();

        textBatch = new SpriteBatch();
	}

    public boolean isEnabled() {
		return enabled;
	}

    public void enable(){
    	enabled = true;
    }

    public void disable(){
    	enabled = false;
    }

	public void setCamera(OrthographicCamera cam){
		this.cam = cam;
		this.projectionMatrix = cam.combined;
	}

	public void setTextBatch(SpriteBatch textBatch){
		this.textBatch = textBatch;
	}

	public void renderWorldCommands(){
		if(enabled){
			for (DrawCommand drawCommand : worldDrawCommands) {
				drawCommand.render();
			}

			
		}
		
		worldDrawCommands.clear(); // vaciamos la lista de comandos.
	}

	public void renderCommands(){
		if(enabled){
			for (DrawCommand drawCommand : drawCommands) {
				drawCommand.render();
			}

			
		}
		
		drawCommands.clear(); // vaciamos la lista de comandos.
	}

	public void renderTextCommands(){
		if(enabled){
			textBatch.begin();
			for (DrawCommand drawCommand : textCommands) {
				drawCommand.render();
			}
			textBatch.end();

			
		}
		
		textCommands.clear(); // vaciamos la lista de comandos.
	}

	/**
	 *
	 *  FUNCIONES DE DIBUJADO
	 */

	/*
	 * Dibujar una linea
	 */
    public void drawLine(final Vector2 start, final Vector2 end, final Color color, final boolean filled)
    {
    	drawCommands.add(new DrawCommand() {

			@Override
			public void render() {
				if(filled)
					debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					debugRenderer.begin(ShapeRenderer.ShapeType.Line);
				debugRenderer.setColor(color);
				debugRenderer.line(start.cpy().scl(1), end.cpy().scl(1));
				debugRenderer.end();
			}
		});

    }

    public void drawLineWorld(final Vector2 start, final Vector2 end, final Color color,final boolean filled){
    	worldDrawCommands.add(new DrawCommand() {

			@Override
			public void render() {
				worldDebugRenderer.setProjectionMatrix(projectionMatrix);
				if(filled)
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Line);
		        worldDebugRenderer.setColor(color);
		        worldDebugRenderer.line(start.cpy().scl(1), end.cpy().scl(1));
		        worldDebugRenderer.end();
			}
		});
    }

    /*
	 * Dibujar un circulo
	 */
    public void drawCircle(final Vector2 center, final float radius, final Color color, final boolean filled)
    {
    	drawCommands.add(new DrawCommand() {

			@Override
			public void render() {
				if(filled)
					debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		        debugRenderer.setColor(color);
		        debugRenderer.circle(center.x, center.y, radius);
		        debugRenderer.end();
			}
		});

    }

    public void drawCircleWorld(final Vector2 center, final float radius, final Color color,final boolean filled){
    	worldDrawCommands.add(new DrawCommand() {

			@Override
			public void render() {

				worldDebugRenderer.setProjectionMatrix(projectionMatrix);
				if(filled)
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Line);
		        worldDebugRenderer.setColor(color);
		        worldDebugRenderer.circle(center.x, center.y, radius);
		        worldDebugRenderer.end();
			}
		});
    }

    /*
     * Dibujar texto
     */
    public void drawText(final String text, final float x, final float y, final Color color){
    	textCommands.add(new DrawCommand() {

			@Override
			public void render() {
		        font.setColor(color);
		        font.draw(textBatch, text,x,y);
			}
		});
    }

    public void drawTextWorld(final String text, final float x, final float y, final Color color){
    	textCommands.add(new DrawCommand() {

			@Override
			public void render() {
		        font.setColor(color);

		        final Vector3 world_pos = new Vector3(x,y,0);
	    		final Vector3 screen_pos = cam.project(world_pos);

	        	font.draw(textBatch, text,screen_pos.x,screen_pos.y);

			}
		});
    }

    /*
     * Dibujar rectángulo
     */
    public void drawRectangle(final Vector2 center, final float width, final float height, final Color color, final boolean filled){
    	drawCommands.add(new DrawCommand() {

			@Override
			public void render() {
				if(filled)
					debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					debugRenderer.begin(ShapeRenderer.ShapeType.Line);
				debugRenderer.setColor(color);
				debugRenderer.rect(center.x - (width/2), center.y - (height/2), width, height);
				debugRenderer.end();
			}
		});
    }

    public void drawRectangleWorld(final Vector2 center, final float width, final float height, final Color color,final boolean filled){
    	worldDrawCommands.add(new DrawCommand() {

			@Override
			public void render() {

				worldDebugRenderer.setProjectionMatrix(projectionMatrix);
				if(filled)
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Filled);
				else
					worldDebugRenderer.begin(ShapeRenderer.ShapeType.Line);
		        worldDebugRenderer.setColor(color);
		        worldDebugRenderer.rect(center.x - (width/2), center.y - (height/2), width, height);
		        worldDebugRenderer.end();
			}
		});
    }

    /*
     * Dibujar grid, usa la función drawRectangle
     */
    public void drawGridWorld(final Vector2 leftBottom, final int rows, final int cols, final float width, final float height , final Color color){
    	for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Vector2 center = new Vector2(leftBottom.x + (width*c) + (width/2), leftBottom.y + (height*r) +(height/2) );
				UI.this.drawRectangleWorld(center, width, height, color, false);
			}
		}
    }
}
