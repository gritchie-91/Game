package main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import handlers.TextureContent;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

public class Game extends ApplicationAdapter {
        public static final String TITLE = "Game";
        public static final int V_WIDTH = 480;
        public static final int V_HEIGHT = 360;
        public static final int SCALE = 1;
        
        
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
        
        public static TextureContent res;
    
	@Override
	public void create () {
            
            Gdx.input.setInputProcessor(new MyInputProcessor());
            res = new TextureContent();
            res.loadTexture("res/images/KingWalk.png", "KingWalk");
            res.loadTexture("res/images/Play.png", "play");
//            res.loadTexture("res/images/hud_spritesheet.png", "hud");
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
