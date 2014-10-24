package main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

public class Game extends ApplicationAdapter {
        public static final String TITLE = "Game";
        public static final int V_WIDTH = 320;
        public static final int V_HEIGHT = 240;
        public static final int SCALE = 2;
        
        public static final float STEP = 1/60f;
        private float accum;
        
        private SpriteBatch sb;
        private OrthographicCamera cam;
        private OrthographicCamera hudcam;
        
        private GameStateManager gsm;
        
        public SpriteBatch getSpritebatch(){
            return sb;
        }
        public OrthographicCamera getCamera(){
            return cam;
        }
        public OrthographicCamera getHUDCamera(){
            return hudcam;
        }
    
	@Override
	public void create () {
            
            Gdx.input.setInputProcessor(new MyInputProcessor());
            
            sb = new SpriteBatch();
            cam = new OrthographicCamera();
            cam.setToOrtho(false,V_WIDTH,V_HEIGHT);
            hudcam = new OrthographicCamera();
            hudcam.setToOrtho(false,V_WIDTH,V_HEIGHT);
            gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
            accum += Gdx.graphics.getDeltaTime();
            while(accum >= STEP){
                accum -= STEP;
                gsm.update(STEP);
                gsm.render();
                MyInput.update();
            }
	}
}
