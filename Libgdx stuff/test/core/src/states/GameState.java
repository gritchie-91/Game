package states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import main.Game;
import handlers.GameStateManager;


public abstract class GameState {
    
    protected GameStateManager gsm;
    protected Game game;
    
    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudcam;
    
    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpritebatch();
        cam = game.getCamera();
        hudcam = game.getHUDCamera();
    }
    
    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
